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
import com.webcabi.snowlinebot.api.linepaymock.LinePayMock;
import com.webcabi.snowlinebot.api.table.TableApi;
import com.webcabi.snowlinebot.MessageRequest;

@RestController
public class SNowLineBotController {

	private final LineMessagingClient lineMessagingClient;
	
	@Autowired
	private ConnectChatApi connectApi;
	
	@Autowired
	private TableApi tableApi;
	
	@Autowired
	private LinePayMock linePayMock;
	
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
	
	@RequestMapping("/snow/lineids")
	public void lineids() {
		try {
			tableApi.getLineId();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/alerm")
	public void pushAlarm() {
        try {
            lineMessagingClient.pushMessage(new PushMessage(userId,
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
            lineMessagingClient.pushMessage(new PushMessage(userId,
                                                new TemplateMessage("イベント情報",
                                                	new ButtonsTemplate("https://gitlab.com/uploads/-/system/user/avatar/374710/avatar.png", "Title", "Text", actions)
                                                )
                                            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
	
	@RequestMapping("/calc")
	public void calc(@RequestParam("amount") String amount) {
		
		try {
			List<String> lineIds = tableApi.getLineId();
			int result = linePayMock.splitBill(lineIds.size(), Integer.parseInt(amount));
			System.out.println(result);
			
			try {
				for (String lineId : lineIds) {
	            lineMessagingClient.pushMessage(new PushMessage(lineId,
	                                                new TemplateMessage("飲み会代金額",
	                                                    new ConfirmTemplate("飲み会代は" + amount + " 円だったよ。払ってくれるよね？",
	                                                        new MessageAction("はい", result + "円だよ"),
	                                                        new MessageAction("いいえ", "残念だよ")
	                                                    )
	                                                )
	                                            )).get();
				}
	        } catch (InterruptedException | ExecutionException e) {
	            throw new RuntimeException(e);
	        }
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
		
	@RequestMapping(value = "/message",
			        method = RequestMethod.POST,
			        consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendMessage(@RequestBody MessageRequest request) throws Exception {
		List<String> lineIds = tableApi.getLineId();
		
        try {
            for (String user : users) {
        		lineMessagingClient.pushMessage(new PushMessage(user,
        				                        new TextMessage(request.getMessage())
        				                       	)).get();
        	}
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

	@RequestMapping(value = "/invite",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendMessage(@RequestBody InviteRequest request) throws Exception {
		List<String> lineIds = tableApi.getLineId();

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

	@RequestMapping(value = "/test",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendTest(@RequestBody InviteRequest request) throws Exception {
		List<String> lineIds = tableApi.getLineId();

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
