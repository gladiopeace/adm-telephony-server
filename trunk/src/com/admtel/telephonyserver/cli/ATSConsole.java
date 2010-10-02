package com.admtel.telephonyserver.cli;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import jline.ConsoleReader;
import jline.History;
import jline.SimpleCompletor;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ATSConsole implements IoHandler{

	/**
	 * @param args
	 */
	IoSession session;
	private NioSocketConnector connector;
	private boolean connect(String address, int port, int connectTimeout){
		
		System.out.println(String.format("ATSConsole trying to connected to %s:%d", address, port));
		connector = new NioSocketConnector();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
				Charset.forName("UTF-8"), "\n", "\n");
		textLineCodecFactory.setDecoderMaxLineLength(8192);
		textLineCodecFactory.setEncoderMaxLineLength(8192);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(this);

		try {
			ConnectFuture connectFuture = connector
					.connect(new InetSocketAddress(address, port));
			connectFuture.awaitUninterruptibly(connectTimeout);
			session = connectFuture.getSession();
			System.out.println(String.format("ATSConsole connected to %s:%d", address, port));
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public ATSConsole(String[] args){
		try {
			String [] commands ={"show channel", "show jobs","exit"};
			
			 OptionParser parser = new OptionParser( "i:p::?." );
			 OptionSet options = parser.parse(args);
			 String address = (String) options.valueOf("i");
			 String port = (String)options.valueOf("p");
			 
			 System.out.println("Connecting to "+ address+":"+port);
			 
			if (!connect(address,Integer.parseInt(port),10000)){
				System.out.println("Failed to connect");
				System.exit(0);
			}
			 
			SimpleCompletor sc = new SimpleCompletor(commands); 
			
			ConsoleReader reader = new ConsoleReader();
			reader.addCompletor(sc);
			History history = new History();
			reader.setHistory(history);
			reader.setUseHistory(true);
			
			while (true){
				String cmd = reader.readLine("ATS >");
				
				history.addToHistory(cmd);
				if (cmd.equals("exit")){
					System.exit(0);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		new ATSConsole(args);
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		
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

}
