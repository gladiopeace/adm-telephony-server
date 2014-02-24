<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Script">
	 <#list script.channels as c>
	 ${c.detailedDump}
	 <br/>
	 -------------------------------------------------------------------------------------------------------------------
	 <br/>
	 </#list>
</@com.page>

</#escape>
