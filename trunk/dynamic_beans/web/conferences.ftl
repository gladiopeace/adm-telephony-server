<#import "common.ftl" as com>

<@com.page title="Conferences">
  <#if conferences?size = 0>
    <p>No Conferences.</p>
  <#else>
    <table border=0 cellspacing=2 cellpadding=2 width="100%">
      <tr align=center valign=top>
        <th bgcolor="#C0C0C0">Id</th>
        <th bgcolor="#C0C0C0">Create Time</th>
        <th bgcolor="#C0C0C0">State</th>
      <#list conferences as c>
        <tr align=left valign=top>
          <td bgcolor="#E0E0E0">${c.id}</td>
          <td bgcolor="#E0E0E0">${c.createTime}</td>
          <td bgcolor="#E0E0E0">${c.state}</td>          
        </tr>             
      </#list>
    </table>
  </#if>
</@com.page>
