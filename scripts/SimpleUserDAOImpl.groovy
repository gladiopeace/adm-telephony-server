import com.admtel.telephonyserver.directory.User;

import org.apache.log4j.Logger;
import com.admtel.telephonyserver.directory.*;
import java.util.Map;
import java.util.HashMap;

class SimpleUserDAOImpl implements UserDAO{
	
	static Logger log = Logger.getLogger(SimpleUserDAOImpl.class)
	
	Map<String, User> users = new HashMap<String, User>()
		
	public init(){
		log.trace("Simple UserDAOImpl Init")
		User user = new User()
		user.setId('agent_1')
		user.setPassword('agent_1')
		user.setDomain('172.16.140.129')
		users.put('agent_1@172.16.140.129', user)
	}
	public User getUser(String id, String domain){		
		User user = users.get(id+"@"+domain)
		log.trace("Looking up user ${id}@${domain} = ${user}")
		return user
	}
}