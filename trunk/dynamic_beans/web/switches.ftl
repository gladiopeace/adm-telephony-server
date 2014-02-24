<#import "common.ftl" as com>
<@com.page title="Switches">
  <#if switches?size = 0>
    <p>No switches.</p>
  <#else>    
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Name</th>
        <th bgcolor="#C0C0C0">Address</th>
        <th bgcolor="#C0C0C0">Status</th>
        <th bgcolor="#C0C0C0">Channels</th>
        <th bgcolor="#C0C0C0">Actions</th>
      <#list switches as s>
        <tr align=left valign=top>
          <td bgcolor="#E0E0E0">${s.name}</td>
          <td bgcolor="#E0E0E0">${s.address}</td>
          <td bgcolor="#E0E0E0">${s.status}</td>
          <td bgcolor="#E0E0E0">${s.numberOfChannels}</td>
          <td bgcolor="#E0E0E0"><a href="?action=switchStop&id=${s.id}">Stop<a>|<a href="?action=switchStart&id=${s.id}">Start|<a href="?action=hangupAllChannels&id=${s.id}">Hangup All Channels</td>
         </tr>          
      </#list>
    </table>
  </#if>
</@com.page>

