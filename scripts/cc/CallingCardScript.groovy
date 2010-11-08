package cc;
import org.apache.log4j.Logger;

class CallingCardScript extends common.GScript {
	
	static String CURRENT_DIR;
	
	static Logger log = Logger.getLogger(CallingCardScript.class)
	
	static{
		CURRENT_DIR = org.codehaus.groovy.ast.ClassHelper.make(CallingCardScript.class).getPackageName();
	}
	
	public CallingCardScript(){		
		super(CURRENT_DIR, "WaitForCall")
	}
	@Override
	protected void onStop() {
		super.onStop()
		log.trace("Script stopped")
	}
}
