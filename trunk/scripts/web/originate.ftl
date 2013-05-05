<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Originate">
  <h1>ORIGINATE</h1>
<div style="margin-top:20px;margin-bottom:20px">
    <#if message??>
        <div>${message}</div>
    </#if>
</div>
<form action="webconf" method="get" enctype="application/x-www-form-urlencoded">
    <label> <b>Destination</b>
        <input name="destination" type="text" id="destination" />
    </label>
    <label> <b>Timeout</b>
        <input type="text" name="timeout" id="timeout"/>
    </label>
    <label><b>Script</b>
    	<input type="text" name="script" id="script"/>
    </label>
    <input name="action" type="hidden" value="originateSubmit" id="action" />
    <input name="originate" type="submit" value="Originate" id="originate" />

</form>
</@com.page>

</#escape>
