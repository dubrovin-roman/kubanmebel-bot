package shop.kubanmebel.kubanmebel_bot.parsermodule.store;

import lombok.Data;

@Data
public class Product {
    private String linkDetailsPictureProduct;
    private String nameProduct;
    private String articleNumber;
    private String price;
    private String linkDetailsProduct;
    private byte[] bytesPicture;
}
