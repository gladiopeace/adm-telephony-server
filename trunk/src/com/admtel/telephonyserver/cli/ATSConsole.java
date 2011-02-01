package com.admtel.telephonyserver.cli;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.codehaus.jackson.map.ObjectMapper;

import com.admtel.telephonyserver.remote.EventDto;
import com.admtel.telephonyserver.remote.HangupEventDto;
import com.admtel.telephonyserver.remote.AlertingEventDto;
import com.admtel.telephonyserver.remote.OutboundAlertingEventDto;
import com.admtel.telephonyserver.requests.Request;

import jline.ConsoleReader;
import jline.History;
import jline.SimpleCompletor;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ATSConsole implements IoHandler{

	/**
	 * @param args
	 */
	static Map<String, CLI_Command > COMMAND_MAP = new HashMap<String, CLI_Command>();
	
	static {
		try{
			COMMAND_MAP.put(CLI_HangupCommand.command, new CLI_HangupCommand());
			COMMAND_MAP.put(CLI_ShowChannelsCommand.command, new CLI_ShowChannelsCommand());
			COMMAND_MAP.put(CLI_ReloadCommand.command, new CLI_ReloadCommand());
			COMMAND_MAP.put(CLI_ShowStatusCommand.command, new CLI_ShowStatusCommand());
			COMMAND_MAP.put(CLI_ShowSwitchCommand.command, new CLI_ShowSwitchCommand());			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	static String[] commandsToArray(){
		String[] result = new String[COMMAND_MAP.size()];
		int i =0;
		for (String str:COMMAND_MAP.keySet()){
			result[i] = str;
			i++;
		}
		
		return result;
	}
	
	IoSession session;
	private NioSocketConnector connector;
	private List<String> channels = new ArrayList<String>();

	CLI_Command getCLI_Command(String cmd){
		for (String str:COMMAND_MAP.keySet()){
			if (cmd.startsWith(str)){				
				return COMMAND_MAP.get(str);
			}
		}
		return null;
	}
	
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
			//String [] commands ={"show channels","hangup", "exit"};
			
			 OptionParser parser = new OptionParser( "i:p::?." );
			 OptionSet options = parser.parse(args);
			 String address = (String) options.valueOf("i");
			 String port = (String)options.valueOf("p");
			 
			 System.out.println("Connecting to "+ address+":"+port);
			 
			if (!connect(address,Integer.parseInt(port),10000)){
				System.out.println("Failed to connect");
				System.exit(0);
			}
			 
			SimpleCompletor sc = new SimpleCompletor(commandsToArray()); 
			
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
				else{
					CLI_Command cliCommand = getCLI_Command(cmd);
					if (cliCommand != null){
						try {
							Request request = (Request) cliCommand.parse(cmd);
							if (request != null){
								System.out.println(String.format("Generated request {%s}", request));
								ObjectMapper mapper = new ObjectMapper();
								mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
								mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
								String requestStr = mapper.writeValueAsString(request); 
								System.out.println("Sending command " + requestStr);
								session.write(requestStr);
								
							}
						} catch (Exception e) {							
							e.printStackTrace();
						} 
					}
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
		arg1.printStackTrace();
		
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		try
		{
			EventDto event = mapper.readValue(message.toString(), EventDto.class);
			System.out.println(event.toDisplayString());
			if (event instanceof AlertingEventDto){
				AlertingEventDto ia = (AlertingEventDto) event;
				channels.add(ia.getChannelId());
			}
			else if (event instanceof OutboundAlertingEventDto){
				OutboundAlertingEventDto oa = (OutboundAlertingEventDto)event;
				channels.add(oa.getChannelId());
			}
			else if (event instanceof HangupEventDto){
				HangupEventDto he = (HangupEventDto) event;
				channels.remove(he.getChannelId());
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		System.out.println("Disconnected");
		System.exit(0);
		
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
	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
		
	}

}
