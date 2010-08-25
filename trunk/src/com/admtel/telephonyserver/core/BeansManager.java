package com.admtel.telephonyserver.core;

import java.util.HashMap;
import java.util.Map;

import com.admtel.telephonyserver.config.BeanDefinition;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;


public class BeansManager  implements DefinitionChangeListener{
	private BeansManager(){
		
	}
	
	Map<String, Object> idMap = new HashMap<String, Object>();
	
	private static class SingletonHolder {
		private static BeansManager instance = new BeansManager();
	}

	public static BeansManager getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition instanceof BeanDefinition){
			BeanDefinition beanDefinition = (BeanDefinition) definition;
			Object bean = SmartClassLoader.createInstance(Object.class, beanDefinition.getClassName());
			if (bean instanceof groovy.lang.Script){
				groovy.lang.Script script = (groovy.lang.Script) bean;
				script.run();
			}
			if (bean != null){
				idMap.put(beanDefinition.getId(), bean);
			}
		}
		
	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {
		if (definition instanceof BeanDefinition){			
			idMap.remove(definition.getId());
		}
		
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		if (newDefinition instanceof BeanDefinition &&
				oldDefinition instanceof BeanDefinition){
			definitionRemoved(oldDefinition);
			definitionAdded(newDefinition);
		}
		
	}
}
