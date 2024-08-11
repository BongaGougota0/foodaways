package za.co.foodaways.model;

import jakarta.persistence.*;
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
    public Integer userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    Roles role;

    // Store Manager Relation
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Store.class, optional = true)
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = true)
    public Store managedStore;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    public Set<Order> orders = new HashSet<>();
}
