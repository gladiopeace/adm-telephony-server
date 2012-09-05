<#import "common.ftl" as com>
<#escape x as x?html>
<@com.page title="Scripts">
  <#if scripts?size = 0>
    <p>No scripts.</p>
  <#else>
    <p>The messages are:
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Name
        <th bgcolor="#C0C0C0">Message
      <#list scripts as s>
        <tr align=left valign=top>
          <td bgcolor="#E0E0E0">${s.id}</td>
          <td bgcolor="#E0E0E0">
          	<ul>
          		<#list s.channels as c>
          			<li>${c.calledStationId}:${c.state}:${c.setupTime}:${c.answerTime}</li>
          		</#list>
          	</ul>
          </td>
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
