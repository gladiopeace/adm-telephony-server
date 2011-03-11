<#import "common.ftl" as com>

<#escape x as x?html>

  <#if result = 0>
  
    <p>Invalid Username or Password</p>
    
    <a href="?action=login">Try Again</a>
    
  <#else>
  
<@com.page title="Main">	

</@com.page>

  </#if>
  
</#escape>