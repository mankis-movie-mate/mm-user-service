package cz.cvut.moviemate.userservice.repository;

import cz.cvut.moviemate.userservice.model.Role;
import cz.cvut.moviemate.userservice.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByRole(Role role);
}
