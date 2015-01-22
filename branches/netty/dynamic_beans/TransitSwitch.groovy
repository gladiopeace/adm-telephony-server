

class TransitSwitch extends Script {
	
	static Logger log = Logger.getLogger(TransitSwitch.class)
	
	AuthorizeResult authorizeResult
	Channel channel
	int currentRoute = 0
	
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}
		
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onTimer() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onStart(){
	
	}
	
	@Override
	protected void processEvent(Event event) {
		currentState event
		
	}
	
	def waitForCall = {
		Event evt = it
		switch (evt.getEventType()){
			case EventType.InboundAlerting:
			InboundAlertingEvent iae = evt
			channel = iae.getChannel()
			
			log.trace("Got A Call on channel ${channel} :  Authorizing user ${channel?.userName}")
			
			authorizeResult = Radius.authorize(channel, channel?.getUserName(), "", "", channel?.getCallingStationId(), channel?.getCalledStationId(), true, true)
			
			log.trace("Channel ${channel}, authorizeResult ${authorizeResult}")
			
			if (authorizeResult.getRoutes().size() == 0){
				log.warn("Channel ${channel} no route to destination hanging up")
				channel.hangup(DisconnectCode.NoCircuitAvailable)
			}
			else{
				channel.dial(authorizeResult.routes[currentRoute], 10)
				currentState = dialingOutbound
			}
			
			break;
		}
	}
	
	def dialingOutbound = {
		Event evt = it
		switch (evt.getEventType()){
			case EventType.Hangup:
			HangupEvent he = evt
			if (he.getChannel() != channel){
				log.trace("Channel ${channel} dial failed ${evt}")
				if (currentRoute < authorizeResult.getRoutes().size()-1){
					currentRoute ++
					channel.dial(authorizeResult.getRoutes()[currentRoute], 10)
				}
				else{
					log.trace "Channel ${channel} --- no more routes to try"
				}
			}
			break;
		}
	}
	def currentState = waitForCall
	
}
