package com.rexxtechnologies.parkav;

public class User {


    private String user_name, password,updated_by,customer_id,DepartmentId,zone_id,address,city,country,email,phone_number;

    public User(String user_name, String password,String customer_id,String updated_by,String email,String phone_number,String DepartmentId,String zone_id,String address,String city,String country) {

        this.user_name = user_name;
        this.password = password;
        this.customer_id=customer_id;
        this.updated_by=updated_by;
        this.DepartmentId=DepartmentId;

        this.phone_number=phone_number;
        this.email=email;
        this.zone_id=zone_id;
        this.address=address;
        this.city=city;
        this.country=country;
    }



    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    public String getPhone_number(){
        return phone_number;
    }
    public  void setPhone_number(String phone_number){
        this.phone_number=phone_number;
    }

    public String getDepartmentId(){
        return DepartmentId;
    }
    public  void setDepartmentId(String departmentId ){
        this.DepartmentId=departmentId;
    }

    public String getZone_id(){
        return zone_id;
    }
    public  void setZone_id(String zone_id){
        this.zone_id=zone_id;
    }

    public String getAddress(){
        return address;
    }
    public  void setAddress(String address){
        this.address=address;
    }

    public String getCity(){
        return city;
    }
    public  void setCity(String city){
        this.city=city;
    }

    public String getCountry(){
        return country;
    }
    public  void setCountry(String country ){
        this.country=country;
    }



}
