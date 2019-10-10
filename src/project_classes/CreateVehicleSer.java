package project_classes;

import java.util.*;
import javax.swing.*;
import java.io.*;

public class CreateVehicleSer
{
    private ObjectOutputStream outFile;
    Vehicle vehicleObj;
    
    public void openFile()
    {
        try
        {
            outFile = new ObjectOutputStream(new FileOutputStream("Vehicles.ser"));
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println("File not found...");
            System.exit(1);
        }
        catch(IOException ioe){
            System.out.println("Err opening output file");
        }              
    }
    
    public void writeObjects()
    {
            try
            {
                vehicleObj = new Vehicle(1,"Audi A8", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(2, "BMW 750iL", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(3,"Maserati GT", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(4,"Audi S6", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(5,"Audi Q7", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(6,"Range Rover Evoque", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(7,"BMW X5", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(8,"Jaguar F-Pace", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(9,"Mercedes E500", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(10,"Volvo XC60", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(11,"BMW 540i", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(12,"Volvo XC90", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(13,"Mercedes S500", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(14,"Mercedes GLE300", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(15,"Lexus LS460", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle(16,"LExus RX350", 2, 500, true);
                outFile.writeObject(vehicleObj);
            }
            catch(IOException ioe){
                System.out.println("Err writing to file");
            }
             
        }
        
    
    
    public void closeFile()
    {
        try{
            outFile.close();
        }
        catch(IOException ioe){
            System.out.println("Err closing file");
        }
        
    }  
}
