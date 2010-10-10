package com.admtel.telephonyserver.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.config.BeanDefinition;
import com.admtel.telephonyserver.config.DefinitionChangeListener;
import com.admtel.telephonyserver.config.DefinitionInterface;

public class BeansManager implements DefinitionChangeListener {

	static Logger log = Logger.getLogger(BeansManager.class);
	
	Map<String, Object> beans = new HashMap<String, Object>();

	private static class SingletonHolder {
		private static BeansManager instance = new BeansManager();
	}

	public static BeansManager getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition != null && definition instanceof BeanDefinition) {
			BeanDefinition beanDefinition = (BeanDefinition) definition;
			Object obj = SmartClassLoader.createInstance(Object.class,
					beanDefinition.getClassName());
			if (obj != null) {
				beans.put(beanDefinition.getId(), obj);
				// inject parameters
				if (beanDefinition.getParameters() != null) {
					Set<Entry<String, String>> parameters = beanDefinition
							.getParameters().entrySet();
					for (Entry<String, String> parameter : parameters) {
						try {
							Field field = obj.getClass().getField(parameter.getKey());
							
							Class fieldType = field.getType();
							
							//TODO complete types
							if (fieldType.isPrimitive()){
								if (fieldType == Boolean.TYPE){
									field.set(obj, Boolean.getBoolean(parameter.getValue()));
								}
								else 
								if (fieldType == Integer.TYPE){
									field.set(obj, Integer.parseInt(parameter.getValue()));
								}
							}
							else if (fieldType.isAssignableFrom(String.class)){
									field.set(obj, parameter.getValue());
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
					try {
						Method m = obj.getClass().getMethod("init");
						if (m != null){
							m.invoke(obj);
						}
						
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					} 

				}
			}
		}

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