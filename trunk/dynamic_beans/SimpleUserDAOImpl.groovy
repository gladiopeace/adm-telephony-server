import com.admtel.telephonyserver.directory.User;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.directory.*;
import java.util.Map;
import java.util.HashMap;

class SimpleUserDAOImpl implements UserDAO{
	
	static Logger log = Logger.getLogger(SimpleUserDAOImpl.class)
	
	def users = [:]

	private void addUser(String pName, String pPassword, String pAccount){
		def user = [name:pName,password:pPassword, account:pAccount] as User
		users[user.name] = user
	}
	public init(){
		addUser('conference$tandium','conference1234','conference_account')
		addUser('test$tandium','test1234','testaccount')
		addUser('test2$tandium', 'test1234','testaccount')
		
	}
	public User getUser(String name){		
		User user = users[name]
		log.trace("Looking up user ${name}")
		return user
	}
}