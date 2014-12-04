package com.admtel.telephonyserver.freeswitch.commands;

import com.admtel.telephonyserver.freeswitch.FSChannel;


/*Agent/Caller Example

This scenario has two extensions: 7010 will be for agents who will hear music till someone calls 7011 will be the customer who will hear hold music until an agent is free.

  <extension name="Agent_Wait">
      <condition field="destination_number" expression="^7010$">
        <action application="set" data="fifo_music=$${hold_music}"/>
        <action application="answer"/>
        <action application="fifo" data="myq out wait"/>
      </condition>
    </extension>
    <extension name="Queue_Call_In">
      <condition field="destination_number" expression="^7011$">
        <action application="set" data="fifo_music=$${hold_music}"/>
        <action application="answer"/>
        <action application="fifo" data="myq in"/>
      </condition>
    </extension>
The agents can dial in to extension 7010 and wait. Callers can be routed/transferred to extension 7011 where they'll be queued until an agent is available.
*/

public class FSQueueCommand extends FSCommand {

	private String queueName;
	private Boolean isAgent;

	public FSQueueCommand(FSChannel channel, String queueName, Boolean isAgent) {
		super(channel);
		this.queueName = queueName;
		this.isAgent = isAgent;
	}
	public String toString(){
		return String
		.format(
				"SendMsg %s\ncall-command: %s\nexecute-app-name: %s\nexecute-app-arg: %s %s\n",
				channel.getId(), "execute", "fifo", queueName,(isAgent?"out wait":"in")); //TODO more parameters
	}

}
