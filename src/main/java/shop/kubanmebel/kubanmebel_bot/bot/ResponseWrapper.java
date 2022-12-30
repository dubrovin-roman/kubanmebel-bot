package shop.kubanmebel.kubanmebel_bot.bot;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseWrapper {
    private List<SendPhoto> photos;
    private List<SendMessage> messages;
    private List<EditMessageText> editMessages;
    private List<TypeSendResponse> whatToSend = new ArrayList<>();

    enum TypeSendResponse {
        PHOTO,
        MESSAGE,
        EDIT_MESSAGE
    }

    public void setTypesSendResponse(TypeSendResponse type) {
        whatToSend.add(type);
    }
}
