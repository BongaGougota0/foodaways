package za.co.foodaways.queries;

public class OrderQueries {
    public static String getCurrentStoreCompletedOrders = "FROM Product p WHERE p.id =: adminId AND (p.status = completed OR p.status = delivered)";
    public static String getCurrentStoreInProgressOrders = "FROM Product p WHERE p.id = adminId AND p.status = in_progress";
    public static String getCurrentStoreCancelledOrders = "FROM Product p WHERE p.id = adminId AND p.status = cancelled";
    public static String getCurrentStoreNewOrders = "FROM Product p WHERE p.id =: adminId";
}
