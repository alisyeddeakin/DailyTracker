package deakin.com.example.healthbot.Models;

import java.io.Serializable;


public class CurrentUser implements Serializable {

    public static User thisUser ;
    public static Boolean loggedIN;
    public static User[] recentSearch;
    public static String searchText = "";

}