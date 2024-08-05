package za.co.foodaways.queries;

public class StoreQueries {
    public static String getAdminStoreID = "FROM Store store WHERE store.storeOwner =: adminId";
}
