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
    	 	<table>
    	 		<tr>
    	 			<td><a href="?action=index">Home</a></td>
    	 		</tr>
    	 		<tr>
    	 			<td><a href="?action=channels">Channels</a></td>
    	 		<tr>
    	 			<td><a href="?action=conferences">Conferences</a></td>
    	 		</tr>
				<tr>
    	 			<td><a href="?action=scripts">Scripts</a></td>
    	 		</tr>
    	 		<tr>
    	 			<td><a href="?action=queues">Queues</a></td>
    	 		</tr>    	 		
    	 		    	 		
    	 	</table>
    	 </td>
    	 <td>
    	     <#nested>
    	 </td>
    	</tr>
    </table>
  </body>
  </html>
</#macro>