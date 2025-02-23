package cz.cvut.moviemate.userservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserDetailsDto extends AppUserDto implements Serializable {
    private UserHistoryDto userHistory;
}