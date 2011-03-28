package com.admtel.telephonyserver.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.admtel.telephonyserver.config.AdmServletDefinition;
import com.admtel.telephonyserver.config.HttpServerDefinition;
import com.admtel.telephonyserver.core.AdmThreadExecutor;
import com.admtel.telephonyserver.core.SmartClassLoader;
import com.admtel.telephonyserver.core.SwitchListener.Status;
import com.admtel.telephonyserver.utils.AdmUtils;

public class HttpServer implements IoHandler {

	enum Status {
		Running, Stopped
	};

	static AdmServlet admServlet = new DefaultAdmServlet();
	
	Status status;
	private NioSocketAcceptor acceptor;
	private HttpServerDefinition definition;

	static Logger log = Logger.getLogger(HttpServer.class);
	
	public HttpServer(HttpServerDefinition definition) {
		this.definition = definition;

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable arg1)
			throws Exception {
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// Check that we can service the request context
		// response.appendBody("<html><body>");

		log.trace(message);
		HttpRequestMessage request = (HttpRequestMessage) message;

		HttpResponseMessage response = new HttpResponseMessage();
		response.setContentType("text/html");
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);


		try {
			AdmServletDefinition servletDefinition = definition.getServletDefinition(request.getContext());
			AdmServlet servlet = null;
			if (servletDefinition != null){
				servlet = SmartClassLoader.createInstance(AdmServlet.class, servletDefinition.getClassName());
				log.trace(servlet);
				servlet.setParameters(servletDefinition.getParameters());
			}
			
			if (servlet == null){
				servlet = admServlet;
			}
			servlet.process(request, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		session.write(response);
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
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		session.close(true);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		// set idle time to 60 seconds

	}

	public Status getStatus() {
		return status;
	}

	public boolean start(Executor executor) {
		acceptor = new NioSocketAcceptor();
		acceptor.setReuseAddress(true);

		ExecutorFilter executorFilter = new ExecutorFilter(executor);
		acceptor.getFilterChain().addFirst("executor", executorFilter);
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new HttpServerProtocolCodecFactory()));

		acceptor.setHandler(this);

		try {
			acceptor.bind(new InetSocketAddress(definition.getAddress(),
					definition.getPort()));
		} catch (IOException e) {
			log.fatal(AdmUtils.getStackTrace(e));
			return false;
		}
		status = Status.Running;
		return true;
	}

	public String getId() {
		return definition.getId();
	}
}
