package project_classes;

import java.io.Serializable;

public class Rental implements Serializable {

    private int rentalNumber;
    private String dateRental;
    private String dateReturned;
    private double pricePerDay;
    private double totalRental;
    private String custNumber;
    private String vehNumber;

    public Rental(int rentalNumber, String dateRental, String dateReturned, double pricePerDay, double totalRental, String custNumber, String vehNumber) {
        this.rentalNumber = rentalNumber;
        this.dateRental = dateRental;
        this.dateReturned = dateReturned;
        this.pricePerDay = pricePerDay;
        this.totalRental = totalRental;
        this.custNumber = custNumber;
        this.vehNumber = vehNumber;
    }

    public int getRentalNumber() {
        return rentalNumber;
    }

    public void setRentalNumber(int rentalNumber) {
        this.rentalNumber = rentalNumber;
    }

    public String getDateRental() {
        return dateRental;
    }

    public void setDateRental(String dateRental) {
        this.dateRental = dateRental;
    }

    public String getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(String dateReturned) {
        this.dateReturned = dateReturned;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getTotalRental() {
        return totalRental;
    }

    public void setTotalRental(double totalRental) {
        this.totalRental = totalRental;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getVehNumber() {
        return vehNumber;
    }

    public void setVehNumber(String vehNumber) {
        this.vehNumber = vehNumber;
    }

    @Override
    public String toString() {
        return String.format("Rental = {rental number: %d, date rental: %s, date returned: %s," +
                "price per day: %.2f, total rental: %.2f, customer number: %s, vehicle number: %s}",
                rentalNumber, dateRental, dateReturned, pricePerDay, totalRental, custNumber, vehNumber);
    }
}
