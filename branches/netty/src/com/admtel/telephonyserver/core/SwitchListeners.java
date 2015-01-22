package com.admtel.telephonyserver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.admtel.telephonyserver.asterisk.ASTSwitchListener;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.SwitchListenerDefinition;
import com.admtel.telephonyserver.config.SwitchType;
import com.admtel.telephonyserver.freeswitch.FSSwitchListener;
import java.util.List;

public class SwitchListeners implements DefinitionChangeListener{
	
	Map<String, SwitchListener> switchListeners = new HashMap<String, SwitchListener>();
	
	private SwitchListeners(){
		
	}
	private static class SingletonHolder{
		private final static SwitchListeners instance = new SwitchListeners();
	}
	
	public static SwitchListeners getInstance(){
		return SingletonHolder.instance;
	}

    public List<SwitchListener> getBySwitchType(SwitchType switchType){
        List<SwitchListener> result = new ArrayList<SwitchListener>();
        for (Map.Entry<String, SwitchListener> entry:switchListeners.entrySet()){
            SwitchListener switchListener = entry.getValue();
            switch (switchType){
                case Asterisk:
                    if (switchListener instanceof ASTSwitchListener){
                        result.add(switchListener);
                    }
                    break;
                case Freeswitch:
                    if (switchListener instanceof FSSwitchListener){
                        result.add(switchListener);
                    }
                    break;
            }
        }
        return result;
    }
	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof SwitchListenerDefinition){
			SwitchListenerDefinition sld = (SwitchListenerDefinition) definition;
			switch (sld.getSwitchType()){
			case Asterisk:
			{
				SwitchListener listener = new ASTSwitchListener((SwitchListenerDefinition) definition);
				switchListeners.put(definition.getId(), listener);
				listener.start();
			}
				break;
			case Freeswitch:
			{
				SwitchListener listener = new FSSwitchListener((SwitchListenerDefinition) definition);
				switchListeners.put(definition.getId(), listener);
				listener.start();
			}
			break;
			}
			
		}
		
	}

	public Collection<SwitchListener> getAll(){
		return switchListeners.values();
	}
	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		// TODO Auto-generated method stub
		
	}
}
