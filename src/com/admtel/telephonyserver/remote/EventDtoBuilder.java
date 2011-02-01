package com.admtel.telephonyserver.remote;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.events.AnsweredEvent;
import com.admtel.telephonyserver.events.Event;
import com.admtel.telephonyserver.events.HangupEvent;
import com.admtel.telephonyserver.events.AlertingEvent;
import dp.lib.dto.geda.assembler.DTOAssembler;

public class EventDtoBuilder {
	
	static Logger log = Logger.getLogger(EventDto.class);

	Map<Class, Class> BUILDER_MAP = new HashMap<Class, Class>();


	public EventDto buildEventDto(Event event) {
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
	
	
	private static class SingletonHolder{
		private final static EventDtoBuilder instance =new EventDtoBuilder();
	}
	
	public static EventDtoBuilder getInstance(){
		return SingletonHolder.instance;
	}
	
	private EventDtoBuilder(){
		try{			
		BUILDER_MAP.put(AlertingEvent.class,
				AlertingEventDto.class);
		BUILDER_MAP.put(AnsweredEvent.class, AnsweredEventDto.class);
		BUILDER_MAP.put(HangupEvent.class, HangupEventDto.class);		
		}
		catch (Exception e){
			log.fatal(e.getMessage(), e);
			System.exit(-1);
		}
	}
}
