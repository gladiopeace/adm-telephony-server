package cc;
import common.GState;
import com.admtel.telephonyserver.events.*;
import com.admtel.telephonyserver.prompts.*;

class PlayCreditState extends GState {

	@Override
	public void onEnter() {
		PromptBuilder pb = PromptBuilderFactory.getInstance().getPromptBuilder(script.channel.getLanguage())
		def prompts = pb.currencyToPrompt(script.session.credit)
		script.channel.playback((String[])prompts,"")
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}	
	def onPlaybackEnded (PlaybackEndedEvent e){
		"GetPhoneNumber"
	}
}
