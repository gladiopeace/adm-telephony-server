package com.admtel.telephonyserver.freeswitch;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.admtel.telephonyserver.asterisk.ASTSwitch;
import com.admtel.telephonyserver.asterisk.events.ASTEvent.EventType;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.core.BasicIoMessage;
import com.admtel.telephonyserver.core.QueuedMessageHandler;
import com.admtel.telephonyserver.core.Registrar;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.SimpleMessageHandler;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.core.Channel.CallOrigin;
import com.admtel.telephonyserver.core.Switch.SwitchStatus;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.freeswitch.events.FSChannelBridgeEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelCreateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelDataEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelOriginateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSCommandReplyEvent;
import com.admtel.telephonyserver.freeswitch.events.FSEvent;
import com.admtel.telephonyserver.freeswitch.events.FSRegisterEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelCreateEvent.CallDirection;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.requests.Request;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.CodecsUtils;

public class FSSwitch extends Switch implements IoHandler, TimerNotifiable {

	static Logger log = Logger.getLogger(ASTSwitch.class);

	IoSession session;

	public static final int CONNECT_TIMEOUT = 1000;
	public static final int RECONNECT_AFTER = 5000;

	private SocketConnector connector;

	protected String encodingDelimiter;
	protected String decodingDelimiter;

	Timer reconnectTimer = null;
	private enum State {Connecting, LoggingIn, LoggedIn, Disconnecting, Disconnected};
	
	State state = State.Disconnected;
	
	public FSSwitch(SwitchDefinition definition) {
		super(definition);
		this.encodingDelimiter = "\n\n";
		this.decodingDelimiter = "\n\n";
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
				Charset.forName("UTF-8"), encodingDelimiter, decodingDelimiter);
		textLineCodecFactory.setDecoderMaxLineLength(8192);
		textLineCodecFactory.setEncoderMaxLineLength(8192);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(this);		
	}

	@Override
	public void start() {
		super.start();
		reconnectTimer = Timers.getInstance().startTimer(this, RECONNECT_AFTER,
				false, null);
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	@Override
	public Result originate(String destination, long timeout, String callerId,
			String calledId, String script, String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable exception)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		messageHandler
				.putMessage(new BasicIoMessage(session, (String) message));

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.warn("Disconnected from switch "+this.getSwitchId());
		state = State.Disconnected;		
		this.start();

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
		String username = FSSwitch.this.getDefinition().getUsername();
		String password = FSSwitch.this.getDefinition().getPassword();
		session.write("auth " + password);
		state = State.LoggingIn;

	}

	@Override
	public boolean onTimer(Object data) {
		if (isConnected())
			return true;// stop the timer
		if (getDefinition().isEnabled() && state == State.Disconnected) {
			return connect();
		}		
		return true;
	}

	private boolean isConnected() {
		return (session != null && session.isConnected());
	}

	private boolean connect() {
		log.debug(String.format("Trying to connect to %s:%d", getDefinition()
				.getAddress(), getDefinition().getPort()));		
		state = State.Connecting;
		try {
			ConnectFuture connectFuture = connector
					.connect(new InetSocketAddress(getDefinition().getAddress(),
							getDefinition().getPort()));
			connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
			session = connectFuture.getSession();
			log.debug(String.format("Connected to %s:%d", getDefinition()
					.getAddress(), getDefinition().getPort()));
			return true;
		} catch (Exception e) {
			log.warn(AdmUtils.getStackTrace(e));
			state = State.Disconnected;			
		}
		return false;
	}

	@Override
	public void processBasicIoMessage(BasicIoMessage message) {
		switch (state){
		case LoggingIn:
			if (message != null) {
				FSEvent event = FSEvent.buildEvent(FSSwitch.this.getSwitchId(),
						message.getMessage());
				switch (event.getEventType()) {
				case CommandReply: {
					FSCommandReplyEvent cre = (FSCommandReplyEvent) event;
					if (cre.isSuccess()) {
						FSSwitch.this.setStatus(SwitchStatus.Ready);
						session.write("event plain all"); // TODO, create new
															// state to
						// check for return of event
						// filter
						state = State.LoggedIn;
						setStatus(SwitchStatus.Ready);
					} else {
						log.warn("Session failed to connect "
								+ session.getRemoteAddress());
					}
				}
					break;
				}
			}
			break;
		case LoggedIn:
			if (message != null) {
				FSEvent event = FSEvent.buildEvent(FSSwitch.this.getSwitchId(),
						message.getMessage());
				//log.debug(String.format("%s\n\n", message.getMessage()));
				if (event == null) {
					/* log.debug("Didn't create Event for message ..."); */
					return;
				}
				switch (event.getEventType()) {
				
				case FsRegister: {
					FSRegisterEvent registerEvent = (FSRegisterEvent) event;
					if (registerEvent.getRegistered()) {
						Registrar.getInstance().register(
								new UserLocation(registerEvent.getSwitchId(),
										"UU", registerEvent.getUser()));
					} else {
						Registrar.getInstance().unregister(
								registerEvent.getUser());
					}
				}
					break;
				case ChannelCreate:
				{
					FSChannelCreateEvent cce = (FSChannelCreateEvent) event;
					FSChannel channel = new FSChannel(FSSwitch.this, cce
							.getChannelId(), message.getSession());
					switch (cce.getDirection()){
					case Inbound:
						channel.setCallOrigin(CallOrigin.Inbound);
						break;
					case Outbound:
						channel.setCallOrigin(CallOrigin.Outbound);
						break;
					}
					
					FSSwitch.this.addChannel(channel);

				}
					break;
				}
				if (event instanceof FSChannelEvent) {
					FSChannelEvent channelEvent = (FSChannelEvent) event;
					FSChannel channel = (FSChannel) FSSwitch.this
							.getChannel(channelEvent.getChannelId());
					if (channel == null){
						return;
					}
					
						switch (event.getEventType()) {
						case ChannelData:
							// Replace the iosession, with the session from the
							// incoming connection
							
							channel.setIoSession(message.getSession());
							break;
						case ChannelOriginate: {
							FSChannelOriginateEvent coe = (FSChannelOriginateEvent) event;
							FSChannel otherChannel = (FSChannel) FSSwitch.this
									.getChannel(coe.getDestinationChannel());
							if (otherChannel != null) {
								otherChannel.putMessage(channelEvent);
							}
						}
							break;
						case ChannelBridge:{
							FSChannelBridgeEvent cbe = (FSChannelBridgeEvent) event;
							FSChannel otherChannel = (FSChannel) FSSwitch.this.getChannel(cbe.getPeerChannelId());
							if (otherChannel != null){
								otherChannel.putMessage(channelEvent);
							}
						}
						break;
						}
						channel.putMessage(channelEvent);
					
				}
			}			
			break;
		}
	}
}
