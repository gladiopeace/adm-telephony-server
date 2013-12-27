<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Channels">
  <#if queues?size = 0>
    <p>No queues.</p>
  <#else>    
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Id</th>  
        <th bgcolor="#C0C0C0">Priority</th>
        <th bgcolor="#C0C0C0">Current Sessions</th>
      <#list queues as c>
        <tr align=left valign=top>          
          <td bgcolor="#A0A0A0">${c.id}</td>
          <td bgcolor="#A0A0A0">${c.priority}</td>
          <td bgcolor="#A0A0A0">${c.currentSessions}</td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
