<#macro page title>
  <html>
  <head>
    <title>ADM TELEPHONY SERVER - ${title?html}</title>
    <meta http-equiv="Content-type" content="text/html">
  </head>
  <body>
    <h1>${title?html}</h1>
    <table>
    	<tr>
    	 <td>
    	 </td>
    	 <td>
    	     <#nested>
    	 </td>
    	</tr>
    </table>
  </body>
  </html>
</#macro>