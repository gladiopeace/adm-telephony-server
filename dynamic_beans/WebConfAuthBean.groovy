
class WebConfAuthBean extends Expando{
	
	def users=[:]
	
	public init(){
		users['admin'] = [password:'admin1234', type:'admin']
		users['support'] = [password:'support1234', type:'support']
	}
	
	def getUser(def username){
		return users[username]
	}
}
