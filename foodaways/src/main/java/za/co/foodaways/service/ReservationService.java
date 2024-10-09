package za.co.foodaways.service;

import org.springframework.stereotype.Service;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.repository.ReservationRepo;

@Service
public class ReservationService {

    private ReservationRepo reservationRepo;

    public ReservationService(ReservationRepo reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    public void addReservation(Reservation reservation){
        reservationRepo.save(reservation);
//        Statement stmt = null;
//        try {
//            stmt = this.conn.getStatement();
//            System.out.println("Data to write "+reservation.toString());
//            stmt.executeQuery("INSERT INTO reservation VALUES "+reservation.toString());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
}
