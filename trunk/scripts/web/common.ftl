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
    	 	<a href="?action=index">Home</a>
    	 	<a href="?action=channels">Channels</a>
    	 </td>
    	 <td>
    	     <#nested>
    	 </td>
    	</tr>
    </table>
  </body>
  </html>
</#macro>