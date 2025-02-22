package cz.cvut.moviemate.userservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserHistoryDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        LocalDateTime bannedAt
) implements Serializable {
}
