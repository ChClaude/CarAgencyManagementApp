package project_classes;

import java.io.*;
public class Vehicle implements Serializable
{
    private int vehNumber;
    private String make;
    private String category;
    private double rentalPrice;
    private boolean availableForRent;
    
    //default constructor
    public Vehicle()
    {
        
    }
    
    //constructor that takes 4 arguments to initialize the instance variables
    public Vehicle(int vn, String m, int cat, double amt, boolean avail)
    {
        setVehNumber(vn);
        setMake(m);
        setCategory(cat);
        setAvailableForRent(avail);
        setRentalPrice(amt);
    }
    public Vehicle(int vn, String m, int cat)
    {
        setVehNumber(vn);
        setMake(m);
        setCategory(cat);
        setAvailableForRent(true);
    }
    
    // set methods

    public void setVehNumber(int vehNumber) {
        this.vehNumber = vehNumber;
    }
    public void setMake(String m)
    {
        make = m;
    }
    public void setRentalPrice(double amt)
    {
        rentalPrice = amt;
    }
    
    public void setCategory(int sCategory)
    {
        switch(sCategory)
        {
            case 1:
                category = "Sedan";
                rentalPrice = 450;
                break;
            case 2:
                category = "SUV";
                rentalPrice = 500;
                break;
        }
    }
    
    public void setAvailableForRent(boolean a)
    {
        availableForRent = a;
    }
    
    
    //get methods

    public int getVehNumber() {
        return vehNumber;
    }

    public String getMake()
    {
        return make;
    }
    
    public String getCategory()
    {
        return category;
    }
    public double getRentalPrice()
    {
        return rentalPrice;
    }
      
    //checks if the vehicle is available
    public boolean isAvailableForRent()
    {
        return availableForRent;
    }
       
    public String toString()
    {
        return String.format("%-35s\t%-10s\t%.2f\t\t%-6s",getMake(),getCategory(), getRentalPrice(), new Boolean(isAvailableForRent()).toString());
    }
}
