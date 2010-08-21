package com.admtel.telephonyserver.asterisk;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.UUID;

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

import com.admtel.telephonyserver.asterisk.events.ASTChannelEvent;
import com.admtel.telephonyserver.asterisk.events.ASTDialEvent;
import com.admtel.telephonyserver.asterisk.events.ASTEvent;
import com.admtel.telephonyserver.asterisk.events.ASTNewChannelEvent;
import com.admtel.telephonyserver.asterisk.events.ASTPeerStatusEvent;
import com.admtel.telephonyserver.asterisk.events.ASTResponseEvent;
import com.admtel.telephonyserver.asterisk.events.ASTEvent.EventType;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.core.BasicIoMessage;
import com.admtel.telephonyserver.core.MessageHandler;
import com.admtel.telephonyserver.core.QueuedMessageHandler;
import com.admtel.telephonyserver.core.Registrar;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.SimpleMessageHandler;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.core.Switch.SwitchStatus;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.utils.AdmUtils;

public class ASTSwitch extends Switch implements IoHandler, TimerNotifiable {

	// /////////////////////////////////////////////////////
	// State classes
	//	
	static Logger log = Logger.getLogger(ASTSwitch.class);

	MessageHandler messageHandler;
	IoSession session;

	public static final int CONNECT_TIMEOUT = 1000;
	public static final int RECONNECT_AFTER = 5000;

	private SocketConnector connector;

	protected String encodingDelimiter;
	protected String decodingDelimiter;

	Timer reconnectTimer = null;

	private class LoggingInState extends SimpleMessageHandler {

		public LoggingInState() {
			String username = ASTSwitch.this.getDefinition().getUsername();
			String password = ASTSwitch.this.getDefinition().getPassword();
			session.write("Action: login\nUsername: " + username + "\nSecret: "
					+ password);
		}

		@Override
		public void onMessage(Object message) {
			BasicIoMessage basicMessage = (BasicIoMessage) message;
			if (basicMessage != null) {
				ASTEvent event = ASTEvent.buildEvent(ASTSwitch.this
						.getSwitchId(), basicMessage.getMessage());
				if (event != null && event.getEventType() == EventType.Response) {
					ASTResponseEvent response = (ASTResponseEvent) event;
					if (response.isSuccess()) {
						log.debug(response.getMessage());
						ASTSwitch.this.setStatus(SwitchStatus.Ready);
						messageHandler = new LoggedInState();
					}
				}
			}

		}
	}

	private class LoggedInState extends QueuedMessageHandler {

		@Override
		public void onMessage(Object message) {
			BasicIoMessage basicIoMessage = (BasicIoMessage) message;
			if (basicIoMessage != null) {

				log.debug(String.format("Switch (%s) : \n%s", ASTSwitch.this
						.getSwitchId(), basicIoMessage.getMessage()));

				ASTEvent event = ASTEvent.buildEvent(ASTSwitch.this
						.getSwitchId(), basicIoMessage.getMessage());
				if (event == null) {
					log.debug("Didn't create Event for message ...");
					return;
				}
				if (event instanceof ASTChannelEvent) {
					ASTChannelEvent channelEvent = (ASTChannelEvent) event;
					if (channelEvent.getChannelId().startsWith("Bridge/"))
						return;
				}
				switch (event.getEventType()) {
				case NewChannel: {
					ASTNewChannelEvent nce = (ASTNewChannelEvent) event;
					ASTChannel channel = new ASTChannel(ASTSwitch.this, nce
							.getChannelId(), basicIoMessage.getSession());
					ASTSwitch.this.addChannel(channel);
				}
					break;
				case PeerStatus:{
					ASTPeerStatusEvent pse = (ASTPeerStatusEvent) event;
					if (pse.getRegistered()){
						Registrar.getInstance().register(new UserLocation(event.getSwitchId(),"XXX", pse.getUser()));
					}
					else{
						Registrar.getInstance().unregister(pse.getUser());
					}									
				}
					break;
				case Dial:{
					ASTDialEvent dialEvent = (ASTDialEvent) event;
					ASTChannel peerChannel = (ASTChannel) ASTSwitch.this.getChannel(dialEvent.getDestinationChannel());
					if (peerChannel != null){
						peerChannel.processNativeEvent(event);
					}
					
				}
				break;
				}
				if (event instanceof ASTChannelEvent) {
					log.debug(event);
					ASTChannelEvent channelEvent = (ASTChannelEvent) event;
					ASTChannel channel = (ASTChannel) ASTSwitch.this
							.getChannel(channelEvent.getChannelId());
					if (channel != null) {
						channel.processNativeEvent(channelEvent);
					}
				}

			}

		}
	}

	// /////////////////////////////////////////////////////
	public ASTSwitch(SwitchDefinition definition) {
		super(definition);
		this.encodingDelimiter = "\r\n\r\n";
		this.decodingDelimiter = "\r\n\r\n";
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
		log.warn("Disconnected from switch " + getDefinition().getId());
		start();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
		messageHandler = new LoggingInState();
	}

	@Override
	public void start() {
		reconnectTimer = Timers.getInstance().startTimer(this, RECONNECT_AFTER,
				false, null);
	}

	private boolean connect() {
		log.debug(String.format("Trying to connect to %s:%d", getDefinition()
				.getAddress(), getDefinition().getPort()));
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
				Charset.forName("UTF-8"), encodingDelimiter, decodingDelimiter);
		textLineCodecFactory.setDecoderMaxLineLength(8192);
		textLineCodecFactory.setEncoderMaxLineLength(8192);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(this);

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
		}
		return false;
	}

	@Override
	public boolean onTimer(Object data) {
		if (isConnected())
			return true;// stop the timer
		return connect();
	}

	private boolean isConnected() {
		return (session != null && session.isConnected());
	}

	@Override
	public Result originate(String destination, long timeout, String callerId,
			String calledId, String script, String data) {

		String uuid = UUID.randomUUID().toString();

		String admArgs="script="+script+"&data="+data;
		
		String translatedDestination = getAddressTranslator().translate(destination);
		session.write(String
				.format(
						"Action: Originate\nChannel: %s\nTimeout: %d\nApplication: AGI\nVariable: adm_args=%s\nData: agi:async\nAsync: 1\nActionId: %s",
						translatedDestination, timeout, admArgs, uuid
								+ "___Originate"));
		return Result.Ok;
	}
}
