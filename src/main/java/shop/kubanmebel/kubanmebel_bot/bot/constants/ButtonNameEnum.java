package shop.kubanmebel.kubanmebel_bot.bot.constants;

import com.vdurmont.emoji.EmojiParser;

public enum ButtonNameEnum {

    CATALOG_BUTTON(":open_file_folder:\nКаталог"),
    CONTACTS_BUTTON(":envelope_with_arrow:\nКонтакты"),
    EXIT_BUTTON(":x:\nЗавершить"),
    BUY_ONLINE_BUTTON("Купить на сайте"),
    CALCULATE_ON_WEBSITE_BUTTON("Рассчитать");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return EmojiParser.parseToUnicode(buttonName);
    }
}
