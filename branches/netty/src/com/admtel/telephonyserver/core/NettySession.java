package com.admtel.telephonyserver.core;

import io.netty.channel.ChannelHandlerContext;

public class NettySession implements Session {

	ChannelHandlerContext context;
	
	public NettySession (ChannelHandlerContext context){
		this.context = context;
	}
	@Override
	public void write(String message) {
		context.writeAndFlush(message+"\r\n\r\n");

	}

}
