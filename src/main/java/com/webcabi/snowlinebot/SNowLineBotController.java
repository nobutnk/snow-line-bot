package com.webcabi.snowlinebot;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.webcabi.snowlinebot.api.connect.ConnectChatApi;
import com.webcabi.snowlinebot.MessageRequest;

@RestController
public class SNowLineBotController {

	private final LineMessagingClient lineMessagingClient;
	
	@Autowired
	private ConnectChatApi connectApi;
	
	@Value("${line.userId}")
	private String userId;
	
    SNowLineBotController(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

	@RequestMapping("/")
	public String index() {
		return "Hello, World!";
	}

	@RequestMapping("snow/get")
	public void getsnow() {
		try {
			connectApi.get(null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("snow/post")
	public void postsnow(@RequestParam("message") String message) {
		try {
			connectApi.post(message);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/alerm")
	public void pushAlarm() {
        try {
            lineMessagingClient.pushMessage(new PushMessage("U70dfcc7f9934b89ba929fc857d657358",
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
            lineMessagingClient.pushMessage(new PushMessage("U70dfcc7f9934b89ba929fc857d657358",
                                                new TemplateMessage("イベント情報",
                                                	new ButtonsTemplate("https://gitlab.com/uploads/-/system/user/avatar/374710/avatar.png", "Title", "Text", actions)
                                                )
                                            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

	@RequestMapping(value = "/invite",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendMessage(@RequestBody InviteRequest request) throws Exception {
		List<String> users = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("Uef8ec22beca510e31a4b9130df7830db");
				add("U096871bbdcf5f3762d56f9e812bba482");
				add("U80f7dbdf2a6b27a95b3b522261b779df");
				add("U7d53343510f879ec9f655efbce371f2d");
				add("U1e55466292f8c6abfb15ed30b127bb72");
			}
		};

		try {
			for (String user : users) {
				lineMessagingClient.pushMessage(new PushMessage(user,
					new TemplateMessage("對間さんから焼肉の招待(" + request.getDatetime() + ")が届きました！ぜひ参加してね！！",
						new ConfirmTemplate("對間さんから焼肉の招待(" + request.getDatetime() + ")が届きました！ぜひ参加してね！！参加しますか？",
						new MessageAction("はい", "はい"),
						new MessageAction("いいえ", "いいえ")
				)))).get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
