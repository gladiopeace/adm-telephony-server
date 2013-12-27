<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Users">
  <#if users?size = 0>
    <p>No Users.</p>
  <#else>    
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Username</th>        
        <th bgcolor="#C0C0C0">SwitchId</th>      
        <th bgcolor="#C0C0C0">Protocol</th>  
      <#list users as u>
        <tr align=left valign=top>          
          <td bgcolor="#A0A0A0">${u.username}</td>
          <td bgcolor="#A0A0A0">${u.switchId?default("")}</td>
          <td bgcolor="#A0A0A0">${u.protocol?default("")}</td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
