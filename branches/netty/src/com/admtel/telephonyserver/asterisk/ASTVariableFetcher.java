/**
 * 
 */
package com.admtel.telephonyserver.asterisk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.asterisk.commands.ASTGetVariableCommand;
import com.admtel.telephonyserver.asterisk.events.ASTEvent;
import com.admtel.telephonyserver.asterisk.events.ASTResponseEvent;

/**
 * @author danny A Class that retrieves a list of variables from asterisk (in an
 *         asynchronous way) The problem with asterisk is that it doesn't report
 *         enough information about the call. Most of the information can be
 *         retrieved using a getVar call, however, this methods is asynchronous,
 *         making the state machine complicated if we want to retrieve more than
 *         one variable.
 * 
 */
public class ASTVariableFetcher {
	private Set<String> requestedVars = new HashSet<String>();
	private Map<String, String> returnedVars = new HashMap<String, String>();
	private Queue<String> vars = new LinkedList<String>();
	private Mode mode;
	private ASTChannel channel;

	// Returns true when all the variables are received

	static Logger log = Logger.getLogger(ASTVariableFetcher.class);
	
	public enum Mode {
		OneShot, Incremental
	};

	// OneShot : All the variable requests are sent immediately.
	// Incremental : Request for the next var is sent when the previous Response
	// is received.

	public ASTVariableFetcher(ASTChannel channel) {
		mode = Mode.OneShot;
		this.channel = channel;
	}

	public ASTVariableFetcher(ASTChannel channel, Mode mode) {
		this.mode = mode;
		this.channel = channel;
	}

	public boolean processASTEvent(ASTEvent event) {
		switch (event.getEventType()) {
		case Response: {
			ASTResponseEvent response = (ASTResponseEvent) event;
			if (response.getRequest().equals("GetVar")) {
				Set<Entry<String, String>> receivedVars = response.getData()
						.entrySet();
				for (Entry entry : receivedVars) {
					log.trace("Received var : " + entry.getKey() +", with value : "+ entry.getValue());
					requestedVars.remove(entry.getKey());
					returnedVars.put((String)entry.getKey(), (String)entry
							.getValue());
				}
			}
			if (mode == Mode.Incremental) {

				String var = vars.poll();
				if (var != null) {
					requestedVars.add(var);
					sendNextVar(var);
				}
			}
			if (requestedVars.size() == 0) {
				return true;
			}

		}
			break;
		}
		return false;
	}

	public void fetch(String... variables) {
		log.debug("Fetching variables : ");
		for (int i = 0; i < variables.length; i++) {
			requestedVars.add(variables[i]);
			vars.offer(variables[i]);
		}

		if (mode == Mode.OneShot) {
			String var = vars.poll();
			while (var != null) {
				sendNextVar(var);
				var = vars.poll();
			}
		} else {
			String var = vars.poll();
			if (var != null) {
				sendNextVar(var);
			}
		}

	}

	private void sendNextVar(String var) {
		log.trace("Requesting variable " + var);
		ASTGetVariableCommand cmd = new ASTGetVariableCommand(channel, var);
		channel.session.write(cmd);// TODO make a generic session object
	}

	public String getVariable(String varName) {
		return returnedVars.get(varName);
	}
}
