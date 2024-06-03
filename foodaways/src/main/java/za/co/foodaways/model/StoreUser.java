package za.co.foodaways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name =  "store_user")
public class StoreUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    private String fullName;
    private String mobileNumber;
    private String email;
    private String password;
    private int roleId;
}
