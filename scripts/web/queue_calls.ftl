<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Queue Calls">
  <#if queue_calls?size = 0>
    <p>No Queue Calls.</p>
  <#else>    
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Id</th>        
        <th bgcolor="#C0C0C0">Agent Id</th>      
        <th bgcolor="#C0C0C0">Queue Id</th>  
        <th bgcolor="#C0C0C0">Setup Time</th>
        <th bgcolor="#C0C0C0">Priority</th>
      <#list queue_calls as c>
        <tr align=left valign=top>          
          <td bgcolor="#A0A0A0">${c.channelId}</td>
          <td bgcolor="#A0A0A0">${c.agentId?default("")}</td>
          <td bgcolor="#A0A0A0">${c.queueId}</td>
          <td bgcolor="#A0A0A0">${c.setupTime?datetime}</td>
          <td bgcolor="#A0A0A0">${c.priority}</td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
