package shop.kubanmebel.kubanmebel_bot.bot.constants;

import com.vdurmont.emoji.EmojiParser;

public enum BotMessageEnum {

    HELLO_MESSAGE("*Привет %s!* \t:wave:"
                        + "\nЯ помогу тебе приобрести красивую и современную мебель."),
    NEW_MESSAGE(":point_right: \tНовинки от мебельной фабрики \"Кубань мебель\" %d шт."),
    CONTACTS_MESSAGE("*Мебельная фабрика \"Кубань мебель\"*"
                        + "\n-------------------------------------"
                        + "\n352251, Россия, Краснодарский край,"
                        + "\nОтрадненский район, станица Попутная, ул. Мащенко, 11ж"
                        + "\nhttps://wa.me/79187437554"
                        + "\n(WhatsApp)"
                        + "\nhttps://vk.com/public202117147"
                        + "\n(ВКонтакте)"
                        + "\n:globe_with_meridians:https://kubanmebel.shop"
                        + "\n-------------------------------------"),
    AUTHOR_MESSAGE("*Связь с разработчиком* \t:robot_face:"
                        + "\n-------------------------------------"
                        + "\nromario.panuchi@yandex.ru"
                        + "\nvk.com/id40779361"),
    CHOOSING_CATEGORY_MESSAGE(":white_check_mark: Выбор нужной категории товаров."),
    CHOOSING_SUBCATEGORY_WITH_PRODUCTS_MESSAGE(":point_right: Товары категории \"%s\" %d шт."),
    CHOOSING_SUBCATEGORY_WITH_SUBCATEGORIES(":point_right: Категория \"%s\"."),
    CALCULATION_KITCHEN_MESSAGE(":arrow_upper_right: Узнать стоимость будущей кухни."),
    EXIT_MESSAGE("*До свидания, %s!*"
                        + "\nСпасибо, что воспользовались услугами нашего бота!"
                        + "\nver. 1.1"
                        + "\n-------------------------------------"
                        + "\nСвязь с разработчиком:"
                        + "\nromario.panuchi@yandex.ru"
                        + "\nvk.com/id40779361"),
    DEFAULT_MESSAGE(":point_down:\tВоспользуйтесь основным меню или клавиатурой\t:point_down:");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return EmojiParser.parseToUnicode(message);
    }
}
