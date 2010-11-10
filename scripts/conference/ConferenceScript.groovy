package conference;
import org.apache.log4j.Logger;

class ConferenceScript extends common.GScript {
	
	static String CURRENT_DIR;
	
	static Logger log = Logger.getLogger(ConferenceScript.class)
		
	static{
		CURRENT_DIR = org.codehaus.groovy.ast.ClassHelper.make(ConferenceScript.class).getPackageName();
	}
	
	public ConferenceScript(){		
		super(CURRENT_DIR, "WaitForCall")
	}
	@Override
	protected void onStop() {
		super.onStop()
		log.trace("Script stopped")
	}
}
