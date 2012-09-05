<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Channels">
  <#if channels?size = 0>
    <p>No channels.</p>
  <#else>    
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Id</th>  
        <th bgcolor="#C0C0C0">Switch</th>       
        <th bgcolor="#C0C0C0">Calling Station Id</th>      
        <th bgcolor="#C0C0C0">Called Station Id</th>  
        <th bgcolor="#C0C0C0">Direction</th>
        <th bgcolor="#C0C0C0">Call State</th>
        <th bgcolor="#C0C0C0">Media State</th>
        <th bgcolor="#C0C0C0">Setup Time</th>
        <th bgcolor="#C0C0C0">Answer Time</th>
        <th bgcolor="#C0C0C0">Account</th>
      <#list channels as c>
        <tr align=left valign=top>          
          <td bgcolor="#A0A0A0">${c.uniqueId}</td>
          <td bgcolor="#A0A0A0">${c._switch.definition.address}</td>
          <td bgcolor="#A0A0A0">${c.callingStationId?default("")}</td>
          <td bgcolor="#A0A0A0">${c.calledStationId?default("")}</td>
          <td bgcolor="#A0A0A0">${c.callOrigin}</td>
          <td bgcolor="#A0A0A0">${c.callState}</td>
          <td bgcolor="#A0A0A0">${c.mediaState}</td>
          <td bgcolor="#A0A0A0">${c.setupTime?default("")}</td>
          <td bgcolor="#A0A0A0">${c.answerTime?default("")}</td>
           <td bgcolor="#A0A0A0">${c.getChannelData()['UserName']?default("")}</td>
          <td bgcolor="#E0E0E0"><form action="webconf" method="get" enctype="application/x-www-form-urlencoded"><input name="destination" type="text" id="destination" />
            <label>
              <input type="text" name="timeout" id="timeout">
            </label>
            <input name="dial" type="submit" value="Dial" id="dial" /><input name="action" type="hidden" value="dial" id="action" /><input name="channel" type="hidden" value="${c.uniqueId}" id="channel" /></form><form action="webconf" method="get" enctype="application/x-www-form-urlencoded" name="hangup"><input name="channel" type="hidden" value="${c.uniqueId}" id="channel" /><input name="action" type="hidden" value="hangup" id="action" /><input name="Submit" type="submit" value="Hangup" /></form>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
