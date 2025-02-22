package cz.cvut.moviemate.userservice.service;

import cz.cvut.moviemate.userservice.model.AppUser;
import cz.cvut.moviemate.userservice.model.Role;
import cz.cvut.moviemate.userservice.model.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface InternalAppUserService extends UserDetailsService {

    AppUser findUserByUsername(String username);

    AppUser findUserByEmail(String email);

    AppUser save(AppUser appUser);

    UserRole findUserRoleByRole(Role role);

    UserRole saveRole(UserRole role);
}
