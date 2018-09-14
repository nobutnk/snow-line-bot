package com.webcabi.snowlinebot.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

@Service
public class NotifyMessageHandler {
	
	private final LineMessagingClient lineMessagingClient;
	
	@Value("${yakiniku.url}")
	private String yakinikuUrl;
	
	public NotifyMessageHandler(LineMessagingClient lineMessagingClient) {

        this.lineMessagingClient = lineMessagingClient;
    }

	public BotApiResponse reply(MessageEvent<TextMessageContent> event) throws IOException, InterruptedException, ExecutionException {

		String receivedMessage = event.getMessage().getText();
		String replyToken = event.getReplyToken();
        List<Message> messages = null;
		switch (receivedMessage) {
		case "焼肉食べたい":
				messages = Arrays.asList(
						new TextMessage("イイね！"),
						new TextMessage(yakinikuUrl));
		}
		
		return lineMessagingClient
	            .replyMessage(new ReplyMessage(replyToken, messages))
	            .get();
	}

}
