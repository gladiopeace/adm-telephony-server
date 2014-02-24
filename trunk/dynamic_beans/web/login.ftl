<#escape x as x?html>
	<h1>Login</h1>
		<form action="webconf" method="get" enctype="application/x-www-form-urlencoded">
    	 	<table>
    	 		<tr>
    	 			<td>UserName:&nbsp;&nbsp;&nbsp;&nbsp;</td>
    	 			<td><input name="username" type="text" id="username" size="30px"></td>
    	 		</tr>
    	 		<tr><td><br /></td></tr>
    	 		<tr>
    	 			<td>Password: &nbsp;&nbsp;&nbsp;&nbsp;</td>
    	 			<td><input name="password" type="password" id="password" size="30px"></td>
    	 		<tr>
    	 		<tr><td><br /></td></tr>
    	 		<tr>
    	 			<td>
    	 					<input name="login" type="submit" value="Login" id="login" />
    	 					<input name="action" type="hidden" value="login" id="action" />
    	 			</td>
    	 		</tr>
    	 	</table>
    	 </form>
</#escape>