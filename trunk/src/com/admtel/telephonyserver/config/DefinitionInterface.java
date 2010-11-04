package com.admtel.telephonyserver.config;

public interface DefinitionInterface {
	public String getId();

	/***
	 * 
	 * @param definition TODO
	 * @return boolean If a core value has changed we return true. Core values
	 *         changes require different action on the configurable entity
	 *         (reload). While non-core changes need just an update to the
	 *         definition
	 */
	public boolean isCoreChange(DefinitionInterface definition);
}
