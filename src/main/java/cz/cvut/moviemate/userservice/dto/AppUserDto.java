package cz.cvut.moviemate.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class AppUserDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private List<String> roles;
    private Boolean enabled;
    private Boolean notBanned;
}