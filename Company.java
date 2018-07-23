package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Company {
    private ObservableList<VehicleControl.Vehicle> vehicles;
    private ObservableList<CustomerControl.Customer> customers;
    private ObservableList<ResevationControl.Reservation> reservations;
    private String name;
    private String details;
    Company(){
        vehicles=FXCollections.observableArrayList(getVehiclesfromText());
        customers=FXCollections.observableArrayList(getCustomersfromText());
        if (getReservationsFromText()==null){
            reservations=FXCollections.observableArrayList();
        }else
            reservations=FXCollections.observableArrayList(getReservationsFromText());
    }
    public ArrayList<VehicleControl.Vehicle> getVehiclesfromText() {
            ArrayList<VehicleControl.Vehicle> vehicleArrayList = new ArrayList<>();
            Scanner scanner = new Scanner("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\vehicles");
            while (scanner.hasNextLine()) {
//      每行内容    汽/货车，车牌号，使用年限，车型，燃油类型，价格,状态,引擎容量;
                String[] element = scanner.nextLine().split(",");
                if (element.length!=1){
//              汽车还是货车
                String type=element[0] ;
                if (type.equals("汽车")){
                    vehicleArrayList.add(new VehicleControl.Car(element[1],element[2],Integer.parseInt(element[3]),element[4],Integer.parseInt(element[5]),Integer.parseInt(element[6])));
                }else
                    vehicleArrayList.add(new VehicleControl.Van(element[1],element[2],Integer.parseInt(element[3]),element[4],Integer.parseInt(element[5]),Integer.parseInt(element[6])));
            }}
            return vehicleArrayList;
    }
    public ArrayList<CustomerControl.Customer> getCustomersfromText(){
        ArrayList<CustomerControl.Customer> customersArraylist = new ArrayList<>();
        Scanner scanner = new Scanner("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\customers");
        while (scanner.hasNextLine()) {
//        每一行储存姓名,电话号码，邮件,会员级别
            String[] element = scanner.nextLine().split(",");
            if (element.length!=1){
            customersArraylist.add(new CustomerControl.Customer(element[0],element[1],element[2],element[3]));
            }
        }
        return customersArraylist;
    }
    public ResevationControl.Reservation[] getReservationsFromText(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\reservations.dat"))) {
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ObservableList<VehicleControl.Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ObservableList<VehicleControl.Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ObservableList<CustomerControl.Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ObservableList<CustomerControl.Customer> customers) {
        this.customers = customers;
    }

    public ObservableList<ResevationControl.Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ObservableList<ResevationControl.Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    @Override
    public String toString(){
        return name+","+details;
    }
}
