<#macro page title>
  <html>
  <head>
    <title>ADM TELEPHONY SERVER - ${title?html}</title>
    <meta http-equiv="Content-type" content="text/html">
    
    <style>
 * {
	margin: 0;
	padding: 0;
}

body {
	font-family: "Trebuchet MS", Helvetica, Sans-Serif;
	font-size: 14px;
}

a {
	text-decoration: none;
	color: #838383;
}

a:hover {
	color: black;
}

#menu {
	position: relative;
	margin-left: 30px;
}

#menu a {
	display: block;
	width: 140px;
}

#menu ul {
	list-style-type: none;
	padding-top: 5px;
}

#menu li {
	float: left;
	position: relative;
	padding: 3px 0;
	text-align: center;
}

#menu ul.sub-menu {
	display: none;
	position: absolute;
	top: 20px;
	left: -10px;
	padding: 10px;
	z-index: 90;
}

#menu ul.sub-menu li {
	text-align: left;
}

#menu li:hover ul.sub-menu {
	display: block;
	border: 1px solid #ececec;
}
#header {margin: 0 0 25px;padding: 0 0 8px}

	#header #site-name {font: 265% arial;letter-spacing: -.05em;margin:0 0 0 40px;padding:3px 0;color:#ccc;border:none}
	
#wrap {min-width:770px;max-width:1200px;margin: 0 auto;position:relative}
#content-wrap {position:relative;width:100%}
	#utility {position:absolute;top:0;left:25px;width:165px;border-top: 5px solid #999;padding-bottom: 40px}
	#sidebar {position:absolute;top:0;right:25px;width:20%;border-top: 5px solid #999;padding-top: 1px;padding-bottom: 40px}

#content {margin: 0 50px}
#footer {clear:both;border-top: 1px solid #E3E8EE;padding: 10px 0 30px;font-size:86%;color:#999}
	#footer p {margin:0}
	#footer a:link {color:#999}

    </style>
    
  </head>
  <body>
  	<div id="wrap">
	<div id="header">
	    <div id="menu">
	    	<ul>
	    		<li><a href="?action=index">Home</a></li>
	    		<li><a href="">Show</a>
	    			<ul class="sub-menu">
		    			<li><a href="?action=channels">Channels</a></li>
		    			<li><a href="?action=scripts">Scripts</a></li>
		  				<li><a href="?action=queues">Queues</a></li>
		  				<li><a href="?action=queue_calls">Queue Calls</a></li>	  					  			
		  				<li><a href="?action=users">Users</a></li>	    			
	    			</ul>
	    		</li>
	  			<li><a href="">Actions</a>
	  				<ul class="sub-menu">
	  					<li><a href="?action=reload">Reload</a></li>
	  				</ul>
	  			</li>
	  			
	  			<li><a href="?action=logout">Logout</a></li>
	    	</ul>
	    </div>
    </div>
    
    <div id="content-wrap">
    <div id="content">
   		<#nested>
   	</div>
   	</div>
   	</div>
  </body>
  </html>
</#macro>