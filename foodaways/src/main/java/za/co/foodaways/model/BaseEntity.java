package za.co.foodaways.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

public class BaseEntity {
    @CreatedDate
    @JsonIgnore
    LocalDateTime createdDate;

    @LastModifiedDate
    @JsonIgnore
    LocalDateTime updatedDate;
}
