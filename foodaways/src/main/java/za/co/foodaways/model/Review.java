package za.co.foodaways.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int reviewId;
    private int userId;
    private int orderId;
    private String comments;
    @Max(value=5)
    private int rating;

    @ManyToMany(mappedBy = "reviews", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    public Set<Product> productReview = new HashSet<>();
}
