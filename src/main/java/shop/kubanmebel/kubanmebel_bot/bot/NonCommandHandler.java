package shop.kubanmebel.kubanmebel_bot.bot;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import shop.kubanmebel.kubanmebel_bot.bot.constants.BotMessageEnum;
import shop.kubanmebel.kubanmebel_bot.bot.constants.ButtonNameEnum;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Category;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Store;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class NonCommandHandler {

    private long chatId;

    private String firstName;

    private String textMessage;

    private List<Category> categoryList;

    public ResponseWrapper getResponse() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        if (textMessage.equals(ButtonNameEnum.CATALOG_BUTTON.getButtonName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            List<SendMessage> sendMessages = new ArrayList<>();
            sendMessages.add(SendMessage.builder()
                            .parseMode("Markdown")
                            .chatId(chatId)
                            .text(BotMessageEnum.CHOOSING_CATEGORY_MESSAGE.getMessage())
                            .replyMarkup(InlineKeyboardMaker.getInlineKeyboardForCatalog(categoryList))
                            .build());
            responseWrapper.setMessages(sendMessages);
        } else if (textMessage.equals(ButtonNameEnum.CONTACTS_BUTTON.getButtonName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setMessages(Util.createListWhitOneMessage(chatId, BotMessageEnum.CONTACTS_MESSAGE.getMessage()));
        } else if (textMessage.equals(ButtonNameEnum.EXIT_BUTTON.getButtonName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setMessages(createListWhitMessageExit(chatId, String.format(BotMessageEnum.EXIT_MESSAGE.getMessage(), firstName)));
        } else {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setMessages(Util.createListWhitOneMessage(chatId, BotMessageEnum.DEFAULT_MESSAGE.getMessage()));
        }
        return responseWrapper;
    }

    private List<SendMessage> createListWhitMessageExit(long chatId, String message) {
        List<SendMessage> sendMessages = new ArrayList<>();
        sendMessages.add(SendMessage.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .text(message)
                .replyMarkup(ReplyKeyboardMaker.removeReplyKeyboard())
                .build());
        return sendMessages;
    }

    public void setMessageData(Message message) {
        this.chatId = message.getChatId();
        this.firstName = message.getChat().getFirstName();
        this.textMessage = message.getText();
    }

    public void setStoreData(Store store) {
        this.categoryList = store.getCategoryList();
    }
}
