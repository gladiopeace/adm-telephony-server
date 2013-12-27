ADM Telephony Server (ATS) is a multi-thread scripting engine for running telephony applications using FreeSwitch and Asterisk.

ATS provides connects to one or more FreeSwitches or Asterisk servers and provides a unified API to interact with them.

 Features
 --------
 1- Plug-in architecture, allows the system to be extended and typical behaviour changed.
 2- Built-in radius support.
 3- Control the number of thread the system uses.
 4- Smart class loader allows it to interact with java VM based language (groovy for now)
 5- Built-in web server, used to monitor and manage the system
 6- XML based configuration
 7- Scripting engine, with configurable script loaders
 8- Dynamic prompt builders (allows the localization of prompts by writing extentions)
 9- Bean manager, used for dynamic script insertion and referencing.
 10- JSON web API.
 
 Building
 --------
 To build from the source, use ant with the included build.xml.
 
 Running
 -------
 1- Modify the included config.xml
 2- java -cp .:log4j.properties:adm-telephony-server.jar:lib/* com.admtel.telephonyserver.core.AdmTelephonyServer
 
 
 FreeSwitch Integration
 ----------------------
 1- event_socket.conf.xml, set the right credentials for ATS to login (check config.xml)
 2- enable mod_xml_curl in modules.conf.xml
 3. Modify xml_curl.conf.xml (get user directory):
		<param name="gateway-url" value="http://<ATSIP>:8057/FSConfigurator" bindings="directory"/>
 4- add to conf/dialplan/default.xml
		<extension name="esl">
		      <condition field="destination_number" expression=".*">
		         <action application="socket" data=<ATSIP>:8084 full async"/>
		      </condition>
		</extension>
 5- Disable mod_native_file in modules.conf.xml	 
 	
 
 