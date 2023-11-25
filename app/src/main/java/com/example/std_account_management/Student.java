package com.example.std_account_management;

public class Student {

    private String name, phone, status, password, email;
    private int age;
    public Student(){

    }

    public Student(String email, String name, int age, String phone, String status, String password) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.status = status;
        this.password = password;
    }

    // getter

    public String getEmail() {return email;}

    public String getName() {return this.name;}
    public int getAge() {return this.age;}
    public String getPhone(){return this.phone;}
    public String getStatus() {return this.status;}
    public String getPass() {return this.password;}

    // setter
    public void setEmail(String email) {this.email = email;}

    public void setName(String name) {this.name = name;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setStatus(String status) {this.status = status;}
    public void setAge(int age) {this.age = age;}
    public void setPass(String pass) {this.password = pass;}
}
