package com.admtel.telephonyserver.freeswitch;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import com.admtel.telephonyserver.config.SwitchListenerDefinition;
import com.admtel.telephonyserver.config.SwitchType;
import com.admtel.telephonyserver.core.*;
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
import com.admtel.telephonyserver.core.Switch.SwitchStatus;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.events.RegisterExpireEvent;
import com.admtel.telephonyserver.events.RegisteredEvent;
import com.admtel.telephonyserver.events.UnregisteredEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelBridgeEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelCreateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelDataEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelOriginateEvent;
import com.admtel.telephonyserver.freeswitch.events.FSChannelOutgoingEvent;
import com.admtel.telephonyserver.freeswitch.events.FSCommandReplyEvent;
import com.admtel.telephonyserver.freeswitch.events.FSEvent;
import com.admtel.telephonyserver.freeswitch.events.FSRegisterEvent;
import com.admtel.telephonyserver.freeswitch.events.FSRegisterExpireEvent;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.misc.VariableMap;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.CodecsUtils;
import java.util.List;

public class FSSwitch extends Switch implements IoHandler, TimerNotifiable {

	static Logger log = Logger.getLogger(FSSwitch.class);

	IoSession session;

	public static final int CONNECT_TIMEOUT = 1000;
	public static final int RECONNECT_AFTER = 5000;

	private SocketConnector connector;

	protected String encodingDelimiter;
	protected String decodingDelimiter;

	Timer reconnectTimer = null;

	private enum State {
		Connecting, LoggingIn, LoggedIn, Disconnecting, Disconnected
	};

	State state = State.Disconnected;

	public FSSwitch(SwitchDefinition definition) {
		super(definition);
		this.encodingDelimiter = "\n\n";
		this.decodingDelimiter = "\n\n";
		connector = new NioSocketConnector();		
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
				Charset.forName("UTF-8"), encodingDelimiter, decodingDelimiter);
		textLineCodecFactory.setDecoderMaxLineLength(16384);
		textLineCodecFactory.setEncoderMaxLineLength(16384);
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
			String calledId, String script, VariableMap data) {
        AdmAddress admAddress = AdmAddress.fromString(destination);
        if (admAddress == null){
        	return Result.InvalidAddress;
        }
        String dialStr = getAddressTranslator().translate(admAddress);
        String variables=null;
        if (data != null && data.size()>0){
        	variables = data.getDelimitedVars("=", ",");
        }
        if (variables != null && variables.length() > 0){
	        String switchCmdStr = String.format("bgapi originate {script=%s,%s}%s 555555", script, variables, dialStr);
	        log.trace("Sending originate request : " + switchCmdStr);
	        session.write(switchCmdStr);        	
        }
        else{
	        String switchCmdStr = String.format("bgapi originate {script=%s}%s 555555", script, dialStr);
	        log.trace("Sending originate request : " + switchCmdStr);
	        session.write(switchCmdStr);
        }
//        List<SwitchListener> listeners = SwitchListeners.getInstance().getBySwitchType(SwitchType.Freeswitch);
//        if (listeners.size() > 0){
//            SwitchListenerDefinition definition = listeners.get(0).getDefinition();//TODO, now we're taking the first one in the list
//            //TODO add more parameters
//            String switchCmdStr = String.format("bgapi originate %s &socket(%s:%d async full)\n", dialStr, definition.getAddress(), definition.getPort());
//            log.trace("Sending originate request : " + switchCmdStr);
//            session.write(switchCmdStr);
//
//        }
		return Result.Ok;
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
		log.warn("Disconnected from switch " + this.getSwitchId());
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
					.connect(new InetSocketAddress(
							getDefinition().getAddress(), getDefinition()
									.getPort()));
			connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
			session = connectFuture.getSession();
			log.debug(String.format("Connected to %s:%d", getDefinition()
					.getAddress(), getDefinition().getPort()));
			return true;
		} catch (Exception e) {
			log.warn(e.getMessage());
			state = State.Disconnected;
		}
		return false;
	}

	@Override
	public void processBasicIoMessage(BasicIoMessage message) {
//		log.debug(String.format("Switch (%s) : \n%s",
//				getSwitchId(), message.getMessage()));

		switch (state) {
		case LoggingIn:
			if (message != null) {
				FSEvent event = FSEvent.buildEvent(FSSwitch.this.getSwitchId(),
						message.getMessage());
				if (event == null) return;
				switch (event.getEventType()) {
				case CommandReply: {
					FSCommandReplyEvent cre = (FSCommandReplyEvent) event;
					if (cre.isSuccess()) {
						FSSwitch.this.setStatus(SwitchStatus.Started);
						session.write("event plain all"); // TODO, create new
															// state to
						//session.write("event plain "+FSEvent.EVENTS_REPORT);
						// check for return of event
						// filter
						state = State.LoggedIn;
						setStatus(SwitchStatus.Started);
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
				
				if (event != null) {
					//log.trace(event);
				}
				else{
					//log.warn("Event is null");
					return;
				}
				switch (event.getEventType()) {

				case FsRegister: {
					FSRegisterEvent registerEvent = (FSRegisterEvent) event;
					if (registerEvent.getRegistered()) {
						EventsManager.getInstance().onEvent(
								new RegisteredEvent(registerEvent.getSwitchId(), 
										registerEvent.getProtocol(), registerEvent.getUser()));
					} else {
						EventsManager.getInstance().onEvent(
								new UnregisteredEvent(registerEvent.getUser()));
					}
				}
					break;
				case FsRegisterExpire:{
					FSRegisterExpireEvent registerExpireEvent = (FSRegisterExpireEvent) event;
					EventsManager.getInstance().onEvent(new RegisterExpireEvent(registerExpireEvent.getUser()));
				}
				break;
				case ChannelCreate:
				case ChannelData:	//The order of these events are not guaranteed. (the can come on different threads too). need to synchronize
				{
					if (!isAcceptingCalls()) return;
					FSChannelEvent ce = (FSChannelEvent) event;
					synchronized (this){
						FSChannel channel = (FSChannel) getChannel(ce.getChannelId());
						if (channel == null){
							channel = new FSChannel(FSSwitch.this,
									ce.getChannelId(), message.getSession());
							addChannel(channel);
						}
						if (event.getEventType() == FSEvent.EventType.ChannelData){
							channel.setIoSession(message.getSession());
						}
					}

				}
					break;
				}
				if (event instanceof FSChannelEvent) {
					FSChannelEvent channelEvent = (FSChannelEvent) event;
					FSChannel channel = (FSChannel) FSSwitch.this
							.getChannel(channelEvent.getChannelId());
					if (channel == null) {
						log.warn(String.format("Got event %s , but channel not found in the switch ", event));
						return;
					}

					channel.putMessage(channelEvent);					

				}
			}
			break;
		}
	}
}
