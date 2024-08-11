package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name =  "store_user")
@Getter
@Setter
public class StoreUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    public int roleId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    public Set<Order> orders = new HashSet<>();
}
