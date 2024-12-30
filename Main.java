package com.rental;

import java.util.*;

// Abstraction
abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable = true;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (vehicleId == null  model == null  baseRentalRate <= 0) {
            throw new IllegalArgumentException("Invalid vehicle details");
        }
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public abstract double calculateRentalCost(int days);
    public abstract boolean isAvailableForRental();

    @Override
    public String toString() {
        return "Vehicle[ID=" + vehicleId + ", Model=" + model + ", Rate=" + baseRentalRate + "]";
    }
}

// Interfaces
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// Inheritance and Encapsulation
class Car extends Vehicle implements Rentable {
    private boolean hasGPS;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasGPS) {
        super(vehicleId, model, baseRentalRate);
        this.hasGPS = hasGPS;
    }

    public boolean hasGPS() {
        return hasGPS;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasGPS) {
            cost += 5.0 * days;
        }
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) {
            throw new IllegalStateException("Car is not available for rental");
        }
        setAvailability(false);
        System.out.println("Car rented by " + customer.getName() + " for " + days + " days.");
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Car has been returned.");
    }
}

class Motorcycle extends Vehicle implements Rentable {
    private boolean hasSideCar;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean hasSideCar) {
        super(vehicleId, model, baseRentalRate);
        this.hasSideCar = hasSideCar;
    }

    public boolean hasSideCar() {
        return hasSideCar;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasSideCar) {
            cost += 10.0 * days;
        }
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) {
            throw new IllegalStateException("Motorcycle is not available for rental");
        }
        setAvailability(false);
        System.out.println("Motorcycle rented by " + customer.getName() + " for " + days + " days.");
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Motorcycle has been returned.");
    }
}

class Truck extends Vehicle implements Rentable {
    private double cargoCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double cargoCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + (cargoCapacity * 2.0 * days);
    }
    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) {
            throw new IllegalStateException("Truck is not available for rental");
        }
        setAvailability(false);
        System.out.println("Truck rented by " + customer.getName() + " for " + days + " days.");
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Truck has been returned.");
    }
}

// Composition
class Customer {
    private String customerId;
    private String name;
    private List<Vehicle> rentalHistory = new ArrayList<>();

    public Customer(String customerId, String name) {
        if (customerId == null || name == null) {
            throw new IllegalArgumentException("Invalid customer details");
        }
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public List<Vehicle> getRentalHistory() {
        return Collections.unmodifiableList(rentalHistory);
    }

    public void addRental(Vehicle vehicle) {
        rentalHistory.add(vehicle);
    }
}

class RentalAgency {
    private List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : fleet) {
            if (vehicle.isAvailableForRental()) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }

    public void processRental(Customer customer, String vehicleId, int days) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                if (vehicle.isAvailableForRental()) {
                    ((Rentable) vehicle).rent(customer, days);
                    customer.addRental(vehicle);
                    return;
                } else {
                    throw new IllegalStateException("Vehicle is not available.");
                }
            }
        }
        throw new NoSuchElementException("Vehicle not found.");
    }
}