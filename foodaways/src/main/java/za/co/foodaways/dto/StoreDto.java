package za.co.foodaways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreDto {
    public Integer storeId;
    public String storeName;
    public String storeManager;
    public String storeNumber;
    public String storeLocation;
}
