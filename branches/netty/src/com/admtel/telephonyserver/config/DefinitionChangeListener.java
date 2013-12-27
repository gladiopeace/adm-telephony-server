package com.admtel.telephonyserver.config;

public interface DefinitionChangeListener {
	public void defnitionChanged(DefinitionInterface oldDefinition, DefinitionInterface newDefinition);
	public void definitionRemoved(DefinitionInterface definition);
	public void definitionAdded(DefinitionInterface definition);
}
