package cz.cvut.moviemate.userservice.model;

import cz.cvut.moviemate.userservice.exception.InvalidTimestampException;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table
@Entity(name = "user_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHistory implements Serializable {

    @Id
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "banned_at")
    private LocalDateTime bannedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_login_history_on_user")
    )
    private AppUser appUser;

    @PrePersist
    @PreUpdate
    protected void validateTimestamps() {
        if (updatedAt.isBefore(createdAt))
            throw new InvalidTimestampException("Date of updating must be after or equal to date of creation");
        if (deletedAt != null && deletedAt.isBefore(updatedAt))
            throw new InvalidTimestampException("Date of deletion must be after or equal to date of updating");
        if (bannedAt != null && bannedAt.isBefore(updatedAt))
            throw new InvalidTimestampException("Date of ban must be after or equal to date of updating");
    }
}
