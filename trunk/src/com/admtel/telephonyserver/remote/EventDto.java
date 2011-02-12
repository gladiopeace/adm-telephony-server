package com.admtel.telephonyserver.remote;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.ConnectedEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.DisconnectedEvent;
import com.admtel.telephonyserver.events.AlertingEvent;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.assembler.DTOAssembler;

@Dto
public abstract class EventDto {
	public abstract String toDisplayString();
}
