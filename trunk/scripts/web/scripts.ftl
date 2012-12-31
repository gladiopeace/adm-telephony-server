<#import "common.ftl" as com>
<#escape x as x?html>
<@com.page title="Scripts">
  <#if scripts?size = 0>
    <p>No scripts.</p>
  <#else>
    <p>The messages are:
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Id
        <th bgcolor="#C0C0C0">Dump
      <#list scripts as s>
        <tr align=left valign=top>
          <td bgcolor="#E0E0E0"><a href="?action=script&id=${s.id}">${s.id}<a></td>
          <td bgcolor="#E0E0E0">${s.dump()}</td>
         </tr>          
      </#list>
    </table>
  </#if>
</@com.page>

</#escape>
