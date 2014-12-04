package com.admtel.telephonyserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.admtel.telephonyserver.config.SwitchListenerDefinition;
import com.admtel.telephonyserver.utils.AdmUtils;

public abstract class SwitchListener implements IoHandler{
	
	static Logger log = Logger.getLogger(SwitchListener.class);
	
	SwitchListenerDefinition definition;
	protected SocketAcceptor acceptor;
	protected String encodingDelimiter;
	protected String decodingDelimiter;

	public enum Status{Idle, Listening};
	
	Status status;
	//
	/////////////////////////////////////////////////////////
	private class ConnectionOpenedHandler implements Runnable{
		IoSession session;
		ConnectionOpenedHandler(IoSession session){
			this.session = session;
		}

		@Override
		public void run() {
			Switch _switch = SwitchListener.this.getSwitch(session);
			if (_switch == null){
				session.close(true);
			}
			else{
				session.setAttribute("Switch", _switch);
			}
			afterSessionOpened (session);
		}
	}
	
	public SwitchListener (SwitchListenerDefinition definition){
		this.definition = definition;
		status = Status.Idle;		
	}
	public Status getStatus(){
		return status;
	}
	public boolean start(){
		
		acceptor = new NioSocketAcceptor();
		acceptor.setReuseAddress(true);
		//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset
				.forName("UTF-8"), encodingDelimiter, decodingDelimiter);
		textLineCodecFactory.setDecoderMaxLineLength(8192);
		textLineCodecFactory.setEncoderMaxLineLength(8192);

		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(textLineCodecFactory));
		
	
		acceptor.setHandler(this);
	
		try {
			acceptor.bind(new InetSocketAddress(definition.getAddress(), definition.getPort()));
		} catch (IOException e) {
			log.fatal(e.getMessage());
			return false;
		}
		status = Status.Listening;
		return true;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable exception)
			throws Exception {
		// TODO Auto-generated method stub
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub		
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		AdmThreadExecutor.getInstance().execute(new ConnectionOpenedHandler(session));
		
	}
	protected Switch getSwitch(IoSession session) {
		InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
		String ip = address.getAddress().toString();
		if (ip.startsWith("/")){
			ip = ip.substring(1);
		}
		Switch _switch = Switches.getInstance().getByAddress(ip);
		if (_switch != null){
			log.debug(String.format("Authorizing switch @ %s, got %s", ip, _switch.getDefinition().getId()));
		}
		else{
			log.debug(String.format("Connection from %s not authorized", ip));
		}
		return _switch;
	}
	public SwitchListenerDefinition getDefinition(){
		return definition;
	}
	public Map<Long,IoSession> getSessions(){
		return acceptor.getManagedSessions();
	}
	abstract public void afterSessionOpened(IoSession session);
}
