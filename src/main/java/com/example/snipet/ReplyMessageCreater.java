package com.example.snipet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.define.StringW;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;

public final class ReplyMessageCreater {
	
	public static Message createText(String replyText) {
		
		return new TextMessage(replyText);
				
	}
	
	public static Message createButtonsTemplate(
			String title,
			String text,
			String imageURL,
			List<StringW> buttonTextList
			) {
		
		List<Action> actions = new ArrayList<>();
		
		for(int i=0; i<buttonTextList.size(); i++) {
			Action buttonAction = new MessageAction(buttonTextList.get(i).s1,buttonTextList.get(i).s2);
			actions.add(buttonAction);
		}
		
        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(imageURL, title, text, actions);

        String altText = "this is buttonTemplate.";

        return new TemplateMessage(altText, buttonsTemplate);
    }
	
	public static Message createConfirmTemplate(
			String question,
			StringW yes,
			StringW no
			) {
		
            return new TemplateMessage("this is confirmTemplate.",
                    new ConfirmTemplate(question,
                            new MessageAction(yes.s1,yes.s2),
                            new MessageAction(no.s1,no.s2)
                            )
                    );
	}
		
		
}
