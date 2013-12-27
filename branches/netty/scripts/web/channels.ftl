<#import "common.ftl" as com>
<#escape x as x?html>
<#import "pagination.ftl" as pagination />
<#setting url_escaping_charset='ISO-8859-1'>
<@com.page title="Channels">
  <#if channels?size = 0>
    <p>No channels.</p>
  <#else>    
  	<nav style="float:right;">
        <@pagination.first />
        <@pagination.previous />
        <@pagination.numbers />
        <@pagination.next />
        <@pagination.last />
    </nav>
    <@pagination.counter />
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
          <td bgcolor="#A0A0A0"><a href="?action=channel&id=${c.uniqueId}">${c.uniqueId}<a></td>
          <td bgcolor="#A0A0A0">${c._switch.definition.address}</td>
          <td bgcolor="#A0A0A0">${c.callingStationId?default("")}</td>
          <td bgcolor="#A0A0A0">${c.calledStationId?default("")}</td>
          <td bgcolor="#A0A0A0">${c.callOrigin}</td>
          <td bgcolor="#A0A0A0">${c.callState}</td>
          <td bgcolor="#A0A0A0">${c.mediaState}</td>
          <td bgcolor="#A0A0A0">${c.setupTime?default("")}</td>
          <td bgcolor="#A0A0A0">${c.answerTime?default("")}</td>
          <td bgcolor="#A0A0A0">${c.accountCode?default("")}</td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
