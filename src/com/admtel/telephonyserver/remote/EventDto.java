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

}
