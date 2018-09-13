package com.webcabi.sample.zipcode;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.webcabi.api.zipcloud.model.ZipSearchResponse;
import com.webcabi.snowlinebot.service.ZipCodeService;

@RestController
public class ZipCodeController {

	private final LineMessagingClient lineMessagingClient;
	
	@Autowired
	private ZipCodeService zipCodeService;
	
    ZipCodeController(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

	@RequestMapping("/zip")
	public String index() {
		return "Hello, Zipcode!";
	}
	
	@RequestMapping(value="/zip/search", method=RequestMethod.GET)
	public void pushPref(@RequestParam("zipcode") String zipcode) {
		ZipSearchResponse res = zipCodeService.service(zipcode);
		System.out.println(res);
		String address = "";
		if (res.getResults() != null && res.getResults().size() > 0) {
		    address = res.getResults().get(0).getAddress1() + res.getResults().get(0).getAddress2();
		}
		try {
            lineMessagingClient.pushMessage(new PushMessage("U70dfcc7f9934b89ba929fc857d657358",
                                                new TemplateMessage("都道府県",
                                                    new ConfirmTemplate(zipcode + "は" + address,
                                                        new MessageAction("はい", "はい"),
                                                        new MessageAction("いいえ", "いいえ")
                                                    )
                                                )
                                            )).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
	}
}
