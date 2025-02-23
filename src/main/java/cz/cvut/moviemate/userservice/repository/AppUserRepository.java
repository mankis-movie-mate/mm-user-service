package cz.cvut.moviemate.userservice.repository;

import cz.cvut.moviemate.userservice.model.AppUser;
import cz.cvut.moviemate.userservice.model.Role;
import cz.cvut.moviemate.userservice.model.UserRole;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    @EntityGraph(value = "AppUser.base", type = EntityGraph.EntityGraphType.FETCH)
    Page<AppUser> findAll(Specification<AppUser> spec, Pageable pageable);

    @EntityGraph(value = "AppUser.base", type = EntityGraph.EntityGraphType.FETCH)
    Optional<AppUser> findByUsername(String username);

    @EntityGraph(value = "AppUser.base", type = EntityGraph.EntityGraphType.FETCH)
    Optional<AppUser> findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT soft_delete_app_user(:id)")
    void softDeleteUserById(Long id);

    default Specification<AppUser> usersWithoutRoles(List<Role> excludedRoles) {
        return (root, query, criteriaBuilder) -> {
            if (excludedRoles == null || excludedRoles.isEmpty()) return criteriaBuilder.conjunction();

            // Create a subquery for excluded roles
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<AppUser> subqueryRoot = subquery.from(AppUser.class);
            Join<AppUser, UserRole> rolesJoin = subqueryRoot.join("roles");

            // Select user IDs where roles match any in the excludedRoles list
            subquery.select(subqueryRoot.get("id"))
                    .where(rolesJoin.get("role").in(excludedRoles));

            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }
}
