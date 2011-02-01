
class AdmCompleteExample extends Script {
	
	def state="WaitingForCall";
	Channel a;
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onTimer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(Event event) {
		println "##### Script " + this +": got event : " + event
		switch (state){
			case "WaitingForCall":
				processWaitingForCall_State(event)
			break
			case "Answering":
				processAnswering_State(event)
			break
			case "Playing":
				processPlaying_State(event)
			break
		}

	}
	
	@Override
	public String getDisplayStr() {
		// TODO Auto-generated method stub
		return null;
	}
	void processPlaying_State(Event event){
		switch(event.getEventType()){
			case EventType.PlayAndGetDigitsEnded:
				PlayAndGetDigitsEndedEvent e = event
				println "################### Got digits "+ e.getDigits()
				a.joinConference "1234", true, false, false
			break
		}
	}
	void processWaitingForCall_State(Event event){
		switch (event.getEventType()){
			case EventType.InboundAlerting:
				InboundAlertingEvent e = event
				a = e.getChannel()
				if (a != null){					
					a.answer()
				}
				state = "Answering"
			break
			case EventType.OutboundAlerting:
				OutboundAlertingEvent e = event
				a = e.getChannel()
				if (a!=null){
				println "*********** outbound alerting on channel " + a
				}
				state = "Answering"
				break
		}
	}
	void processAnswering_State(Event event){
		switch (event.getEventType()){
			case EventType.Answered:
			a.setHangupAfter(50000);
			PromptBuilder pb = PromptBuilderFactory.getInstance().getPromptBuilder(a.getLanguage())
			def prompts = pb.currencyToPrompt(new BigDecimal(120.34))
			prompts += (pb.numberToPrompt(13245))
			prompts += (pb.numberToPrompt(33245))
			prompts += (pb.numberToPrompt(34245))
			println prompts
			a.playAndGetDigits(10, (String[])prompts, 1000, "#")
			
			println "*******" + prompts
			
			
			state = "Playing"
			break
		}
	}
	
	@Override
	protected void onStart(){
	
	}

}
