<#import "common.ftl" as com>
<#escape x as x?html>

<@com.page title="Hangup">
  <h1>HANGUP</h1>

<form action="webconf" method="get" enctype="application/x-www-form-urlencoded" name="hangup">
    <input name="channel" type="hidden" value="${c.uniqueId}" id="channel" />
    <input name="action" type="hidden" value="hangup" id="action" />
    <input name="Submit" type="submit" value="Hangup" />
</form>
</@com.page>

</#escape>
