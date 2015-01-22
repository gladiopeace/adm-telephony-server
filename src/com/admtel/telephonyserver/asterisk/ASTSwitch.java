package com.admtel.telephonyserver.asterisk;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.admtel.telephonyserver.asterisk.events.ASTHangupEvent;
import com.admtel.telephonyserver.asterisk.events.ASTNewChannelEvent;
import com.admtel.telephonyserver.asterisk.events.ASTPeerStatusEvent;
import com.admtel.telephonyserver.asterisk.events.ASTResponseEvent;
import com.admtel.telephonyserver.asterisk.events.ASTEvent.EventType;
import com.admtel.telephonyserver.asterisk.events.CoreShowChannelEvent;
import com.admtel.telephonyserver.asterisk.events.CoreShowChannelsCompleteEvent;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.SwitchDefinition;
import com.admtel.telephonyserver.core.AdmAddress;
import com.admtel.telephonyserver.core.BasicIoMessage;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.core.NettySession;
import com.admtel.telephonyserver.core.QueuedMessageHandler;
import com.admtel.telephonyserver.core.Result;
import com.admtel.telephonyserver.core.Session;
import com.admtel.telephonyserver.core.SimpleMessageHandler;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.Timers;
import com.admtel.telephonyserver.core.Switch.SwitchStatus;
import com.admtel.telephonyserver.core.Timers.Timer;
import com.admtel.telephonyserver.interfaces.TimerNotifiable;
import com.admtel.telephonyserver.misc.VariableMap;
import com.admtel.telephonyserver.registrar.UserLocation;
import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.events.DisconnectCode;
import com.admtel.telephonyserver.events.RegisteredEvent;
import com.admtel.telephonyserver.events.UnregisteredEvent;

public class ASTSwitch extends Switch implements TimerNotifiable {

	// /////////////////////////////////////////////////////
	// State classes
	//
	static Bootstrap b = new Bootstrap();
	static EventLoopGroup group = new NioEventLoopGroup();
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	public static byte[] DELIMITERS = {13,10,13,10};
	public static ByteBuf CHANNEL_DELIMITER = Unpooled.copiedBuffer(DELIMITERS);
	
	Session session;
	
	ChannelHandlerContext context;	
	 class Handler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			session = new NettySession(ctx);
			context = ctx;
			log.trace("Sending login");
			String username = ASTSwitch.this.getDefinition().getUsername();
			String password = ASTSwitch.this.getDefinition().getPassword();
			session.write("Action: login\nUsername: " + username + "\nSecret: " + password);
			state = State.LoggingIn;

		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			messageHandler.putMessage(new BasicIoMessage(new NettySession(context), (String) msg));
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			log.warn("Disconnected from switch " + getDefinition().getId());
			state = State.Disconnected;
			start();

