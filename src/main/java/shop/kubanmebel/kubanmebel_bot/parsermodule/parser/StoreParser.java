package shop.kubanmebel.kubanmebel_bot.parsermodule.parser;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import shop.kubanmebel.kubanmebel_bot.parsermodule.Parser;
import shop.kubanmebel.kubanmebel_bot.parsermodule.store.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
@Slf4j
public class StoreParser implements Parser {
    private Store store;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36";
    private static final String REFERRER = "http://www.google.com";

    public static final List<String> UNDESIRABLE_CATEGORIES = List.of("Акции", "Хиты продаж");

    public StoreParser() {
        this.store = new Store();
    }

    public void parsing() {
        try {
            Document documentHomePage = Jsoup.connect(Store.URL_HOME_PAGE).userAgent(USER_AGENT).referrer(REFERRER).get();
            Elements elementsMainMenuBar = documentHomePage.getElementsByClass(Store.CLASS_NAME_MAIN_MENU_BAR);
            String linkPictureStore = getLinkMainPicture(documentHomePage);
            store.setLinkDetailsPictureStore(linkPictureStore);
            store.setBytesPictureStore(createArrayBytesOfPicture(linkPictureStore));
            store.setCategoryList(createListCategories(elementsMainMenuBar));
            getAllSubCategoriesAndSet(store.getCategoryList());
            getAllProductsAndSet(store.getCategoryList());
        } catch (IOException e) {
            log.error("Неудалось получить доступ к основной странице по ссылке. " + e.getMessage());
        }
    }

    private String getLinkMainPicture(Document documentHomePage) {
        String link = "link not found";
        try {
            Element element = documentHomePage.getElementsByClass("a-logo__link").first().firstElementChild();
            link = element.attr("src");
        } catch (NullPointerException e) {
            log.error("Не получилось получить ссылку на основной логотип магазина. " + e.getMessage());
        }
        return link;
    }

    private List<Category> createListCategories(Elements elementsMainMenuBar) {
        Map<String, String> namesAndLinksCategories = getNamesAndLinksCategoriesFromElementsMainMenuBar(elementsMainMenuBar);
        List<String> namesCategories = getCurrentNamesCategoriesFromElementsMainMenuBar(elementsMainMenuBar);
        List<Category> categoryList = new ArrayList<>();
        for (String name : namesCategories) {
            Category categoryTemp = new Category();
            categoryTemp.setName(name);
            categoryTemp.setCallbackData(name);
            categoryTemp.setLink(namesAndLinksCategories.get(name));
            categoryList.add(categoryTemp);
        }
        return categoryList;
    }

    private Map<String, String> getNamesAndLinksCategoriesFromElementsMainMenuBar(Elements elementsMainMenuBar) {
        Map<String, String> namesAndLinksCategories = new HashMap<>();
        for (Element element : elementsMainMenuBar) {
            String nameCategory = makeNameWithCapitalLetter(element.child(1).text().toLowerCase());
            namesAndLinksCategories.put(nameCategory, element.attr("href"));
        }
        return namesAndLinksCategories;
    }

    private List<String> getCurrentNamesCategoriesFromElementsMainMenuBar(Elements elementsMainMenuBar) {
        List<String> namesCategories = new ArrayList<>();
        for (Element element : elementsMainMenuBar) {
            String nameCategory = makeNameWithCapitalLetter(element.child(1).text().toLowerCase());
            if (!UNDESIRABLE_CATEGORIES.contains(nameCategory))
                namesCategories.add(nameCategory);
        }
        return namesCategories;
    }

