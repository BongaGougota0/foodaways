package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.repository.ReservationRepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ReservationService {

    private ReservationRepo reservationRepo;
    private DataIO conn;

    @Autowired
    public ReservationService(ReservationRepo reservationRepo, DataIO conn) {
        this.reservationRepo = reservationRepo;
        this.conn = conn;
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
