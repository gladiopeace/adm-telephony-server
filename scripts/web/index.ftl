<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Main">
  <a href="${context}?action=hangup">Hangup Channel</a>
  
  <#if channels?size = 0>
    <p>No channels.</p>
  <#else>
    <p>The messages are:
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Name
        <th bgcolor="#C0C0C0">Message
      <#list channels as c>
        <tr align=left valign=top>
          <td bgcolor="#E0E0E0">${c.id}</td>
          <td bgcolor="#E0E0E0"><a href="${context}?action=hangup&switch=${c.switch.definition.id}&channel=${c.id}"/> Hangup</td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
