package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
}
