package ecocupfirebase.ecocup;

import com.google.firebase.database.Exclude;

public class Users {

    public String Username;

    public Users() { }

    @Exclude
    public String getUsername() {
        return Username;
    }
    @Exclude
    public void setUsername(String username) {
        Username = username;
    }
    public Users(String username) {
        Username = username;
    }
}
