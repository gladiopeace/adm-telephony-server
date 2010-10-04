package com.admtel.telephonyserver.remote;

import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.admtel.telephonyserver.core.AdmTelephonyServer;
import com.admtel.telephonyserver.core.EventsManager;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.interfaces.EventListener;
import com.admtel.telephonyserver.requests.Request;


public class JSonSocketManagerBean implements IoHandler, EventListener {

	static Logger log = Logger.getLogger(JSonSocketManagerBean.class);
	public String address = "localhost";
	public int port = 12341;
	protected SocketAcceptor acceptor;

	public void init() {
		EventsManager.getInstance().addEventListener("JSonSocketManagerBean",
				this);
		acceptor = new NioSocketAcceptor();
		acceptor.setReuseAddress(true);
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
				Charset.forName("UTF-8"), "\n", "\n");
		textLineCodecFactory.setDecoderMaxLineLength(8192);
		textLineCodecFactory.setEncoderMaxLineLength(8192);

		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(textLineCodecFactory));

		acceptor.setHandler(this);

		try {
			acceptor.bind(new InetSocketAddress(address, port));
			log.trace(String.format("JSonSocketManagerBean, started on %s:%d",
					address, port));
		} catch (IOException e) {
			log.fatal(e.getMessage(), e);
		}

	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		log.error(arg1.getMessage(), arg1);

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		String messageStr = message.toString();
		
		log.trace("Received Message "+messageStr);
		try{
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE				
		Request request = mapper.readValue(messageStr, Request.class);
		log.trace("Request is "+request);
		if (request != null){
			log.trace(String.format("Received {%s} from {%s} - RequestDto = {%s}", message, session.getRemoteAddress(), request));
			AdmTelephonyServer.getInstance().processRequest(request);
		}
		}
		catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEvent(Event event) {
		Collection<IoSession> sessions = this.acceptor.getManagedSessions()
				.values();
		try{
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE		
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		EventDto eventDto = EventDto.buildEventDto(event);
		if (eventDto != null){
			for (IoSession s:sessions){
				try {
					s.write(mapper.writeValueAsString(eventDto));			
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}	
		}
		}
		catch (Exception e){
			log.fatal(e.getMessage(), e);
		}
		return false;
	}
}
