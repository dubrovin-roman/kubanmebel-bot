package shop.kubanmebel.kubanmebel_bot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import shop.kubanmebel.kubanmebel_bot.bot.constants.ButtonNameEnum;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Product;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Util {

    public static List<SendPhoto> createListProductsToSend(long chatId, List<Product> productList) {
        List<SendPhoto> sendPhotos = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        productList.forEach(product -> {
            InputFile inputFile = new InputFile(new ByteArrayInputStream(product.getBytesPicture()), "temp");
            String nameProductWithUrl = "["+ product.getNameProduct() +"](" + product.getLinkDetailsProduct() + ")";
            String caption = count.get() + ". " + nameProductWithUrl
                    + "\nАртикул: " + product.getArticleNumber()
                    + "\nЦена: " + product.getPrice() + " руб.";
            sendPhotos.add(SendPhoto.builder()
                    .parseMode("Markdown")
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(caption)
                    .replyMarkup(InlineKeyboardMaker
                            .getInlineKeyboardWithOneButtonURL(ButtonNameEnum.BUY_ONLINE_BUTTON.getButtonName(), product.getLinkDetailsProduct()))
                    .build());
            count.getAndIncrement();
        });
        return sendPhotos;
    }

    public static List<SendMessage> createListWhitOneMessage(long chatId, String message) {
        List<SendMessage> sendMessages = new ArrayList<>();
        sendMessages.add(SendMessage.builder()
                .parseMode("Markdown")
                .chatId(chatId)
                .text(message)
                .build());
        return sendMessages;
    }
}
