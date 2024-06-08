package za.co.foodaways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReservationComments() {
        return reservationComments;
    }

    public void setReservationComments(String reservationComments) {
        this.reservationComments = reservationComments;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public int id;
    public String fullName;
    public String emailAddress;
    public String phoneNumber;
    public int personCount;
    public String date;

    public String time;

    public String reservationComments;

    @Override
    public String toString() {
        return "(" + fullName + ','+ emailAddress + ',' +phoneNumber + ','+ personCount +
                "," + date + ',' + time +"," + reservationComments + ')';
    }
}
