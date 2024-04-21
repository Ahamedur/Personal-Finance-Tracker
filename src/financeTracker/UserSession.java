/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

/**
 *
 * @author ahame
 */

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username; // Optional, if you also want to track the username globally

    private UserSession(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static UserSession getInstance(int userId, String username) {
        if (instance == null) {
            instance = new UserSession(userId, username);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void cleanUserSession() {
        userId = 0;
        username = null;
        instance = null; // Destroy instance
    }
}