			super.channelInactive(ctx);
		}
		
	}

	private static Logger log = Logger.getLogger(ASTSwitch.class);

	private static final int RECONNECT_AFTER = 5000;
	private static final long WATCHDOG_TIMER = 30000L;


	private Timer reconnectTimer = null;
	private Watchdog watchdog = new Watchdog();

	private enum State {
		Connecting, LoggingIn, LoggedIn, Disconnecting, Disconnected,
	};

	private State state = State.Disconnected;

	class Watchdog implements TimerNotifiable {

		Set<String> channels = new HashSet<String>();

		private void start() {
			channels.clear();
			Timers.getInstance().startTimer(this, WATCHDOG_TIMER, true, null);
		}

		public Watchdog() {
			start();
		}

		@Override
		public boolean onTimer(Object data) {
			log.trace(String.format("Watchdog timer fired for switch : %s", ASTSwitch.this.getId()));
			if (state == State.LoggedIn) {
				// Get list of channels from switch
				context.write("Action: CoreShowChannels");
			}
			else {
				start();
			}
			return true;
		}

		public void processEvent(CoreShowChannelEvent csce) {
			channels.add(csce.getChannelId());
		}

		public void processEvent(CoreShowChannelsCompleteEvent cscce) {
			List<Channel> switchChannels = new ArrayList<Channel>(ASTSwitch.this.getAllChannels());
			if (cscce.listItems == channels.size()) { // We have received the
														// right number of
														// channels
				// Schedule channels that don't exist to auto hangup
				for (Channel channel : switchChannels) {
					if (!channels.contains(channel.getId())) {
						// Schedule for remove
						log.warn(String.format("Channel %s not found in the switch, sending a fake hangup message ",
								channel.getId()));
						channel.putMessage(ASTHangupEvent.build(channel.get_switch().getSwitchId(), channel.getId(),
								DisconnectCode.Preemption));
					}
				}
			}

			start();
		}
	}

	// /////////////////////////////////////////////////////
	public ASTSwitch(SwitchDefinition definition) {
		super(definition);
		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, CHANNEL_DELIMITER));
				ch.pipeline().addLast(DECODER);
				ch.pipeline().addLast(ENCODER);

				ch.pipeline().addLast("handler", new Handler());
			}
		});
	}

	@Override
	public void start() {
		super.start();
		reconnectTimer = Timers.getInstance().startTimer(this, RECONNECT_AFTER, false, null);
	}

	private boolean connect() {
		log.debug(String.format("Trying to connect to %s:%d", getDefinition().getAddress(), getDefinition().getPort()));

		state = State.Connecting;
		try {
			ChannelFuture cf = b.connect(getDefinition().getAddress(), getDefinition().getPort()).sync();
						
			log.debug(String.format("Connected to %s:%d", getDefinition().getAddress(), getDefinition().getPort()));
			return true;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			state = State.Disconnected;
		}
		return false;
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
		return (context != null && !context.isRemoved());
	}

	@Override
	public Result originate(String destination, long timeout, String callerId, String calledId, String script,
			VariableMap data) {

		AdmAddress admAddress = AdmAddress.fromString(destination);
		if (admAddress == null) {
			return Result.InvalidAddress;
		}
		String uuid = UUID.randomUUID().toString();

		String dialStr = getAddressTranslator().translate(admAddress);
		data.addVariable("script", script);
		String variables = null;
		if (data != null && data.size() > 0) {
			variables = data.getDelimitedVars("=", "|");
		}
		if (variables != null && variables.length() > 0) {
			session.write(String
					.format("Action: Originate\nChannel: %s\nTimeout: %d\nContext: default\nApplication: AGI\nVariable: %s\nData: agi:async\nAsync: 1\nActionId: %s",
							dialStr, timeout, variables, uuid + "___Originate"));
		}
		return Result.Ok;
	}

	@Override
	public void processBasicIoMessage(BasicIoMessage message) {

		log.trace(String.format("Switch (%s) : \n%s", ASTSwitch.this.getSwitchId(), message.getMessage()));

		switch (state) {
		case LoggingIn: {
			if (message != null) {
				ASTEvent event = ASTEvent.buildEvent(ASTSwitch.this.getSwitchId(), message.getMessage());
				if (event != null && event.getEventType() == EventType.Response) {
					ASTResponseEvent response = (ASTResponseEvent) event;
					if (response.isSuccess()) {
						log.debug(response.getMessage());
						ASTSwitch.this.setStatus(SwitchStatus.Started);
						state = State.LoggedIn;
						setStatus(SwitchStatus.Started);
					}
				}
			}
		}
			break;
		case LoggedIn: {
			if (message != null) {
				ASTEvent event = ASTEvent.buildEvent(ASTSwitch.this.getSwitchId(), message.getMessage());
				if (event == null) {
					// log.debug(String.format("Switch (%s) : Didn't create an AST message"));
					return;
				}
				if (event instanceof ASTChannelEvent) {
					ASTChannelEvent channelEvent = (ASTChannelEvent) event;
					if (channelEvent.getChannelId().startsWith("Bridge/")
							|| channelEvent.getChannelId().contains("pseudo"))
						return;
				}
				switch (event.getEventType()) {
				case NewChannel: {
					if (!isAcceptingCalls()) {
						log.warn(String.format("Switch %s not accepting calls", this.getId()));
						return;
					}
					ASTNewChannelEvent nce = (ASTNewChannelEvent) event;
					ASTChannel channel = new ASTChannel(ASTSwitch.this, nce.getChannelId(), message.getSession());
					ASTSwitch.this.addChannel(channel);
				}
					break;
				case PeerStatus: {
					ASTPeerStatusEvent pse = (ASTPeerStatusEvent) event;
					if (pse.getRegistered()) {
						EventsManager.getInstance().onEvent(
								new RegisteredEvent(pse.getSwitchId(), pse.getProtocol(), pse.getUser()));
					} else {
						EventsManager.getInstance().onEvent(new UnregisteredEvent(pse.getUser()));
					}
				}
					break;
				case CoreShowChannel:
					watchdog.processEvent((CoreShowChannelEvent) event);
					return;
				case CoreShowChannelsComplete:
					watchdog.processEvent((CoreShowChannelsCompleteEvent) event);
					return;
				}

				if (event instanceof ASTChannelEvent) {
					ASTChannelEvent channelEvent = (ASTChannelEvent) event;
					ASTChannel channel = (ASTChannel) ASTSwitch.this.getChannel(channelEvent.getChannelId());
					if (channel != null) {
						channel.putMessage(channelEvent);
					}
				}

			}
		}
			break;
		}
	}

}
