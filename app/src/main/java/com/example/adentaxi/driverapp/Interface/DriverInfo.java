package com.example.adentaxi.driverapp.Interface;


public class DriverInfo {
    private String name;
    private String email;
    private String phone;
    private String car_name;
    private String password;
    private String driver_active;
    private Double driverLatitude;
    private Double driverLongitude;

    public DriverInfo() {
    }

    public DriverInfo(String name, String email, String phone, String car_name, String password, String driver_active, Double driverLatitude, Double driverLongitude) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.car_name = car_name;
        this.password = password;
        this.driver_active = driver_active;
        this.driverLatitude = driverLatitude;
        this.driverLongitude = driverLongitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver_active() {
        return driver_active;
    }

    public void setDriver_active(String driver_active) {
        this.driver_active = driver_active;
    }

    public Double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(Double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public Double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(Double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }
}