    private String makeNameWithCapitalLetter(String name) {
        char[] chars = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean isFirstSymbol = true;
        for (Character ch : chars) {
            if (isFirstSymbol) {
                builder.append(ch.toString().toUpperCase());
                isFirstSymbol = false;
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    private void getAllSubCategoriesAndSet(List<? extends AbstractCategory> list) {
        list.forEach(category -> {
            try {
                Document documentCategory = Jsoup.connect(category.getLink()).userAgent(USER_AGENT).referrer(REFERRER).get();
                if (existSubCategoriesElementsInDocument(documentCategory)) {
                    Elements elementsCategories = documentCategory.getElementsByClass(Store.CLASS_NAME_SUB_CATEGORIES);
                    List<SubCategory> categoryList = new ArrayList<>();
                    for (Element element : elementsCategories) {
                        SubCategory subCategory = new SubCategory();
                        subCategory.setName(element.attr("title"));
                        int d = (int) (Math.random() * 1_000_000_000);
                        String data = String.valueOf(d);
                        subCategory.setCallbackData(data);
                        subCategory.setLink(element.attr("href"));
                        categoryList.add(subCategory);
                    }
                    getAllSubCategoriesAndSet(categoryList);
                    category.setSubCategoryList(categoryList);
                }
            } catch (IOException e) {
                log.error("Неудалось получить доступ к подкатегории по ссылке. " + e.getMessage());
            }
        });
    }

    private void getAllProductsAndSet(List<? extends AbstractCategory> list) {
        list.forEach(category -> {
            try {
                if (category.isSubCategoryListEmpty()) {
                    Document documentCategory = Jsoup.connect(category.getLink()).userAgent(USER_AGENT).referrer(REFERRER).get();
                    if (existProductsElementsInDocument(documentCategory)) {
                        if (existShowMoreButton(documentCategory)) {
                            category.setProductList(createProductsWhenExistShowMoreButton(category.getLink(), 1));
                        } else {
                            category.setProductList(createProducts(documentCategory));
                        }
                    }
                } else {
                    getAllProductsAndSet(category.getSubCategoryList());
                }
            } catch (IOException e) {
                log.error("Неудалось получить доступ к товару по ссылке. " + e.getMessage());
            }
        });
    }

    private List<Product> createProductsWhenExistShowMoreButton(String categoryLink, int numberPage) throws IOException {
        int currentPageNumber = numberPage;
        String currentCategoryLink = categoryLink + "?page=" + currentPageNumber;
        Document document = Jsoup.connect(currentCategoryLink).userAgent(USER_AGENT).referrer(REFERRER).get();
        List<Product> result = new ArrayList<>(createProducts(document));
        if (existShowMoreButton(document)) {
            currentPageNumber++;
            result.addAll(createProductsWhenExistShowMoreButton(categoryLink, currentPageNumber));
        }
        return result;
    }

    private boolean existSubCategoriesElementsInDocument(Document document) {
        return !document.getElementsByClass(Store.CLASS_NAME_SUB_CATEGORIES).isEmpty();
    }

    private boolean existProductsElementsInDocument(Document document) {
        return !document.getElementsByClass(Store.CLASS_NAME_PRODUCTS).isEmpty();
    }

    private boolean existShowMoreButton(Document document) {
        return !document.getElementsByAttributeValue("id", Store.ID_SHOW_MORE_BUTTON).isEmpty();
    }

    private List<Product> createProducts(Document document) {
        Elements elementsProducts = document.getElementsByClass(Store.CLASS_NAME_PRODUCTS);
        List<Product> productList = new ArrayList<>();
        for (Element element : elementsProducts) {
            try {
                String linkDetailsProduct = element.attr("href");
                Product product = new Product();
                product.setNameProduct(element.attr("title"));
                product.setLinkDetailsProduct(linkDetailsProduct);
                Document documentProduct = Jsoup.connect(linkDetailsProduct).userAgent(USER_AGENT).referrer(REFERRER).get();
                Element elementPicture = documentProduct.getElementsByClass("a-image-item__img js-product-img").first();
                String linkPicture = elementPicture.attr("srcset");
                String currentLinkPicture = linkPicture.substring(0, linkPicture.lastIndexOf(" "));
                product.setBytesPicture(createArrayBytesOfPicture(currentLinkPicture));
                product.setLinkDetailsPictureProduct(currentLinkPicture);
                Element elementArticleNumber = documentProduct.getElementsByClass("a-list__value code").first();
                product.setArticleNumber(elementArticleNumber.text());
                Element elementPrice = documentProduct.getElementsByAttributeValue("id", "mainPrice").first();
                product.setPrice(elementPrice.text());
                productList.add(product);
            } catch (IOException | NullPointerException e) {
                log.error("Неудалось получить доступ к товару по ссылке или подэлементу. " + e.getMessage());
            }
        }
        return productList;
    }

    private byte[] createArrayBytesOfPicture(String linkPicture) {
        byte[] bytes = null;
        try (InputStream in = new BufferedInputStream(new URL(linkPicture).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int n;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            bytes = out.toByteArray();
        } catch (IOException e) {
            log.error("Неудалось скачать картинку. " + e.getMessage());
        }
        return bytes;
    }
}
