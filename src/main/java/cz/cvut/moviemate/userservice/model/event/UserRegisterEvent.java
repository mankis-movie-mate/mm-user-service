package cz.cvut.moviemate.userservice.model.event;

import lombok.Data;

import java.util.Date;

@Data
public class UserRegisterEvent {
    private String userId;
    private String username;
    private String email;
    private Date timestamp = new Date();

    public UserRegisterEvent(String email, String username, String userId) {
        this.email = email;
        this.username = username;
        this.userId = userId;
    }
}