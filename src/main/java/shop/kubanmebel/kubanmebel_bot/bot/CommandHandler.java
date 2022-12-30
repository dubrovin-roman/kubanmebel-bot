package shop.kubanmebel.kubanmebel_bot.bot;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import shop.kubanmebel.kubanmebel_bot.bot.constants.BotMessageEnum;
import shop.kubanmebel.kubanmebel_bot.bot.constants.ButtonNameEnum;
import shop.kubanmebel.kubanmebel_bot.bot.constants.CommandNameEnum;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Category;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Product;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Store;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@NoArgsConstructor
public class CommandHandler {
    private long chatId;

    private String firstName;

    private String textMessage;

    private Category newCategory;

    private byte[] bytesPictureStore;

    public ResponseWrapper getResponse() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        if (textMessage.equals(CommandNameEnum.START.getCommandName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.PHOTO);
            List<SendPhoto> sendPhotos = new ArrayList<>();
            InputFile inputFile = new InputFile(new ByteArrayInputStream(bytesPictureStore), "temp");
            sendPhotos.add(SendPhoto.builder()
                    .parseMode("Markdown")
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(String.format(BotMessageEnum.HELLO_MESSAGE.getMessage(), firstName))
                    .replyMarkup(ReplyKeyboardMaker.getReplyKeyboard())
                    .build());
            responseWrapper.setPhotos(sendPhotos);
        } else if (textMessage.equals(CommandNameEnum.NEW.getCommandName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.PHOTO);
            responseWrapper.setMessages(Util.createListWhitOneMessage(chatId,
                            String.format(BotMessageEnum.NEW_MESSAGE.getMessage(), newCategory.getProductList().size())));
            responseWrapper.setPhotos(Util.createListProductsToSend(chatId, newCategory.getProductList()));
        } else if (textMessage.equals(CommandNameEnum.CONTACTS.getCommandName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setMessages(Util.createListWhitOneMessage(chatId, BotMessageEnum.CONTACTS_MESSAGE.getMessage()));
        } else if (textMessage.equals(CommandNameEnum.AUTHOR.getCommandName())) {
            responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
            responseWrapper.setMessages(Util.createListWhitOneMessage(chatId, BotMessageEnum.AUTHOR_MESSAGE.getMessage()));
        }
        return responseWrapper;
    }

    public void setMessageData(Message message) {
        this.chatId = message.getChatId();
        this.firstName = message.getChat().getFirstName();
        this.textMessage = message.getText();
    }

    public void setStoreData(Store store) {
        this.bytesPictureStore = store.getBytesPictureStore();
        store.getCategoryList().forEach(category -> {
            if (category.getName().equals("Новинки"))
                this.newCategory = category;
        });
    }
}
