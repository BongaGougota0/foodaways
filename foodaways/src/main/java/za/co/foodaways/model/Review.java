package za.co.foodaways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;

@Entity(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Integer id;
    private int userId;
    private int orderId;
    private String comments;
    @Max(value=5)
    private int rating;
}
