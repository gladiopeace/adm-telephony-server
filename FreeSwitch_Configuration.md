This page describes the FreeSwitch related configuration.

  * Download and Install FreeSwitch (www.freeswitch.org), currently supporting versions 1.0.6 and 1.0.7.
  * Modify event\_socket.conf.xml and set the proper ATS login parameters.
  * Enable mod\_xml\_curl in modules.conf.xml (retrieve the confuguration from xml\_curl server).
  * Modify xml\_curl.conf.xml:
```
To get the user directory add:
<param name="gateway-url" 	value="http://<ATSIP>:8057/FSConfigurator" bindings="directory"/> 
```
  * Modify dialplan/default.xml
```
<extension name="esl">
      <condition field="destination_number" expression=".*">
         <action application="socket" data=<ATSIP>:8084 full async"/>
      </condition>
</extension>
```