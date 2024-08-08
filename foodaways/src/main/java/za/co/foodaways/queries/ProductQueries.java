package za.co.foodaways.queries;

public class ProductQueries {
    public static String getAllProducts = "FROM Product";
    public static String getProductsByRating = "FROM Product p ORDER BY p.rating DESC";
    public static String getProductsBySales = "FROM Product p ORDER BY p.sales DESC";
    public static String getStoreProductsByManagerId = "FROM Product p WHERE p.storeId=:id";
}
