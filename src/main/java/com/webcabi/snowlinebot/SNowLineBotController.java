package com.webcabi.snowlinebot;

import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;

@RestController
public class SNowLineBotController {

	private final LineMessagingClient lineMessagingClient;
	
    SNowLineBotController(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

	@RequestMapping("/")
	public String index() {
		return "Hello, World!";
	}

	@RequestMapping("/alerm")
	public void pushAlarm() {
        try {
            lineMessagingClient.pushMessage(new PushMessage("ユーザID",
                                                new TemplateMessage("明日は燃えるごみの日だよ！",
                                                    new ConfirmTemplate("ごみ捨ては終わった？",
                                                        new MessageAction("はい", "はい"),
                                                        new MessageAction("いいえ", "いいえ")
                                                    )
                                                )
                                            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

	@RequestMapping("/event")
	public void pushEvent() {
		List<Action> actions = new ArrayList<Action>() {
			private static final long serialVersionUID = 1L;

			{
                add(new MessageAction("2018年9月13日", "2018-09-13"));
                add(new MessageAction("2018年9月14日", "2018-09-14"));
			}
		};
        try {
            lineMessagingClient.pushMessage(new PushMessage("Uef8ec22beca510e31a4b9130df7830db",
                                                new TemplateMessage("イベント情報",
                                                	new ButtonsTemplate("https://gitlab.com/uploads/-/system/user/avatar/374710/avatar.png", "Title", "Text", actions)
                                                )
                                            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
