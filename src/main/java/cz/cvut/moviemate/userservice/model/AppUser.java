package cz.cvut.moviemate.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Table
@Entity(name = "app_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "AppUser.base",
        attributeNodes = {
                @NamedAttributeNode("userHistory"),
                @NamedAttributeNode("roles")
        }
)
public class AppUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "app_user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "is_not_banned", nullable = false)
    @Builder.Default
    private Boolean notBanned = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", length = 500, columnDefinition = "jsonb")
    private String metadata;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserHistory userHistory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_roles_on_app_user")),
            inverseJoinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_user_roles_on_role"))
    )
    @Builder.Default
    private List<UserRole> roles = new ArrayList<>();

    public void addRole(UserRole... rolesToAdd) {
        if (rolesToAdd != null && rolesToAdd.length > 0) {
            roles.addAll(Arrays.asList(rolesToAdd));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .toList();
    }

    @Override
    public boolean isAccountNonLocked() {
        return notBanned;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
