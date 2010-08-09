package com.admtel.telephonyserver.prompts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;
import com.admtel.telephonyserver.config.PromptBuilderDefinition;
import com.admtel.telephonyserver.core.SmartClassLoader;

public class PromptBuilderFactory implements DefinitionChangeListener{
	
	static Logger log = Logger.getLogger(PromptBuilderFactory.class);
	Map<String, PromptBuilder> idMap = new HashMap<String, PromptBuilder>();
	
	
	private PromptBuilderFactory()
	{
		
	}
	
	private static class SingletonHolder {
		private static PromptBuilderFactory instance = new PromptBuilderFactory();
	}
	
	public static PromptBuilderFactory getInstance(){
		return SingletonHolder.instance;
	}
	public PromptBuilder getPromptBuilder(Locale local) {
		log.trace("Building PromptBuilder for " + local);
		PromptBuilder result = idMap.get(local.toString());
		
		log.trace("Built Prompt builder for "+ local +" : "+ result);
		return result;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof PromptBuilderDefinition){
			PromptBuilderDefinition promptBuilderDefinition = (PromptBuilderDefinition)definition;
			PromptBuilder promptBuilder = SmartClassLoader.createInstance(PromptBuilder.class, promptBuilderDefinition.getClassName());
			if (promptBuilder != null){
				idMap.put(promptBuilderDefinition.getId(), promptBuilder);
			}
		}
		
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof PromptBuilderDefinition){
			PromptBuilderDefinition promptBuilderDefinition = (PromptBuilderDefinition)definition;
			idMap.remove(promptBuilderDefinition.getId());
		}
		
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		definitionRemoved(oldDefinition);
		definitionAdded(newDefinition);		
	}
}
