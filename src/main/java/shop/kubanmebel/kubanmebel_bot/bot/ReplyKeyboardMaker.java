package shop.kubanmebel.kubanmebel_bot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import shop.kubanmebel.kubanmebel_bot.bot.constants.ButtonNameEnum;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardMaker {
    public static ReplyKeyboardMarkup getReplyKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(ButtonNameEnum.CATALOG_BUTTON.getButtonName()));
        row.add(new KeyboardButton(ButtonNameEnum.CONTACTS_BUTTON.getButtonName()));
        row.add(new KeyboardButton(ButtonNameEnum.EXIT_BUTTON.getButtonName()));

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardRemove removeReplyKeyboard() {
        return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }
}
