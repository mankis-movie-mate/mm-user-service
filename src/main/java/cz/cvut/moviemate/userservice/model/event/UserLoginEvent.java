package cz.cvut.moviemate.userservice.model.event;
import lombok.Data;
import java.time.Instant;

@Data
public class UserLoginEvent {
    private String userId;
    private String username;
    private String email;
    private Instant timestamp = Instant.now();

    public UserLoginEvent(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}