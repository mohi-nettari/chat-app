package com.example.whatsapp.Model;

public class Users {
    String ProfilePic,userName,email,password,userId,lastmessage,status;
     Boolean state;

    public Users(String profilePic, String userName, String email, String password, String userId, String lastmessage , String status) {
        this.userName = userName;
        this.ProfilePic = profilePic;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastmessage = lastmessage;
        this.status = status;
    }

    public Users(String profilePic, String userName, String email, String password, String userId, String lastmessage  , String status , Boolean state) {
        this.userName = userName;
        this.ProfilePic = profilePic;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastmessage = lastmessage;
        this.state = state;
        this.status = status;
    }

    public Users(String userName, String email, String password,String userId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }
    public Users(String userName, String email, String password,String userId,Boolean state) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.state = state;
    }

//    public Users(String userName, String email, String password ) {
//        this.userName = userName;
//        this.email = email;
//        this.password = password;
//        this.userId = userId;
//    }

    public Users(String email,String password ,String userName) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    public Users( ) {

    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }
}
