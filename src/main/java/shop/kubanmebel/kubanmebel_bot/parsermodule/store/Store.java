package shop.kubanmebel.kubanmebel_bot.parsermodule.store;

import lombok.Data;

import java.util.List;

@Data
public class Store {
    public static final String URL_HOME_PAGE = "https://kubanmebel.shop";
    public static final String URL_MOCLIENTS = "https://moclients.com/quiz/4d84d495";
    public static final String CLASS_NAME_MAIN_MENU_BAR = "a-mega-menu-item__link nav-link js-open-by-click";
    public static final String CLASS_NAME_SUB_CATEGORIES = "a-categories-grid__item a-category-banner col-6 col-md-4 col-12-440width";
    public static final String CLASS_NAME_PRODUCTS = "a-image-box__link";
    public static final String ID_SHOW_MORE_BUTTON = "chd-show-more-button";

    private String linkDetailsPictureStore;
    private byte[] bytesPictureStore;
    private List<Category> categoryList;
}
