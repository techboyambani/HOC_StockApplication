package com.hoc.stockapp.Model;

public class UserInformation {
    private String email, organization, password;
    public UserInformation(){

    }
    public UserInformation(String email, String organization, String password){
        this.email = email;
        this.organization = organization;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.email = organization; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.email = password; }
}
