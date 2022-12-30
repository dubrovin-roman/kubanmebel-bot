package shop.kubanmebel.kubanmebel_bot.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Category;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMaker {

    public static InlineKeyboardMarkup getInlineKeyboardWithOneButtonURL(String textButton, String url) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton buyButton = new InlineKeyboardButton();
        buyButton.setText(textButton);
        buyButton.setUrl(url);

        rowInLine.add(buyButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    public static InlineKeyboardMarkup getInlineKeyboardForCategoryWithoutProducts(List<SubCategory> subCategoryList) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (SubCategory subCategory : subCategoryList) {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            String subCategoryName = subCategory.getName();
            String callbackData = subCategory.getCallbackData();
            String subCategoryNameWithEmoji = EmojiParser.parseToUnicode(":heavy_minus_sign:" + subCategoryName);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(subCategoryNameWithEmoji);
            button.setCallbackData(callbackData);
            rowInLine.add(button);
            rowsInLine.add(rowInLine);
        }
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    public static InlineKeyboardMarkup getInlineKeyboardForCatalog(List<Category> categoryList) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine;

        int totalCount = 0;
        int cycleCount = 0;

        while (totalCount != categoryList.size()) {
            rowInLine = new ArrayList<>();
            for (int i = totalCount; i < categoryList.size(); i++) {
                String categoryName = categoryList.get(i).getName();
                String callbackData = categoryList.get(i).getCallbackData();
                String categoryNameWithEmoji = createCategoryNameWithEmoji(categoryName);
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(categoryNameWithEmoji);
                button.setCallbackData(callbackData);
                rowInLine.add(button);
                totalCount++;
                cycleCount++;
                if (cycleCount == 3) {
                    cycleCount = 0;
                    break;
                }
            }
            rowsInLine.add(rowInLine);
        }

        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    private static String createCategoryNameWithEmoji(String name) {
        String categoryName;
        if (name.equals("Новинки"))
            categoryName = ":star:" + name;
        else
            categoryName = ":small_orange_diamond:" + name;
        return EmojiParser.parseToUnicode(categoryName);
    }

}
