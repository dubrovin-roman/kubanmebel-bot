package shop.kubanmebel.kubanmebel_bot.bot;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import shop.kubanmebel.kubanmebel_bot.bot.constants.BotMessageEnum;
import shop.kubanmebel.kubanmebel_bot.bot.constants.ButtonNameEnum;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.*;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class CallbackQueryHandler {
    private String callbackData;
    private long chatId;

    private int messageId;

    private List<AbstractCategory> categoryList;

    public ResponseWrapper getResponse() {
        AbstractCategory category = findCategoryByCallbackData(categoryList, callbackData);
        if (category.getName().equals("Новинки")) {
            return createResponseForCategoryNew(chatId, messageId, category);
        } else if (category.getName().equals("Кухни")) {
            return createResponseForCategoryKitchen(chatId, messageId, category);
        }

        if (category.isSubCategoryListEmpty()) {
            return createResponseForCategoryWithProducts(chatId, messageId, category);
        } else {
            return createResponseForCategoryWithoutProducts(chatId, messageId, category);
        }
    }

    private AbstractCategory findCategoryByCallbackData(List<AbstractCategory> list, String data) {
        for (AbstractCategory category : list) {
            if (category.getCallbackData().equals(data))
                return category;
        }
        return null;
    }

    private ResponseWrapper createResponseForCategoryWithoutProducts(long chatId, int messageId, AbstractCategory category) {
        String categoryName = category.getName();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.EDIT_MESSAGE);
        List<EditMessageText> sendEditMessages = new ArrayList<>();
        sendEditMessages.add(EditMessageText.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format(BotMessageEnum.CHOOSING_SUBCATEGORY_WITH_SUBCATEGORIES.getMessage(), categoryName))
                .replyMarkup(InlineKeyboardMaker.getInlineKeyboardForCategoryWithoutProducts(category.getSubCategoryList()))
                .build());
        responseWrapper.setEditMessages(sendEditMessages);
        return responseWrapper;
    }

    private ResponseWrapper createResponseForCategoryWithProducts(long chatId, int messageId, AbstractCategory category) {
        String categoryName = category.getName();
        int sizeProductList = category.getProductList().size();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.EDIT_MESSAGE);
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.PHOTO);
        responseWrapper.setEditMessages(createListWhitOneEditMessage(chatId, messageId,
                String.format(BotMessageEnum.CHOOSING_SUBCATEGORY_WITH_PRODUCTS_MESSAGE.getMessage(), categoryName, sizeProductList)));
        responseWrapper.setPhotos(Util.createListProductsToSend(chatId, category.getProductList()));
        return responseWrapper;
    }

    private ResponseWrapper createResponseForCategoryNew(long chatId, int messageId, AbstractCategory category) {
        int sizeProductList = category.getProductList().size();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.EDIT_MESSAGE);
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.PHOTO);
        responseWrapper.setEditMessages(createListWhitOneEditMessage(chatId, messageId,
                String.format(BotMessageEnum.NEW_MESSAGE.getMessage(), sizeProductList)));
        responseWrapper.setPhotos(Util.createListProductsToSend(chatId, category.getProductList()));
        return responseWrapper;
    }

    private ResponseWrapper createResponseForCategoryKitchen(long chatId, int messageId, AbstractCategory category) {
        String categoryName = category.getName();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.EDIT_MESSAGE);
        responseWrapper.setTypesSendResponse(ResponseWrapper.TypeSendResponse.MESSAGE);
        List<EditMessageText> editMessages = new ArrayList<>();
        List<SendMessage> sendMessages = new ArrayList<>();
        editMessages.add(EditMessageText.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format(BotMessageEnum.CHOOSING_SUBCATEGORY_WITH_SUBCATEGORIES.getMessage(), categoryName))
                .replyMarkup(InlineKeyboardMaker.getInlineKeyboardForCategoryWithoutProducts(category.getSubCategoryList()))
                .build());
        sendMessages.add(SendMessage.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .text(BotMessageEnum.CALCULATION_KITCHEN_MESSAGE.getMessage())
                .replyMarkup(InlineKeyboardMaker.getInlineKeyboardWithOneButtonURL(ButtonNameEnum.CALCULATE_ON_WEBSITE_BUTTON.getButtonName(), Store.URL_MOCLIENTS))
                .build());
        responseWrapper.setEditMessages(editMessages);
        responseWrapper.setMessages(sendMessages);
        return responseWrapper;
    }

    private List<EditMessageText> createListWhitOneEditMessage(long chatId, int messageId, String message) {
        List<EditMessageText> sendEditMessages = new ArrayList<>();
        sendEditMessages.add(EditMessageText.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .messageId(messageId)
                .text(message)
                .build());
        return sendEditMessages;
    }

    public void setUpdateData(Update update) {
        this.callbackData = update.getCallbackQuery().getData();
        this.chatId = update.getCallbackQuery().getMessage().getChatId();
        this.messageId = update.getCallbackQuery().getMessage().getMessageId();
    }

    public void setStoreData(Store store) {
        List<AbstractCategory> result = new ArrayList<>();
        getAllCategories(store.getCategoryList(), result);
        this.categoryList = result;
    }

    private void getAllCategories(List<? extends AbstractCategory> list, List<AbstractCategory> result) {
        for (AbstractCategory category : list) {
            result.add(category);
            if (!category.isSubCategoryListEmpty()) {
                getAllCategories(category.getSubCategoryList(), result);
            }
        }
    }
 }
