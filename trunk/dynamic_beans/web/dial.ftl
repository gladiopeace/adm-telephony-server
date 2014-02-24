<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Hagup">
  <h1>DIAL</h1>
<div style="margin-top:20px;margin-bottom:20px">
    <#if dialInstance??>
        <div>${dialInstance}</div>
    </#if>
</div>
<form action="webconf" method="get" enctype="application/x-www-form-urlencoded">
    <label> <b>Destination</b>
        <input name="destination" type="text" id="destination" />
    </label>
    <label> <b>Timeout</b>
        <input type="text" name="timeout" id="timeout">
    </label>
    <input name="action" type="hidden" value="dialSubmit" id="action" />
    <input name="dial" type="submit" value="Dial" id="dial" />

</form>
</@com.page>

</#escape>
