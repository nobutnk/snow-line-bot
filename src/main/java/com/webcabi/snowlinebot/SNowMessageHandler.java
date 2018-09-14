package com.webcabi.snowlinebot;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.webcabi.snowlinebot.service.NotifyMessageHandler;

@LineMessageHandler
public class SNowMessageHandler {
	
	private final NotifyMessageHandler notifyMessageHandler;
	
	public SNowMessageHandler(NotifyMessageHandler notifyMessageHandler) {
		this.notifyMessageHandler = notifyMessageHandler;
	}
    
    @EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException, InterruptedException, ExecutionException {
    	System.out.println("event: " + event);
        BotApiResponse response = notifyMessageHandler.reply(event);
        System.out.println("Sent messages: " + response);
    }
	
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
