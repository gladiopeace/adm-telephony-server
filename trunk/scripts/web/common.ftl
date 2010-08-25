<#macro page title>
  <html>
  <head>
    <title>ADM TELEPHONY SERVER - ${title?html}</title>
    <meta http-equiv="Content-type" content="text/html">
  </head>
  <body>
    <h1>${title?html}</h1>
    <hr>
    <#nested>
    <hr>
    <table border="0" cellspacing=0 cellpadding=0 width="100%">
      <tr valign="middle">
        <td align="left">
		</td>          
        <td align="right">
        </td>  

    </table>
  </body>
  </html>
</#macro>