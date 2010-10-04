package com.admtel.telephonyserver.remote;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.InboundAlertingEvent;
import com.admtel.telephonyserver.events.OutboundAlertingEvent;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.assembler.DTOAssembler;

@Dto
public abstract class EventDto {
	static Logger log = Logger.getLogger(EventDto.class);

	static Map<Class, Class> BUILDER_MAP = new HashMap<Class, Class>();

	static {
		try {
			BUILDER_MAP.put(InboundAlertingEvent.class,
					InboundAlertingEventDto.class);
			BUILDER_MAP.put(AnsweredEvent.class, AnsweredEventDto.class);
			BUILDER_MAP.put(HangupEvent.class, HangupEventDto.class);
			BUILDER_MAP.put(OutboundAlertingEvent.class, OutboundAlertingEventDto.class);
		} catch (Exception e) {
			log.fatal(e.getMessage(), e);
		}
	}

	static public EventDto buildEventDto(Event event) {
		Class dtoClass = BUILDER_MAP.get(event.getClass());
		if (dtoClass != null) {

			try {
				DTOAssembler assembler = DTOAssembler.newAssembler(dtoClass,
						event.getClass());
				EventDto eventDto = (EventDto) dtoClass.newInstance();
				assembler.assembleDto(eventDto, event, null, null);
				return eventDto;
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return null;
	}
}
