package com.admtel.telephonyserver.cli;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.admtel.telephonyserver.config.CLI_ListenerDefinition;
import com.admtel.telephonyserver.core.AdmThreadExecutor;
import com.admtel.telephonyserver.core.Channel;
import com.admtel.telephonyserver.core.Conference;
import com.admtel.telephonyserver.core.Conferences;
import com.admtel.telephonyserver.core.Registrar;
import com.admtel.telephonyserver.core.Script;
import com.admtel.telephonyserver.core.Scripts;
import com.admtel.telephonyserver.core.Switch;
import com.admtel.telephonyserver.core.SwitchListener;
import com.admtel.telephonyserver.core.SwitchListeners;
import com.admtel.telephonyserver.core.Switches;
import com.admtel.telephonyserver.registrar.UserLocation;

public class CLI_Connection extends IoHandlerAdapter {
	static Logger log = Logger.getLogger(CLI_Connection.class);

	CLI_ListenerDefinition definition;
	private SocketAcceptor acceptor;

	enum Status {
		AskLogin, AskPassword, LoggedIn
	};

	public CLI_Connection(CLI_ListenerDefinition definition) {
		this.definition = definition;
	}

	public void start() throws IOException {
		acceptor = new NioSocketAcceptor();
		acceptor.setReuseAddress(true);
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"), LineDelimiter.NUL,
						LineDelimiter.DEFAULT)));

		acceptor.setHandler(this);

		acceptor.bind(new InetSocketAddress(definition.getAddress(), definition
				.getPort()));
	}

	public void stop() {
		acceptor.dispose();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.warn(cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Status status = (Status) session.getAttribute("status");
		log.debug(message);
		if (status == null) {
			session.close(true);
			return;
		}
		switch (status) {
		case AskLogin:
			session.write("password > ");
			status = Status.AskPassword;
			session.setAttribute("status", status);
			session.setAttribute("username", message);
			break;
		case AskPassword: {
			String username = (String) session.getAttribute("username");
			String password = (String) message;
			if (username.equals(definition.getUsername())
					&& password.equals(definition.getPassword())) {
				status = Status.LoggedIn;
				session.setAttribute("status", status);
				session.write("Welcome to AdmTelephonyServer\n\n");
				session.write("> ");
			} else {
				session.write("Invalid login credentials\n");
				status = Status.AskLogin;
				session.setAttribute("status", status);
				session.write("Login > ");
			}
		}
			break;
		case LoggedIn:
			if (message.equals("exit")) {
				session.close(true);
			} else if (message.equals("help")) {
				session.write("show channels\t\tshow status\t\texit\n\n");
			} else if (message.equals("show status")) {
				showStatus(session);
			} else if (message.equals("show channels")) {
				showChannels(session);
			} else if (message.equals("show scripts")) {
				showScripts(session);
			}else if (message.equals("show conferences")) {
				showConferences(session);
			} else if (((String)message).startsWith("originate")){
				originateCall(session, (String)message);
			}else if (((String)message).equals("show users")){
				showUsers(session);
			}else{			
				session.write("Invalid command\n");
			}
			session.write("> ");
			break;
		}
	}

	private void showUsers(IoSession session) {
		// TODO Auto-generated method stub
		session.write("Users\n");
		Collection<UserLocation> users = Registrar.getInstance().get(0, -1);
		for (UserLocation userLocation:users){
			session.write(String.format("\t%s\n", userLocation));
		}
	}

	private void showConferences(IoSession session) {
		Collection<Conference> conferences = Conferences.getInstance().getAll();
		Iterator<Conference> it = conferences.iterator();
		session.write("Conferences\n");
		while (it.hasNext()){
			Conference c = it.next();
			session.write(String.format("%s\n", c.dump()));
		}
		
	}

	private void originateCall(IoSession session, String message) {
		
		//TODO refactor
		String args[] = message.split(" ");
		if (args.length<4){
			session.write("\nInvalide originate format : originate <switchId> <destination> <script> [timeout]ms\n");
			return;
		}
		if (args.length==4){
			String switchId=args[1];
			String destination=args[2];
			String script=args[3];
			Switch _switch = Switches.getInstance().getById(switchId);
			if (_switch != null){
				_switch.originate(destination, 10000, "", "", script, "");
			}
		}
		else if (args.length==5){
			
		}
		return;	
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write("Login > ");
		session.setAttribute("status", Status.AskLogin);
	}

	//
	public void showStatus(IoSession session) throws Exception {

		session.write("Status\n");
		session
				.write("\t" + AdmThreadExecutor.getInstance().getStatus()
						+ "\n");
		Iterator<SwitchListener> switchListenerIterator = SwitchListeners
				.getInstance().getAll().iterator();

		session.write("Listeners\n");
		while (switchListenerIterator.hasNext()) {
			SwitchListener switchListener = switchListenerIterator.next();
			session.write(String.format("\t%s\t%s\n", switchListener
					.getDefinition().getId(), switchListener.getStatus()));
		}
		Iterator<Switch> switchIterator = Switches.getInstance().getAll()
				.iterator();
		session.write("Switches\n");
		while (switchIterator.hasNext()) {
			Switch _switch = switchIterator.next();
			session.write(String.format("\t%s\t%s\t%s\n", _switch
					.getDefinition().getId(), _switch.getDefinition()
					.getAddress(), _switch.getStatus()));
		}

	}

	private void showChannels(IoSession session) {
		Iterator<Switch> switchIterator = Switches.getInstance().getAll()
				.iterator();
		session.write("Channels\n");
		while (switchIterator.hasNext()) {
			Switch _switch = switchIterator.next();
			Iterator<Channel> channelIt = _switch.getAllChannels().iterator();
			while (channelIt.hasNext()) {
				Channel channel = channelIt.next();
				session.write(String.format("\t%s\t%s\t%s\n", _switch
						.getDefinition().getId(), channel.getId(), channel
						.getState()));
			}
		}

	}

	private void showScripts(IoSession session) {
		Iterator<Script> scriptIterator = Scripts.getInstance().getAll()
				.iterator();
		session.write("Scripts\n");
		while (scriptIterator.hasNext()) {
			Script script = scriptIterator.next();
			session.write(String
					.format("\t%s\t%s\t%s\n",
							script.getClass().getSimpleName(), script
									.getState(), script.getDisplayStr()));
			Iterator<Channel> channelIt = script.getChannels().iterator();
			while (channelIt.hasNext()) {
				Channel c = channelIt.next();
				session.write(String.format("\t\t%s\t%s\t%s\n", c.getSwitch()
						.getDefinition().getId(), c.getId(), c.getState()));
			}
		}
	}
}
