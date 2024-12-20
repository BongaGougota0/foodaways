package za.co.foodaways.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StoreUserDto {
    public Integer userId;
    public String fullName;
    public String phoneNumber;
    public String email;
    public int role;
}
