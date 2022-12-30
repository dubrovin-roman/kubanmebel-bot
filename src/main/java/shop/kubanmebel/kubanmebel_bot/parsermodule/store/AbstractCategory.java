package shop.kubanmebel.kubanmebel_bot.parsermodule.store;

import java.util.List;

public abstract class AbstractCategory {
    private String name;
    private String link;
    private String callbackData;
    private List<SubCategory> subCategoryList;
    private List<Product> productList;

    public AbstractCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public boolean isSubCategoryListEmpty() {
        return getSubCategoryList() == null;
    }

    public boolean isProductListEmpty() {
        return getProductList() == null;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
