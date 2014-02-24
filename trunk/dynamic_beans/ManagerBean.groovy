import org.apache.log4j.Logger;

class ManagerBean{
	
	static Logger log = Logger.getLogger(ManagerBean.class)
	
	public String address
	public int port
	public init(){
		log.trace("Manager bean started with ${address}:${port}")
	}
}