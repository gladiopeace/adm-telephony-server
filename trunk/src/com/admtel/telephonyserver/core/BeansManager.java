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
import com.admtel.telephonyserver.interfaces.Loadable;

public class BeansManager implements DefinitionChangeListener, Loadable{

	static Logger log = Logger.getLogger(BeansManager.class);

	Map<String, Object> beans = new HashMap<String, Object>();
	Map<String, BeanDefinition> beanDefinitions = new HashMap<String, BeanDefinition>();

	private static class SingletonHolder {
		private static BeansManager instance = new BeansManager();
	}

	private BeansManager() {
		
	}

	public static BeansManager getInstance() {
		return SingletonHolder.instance;
	}

	public Object getBean(String id) {
		Object result = beans.get(id);
		log.trace(String.format("Looking for bean (%s) -- result = (%s)", id, result));
		return result;
	}

	public void init() {
		log.trace ("Initializing bean manager");
		load();
	}

	@Override
	public void definitionAdded(DefinitionInterface definition) {
		if (definition != null && definition instanceof BeanDefinition) {
			BeanDefinition beanDefinition = (BeanDefinition) definition;
			beanDefinitions.put(definition.getId(), beanDefinition);
		}

	}

	@Override
	public void definitionRemoved(DefinitionInterface definition) {		
		BeanDefinition beanDefinition = beanDefinitions.remove(definition.getId());
		if (beanDefinition != null){
			beanRemove(beanDefinition.getId());
		}
	}

	@Override
	public void defnitionChanged(DefinitionInterface oldDefinition,
			DefinitionInterface newDefinition) {
		definitionRemoved(oldDefinition);
		definitionAdded(newDefinition);

	}

	@Override
	public void reload() {
		load();		
	}

	private void beanDeinit(Object bean){
		if (bean != null){
		try {
			Method m = bean.getClass().getMethod("deinit");
			if (m != null) {
				m.invoke(bean);
			}

		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		}
		
	}
	private void beanRemove(String id){
		Object bean = beans.remove(id);
		beanDeinit(bean);
	}
	private void beanAdd(String id, Object obj){
		Object previousBean = beans.put(id, obj);
		beanDeinit(previousBean);
	}
	@Override
	public void load() {
		for (BeanDefinition beanDefinition : beanDefinitions.values()) {
			Object obj = SmartClassLoader.createInstance(Object.class,
					beanDefinition.getClassName());
			log.trace(String.format("Loading bean (%s) using class(%s)",
					beanDefinition.getId(), beanDefinition.getClassName()));
			if (obj != null) {
				log.trace(String.format("Loaded bean (%s) using class(%s)",
						beanDefinition.getId(), beanDefinition.getClassName()));
				beanAdd(beanDefinition.getId(), obj);
			}
			else{
				log.error(String.format("Failed to load bean (%s) using class(%s)",
						beanDefinition.getId(), beanDefinition.getClassName()));
			}
		}
		for (BeanDefinition beanDefinition : beanDefinitions.values()) {
			// inject parameters
			if (beanDefinition.getParameters() != null) {
				Object obj = getBean(beanDefinition.getId());
				Set<Entry<String, String>> parameters = beanDefinition
						.getParameters().entrySet();
				for (Entry<String, String> parameter : parameters) {
					try {
						Field field = obj.getClass().getField(
								parameter.getKey());

						Class fieldType = field.getType();

						// TODO complete types
						if (fieldType.isPrimitive()) {
							if (fieldType == Boolean.TYPE) {
								field.set(obj, Boolean.getBoolean(parameter
										.getValue()));
							} else if (fieldType == Integer.TYPE) {
								field.set(obj,
										Integer.parseInt(parameter.getValue()));
							}
						} else if (fieldType.isAssignableFrom(String.class)) {
							field.set(obj, parameter.getValue().toString());
						} else {
							Object bean = getBean(parameter.getValue()); //must be an object
							if (bean == null) {
								throw new Exception(String.format(
										"Invalid bean (%s)",
										parameter.getValue()));
							}
							field.set(obj, bean);
						}
					} catch (Exception e) {
						log.error(String.format("Error loading class {%s} : %s", parameter.getValue(), e.getMessage()), e);
					}
				}
				try {
					Method m = obj.getClass().getMethod("init");
					if (m != null) {
						m.invoke(obj);
					}

				} catch (Exception e) {
					log.warn(e.getMessage(), e);
				}

			}
		}
		
	}

}
