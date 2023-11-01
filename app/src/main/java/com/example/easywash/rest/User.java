package com.example.easywash.rest;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("first_name")
    private String first_name;

  //  public String getCreated() {
  //      return created;
  //  }

  //  public void setCreated(String created) {
   //     this.created = created;
   // }
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    //private String created;

    public User(String first_name, String last_name, String phone, String email, String password) {
        this.id = "0";
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    public User() {
        this.id = "0";
        this.first_name = "first_name";
        this.last_name = "last_name";
        this.phone = "phone";
        this.email = "email";
        this.password = "password";
    }

    //public String getId() {
    //    return id;
    //}

    //public void setId(String id) {
   //     this.id = id;
   // }

    public String getfirst_name() {
        return first_name;
    }

    public void setfirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
