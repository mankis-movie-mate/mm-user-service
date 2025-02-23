package cz.cvut.moviemate.userservice.dto.mapper;

import cz.cvut.moviemate.userservice.dto.AppUserDetailsDto;
import cz.cvut.moviemate.userservice.dto.AppUserDto;
import cz.cvut.moviemate.userservice.dto.UpdateUserRequest;
import cz.cvut.moviemate.userservice.model.AppUser;
import cz.cvut.moviemate.userservice.model.UserHistory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    @Mapping(target = "roles", expression = "java(rolesToString(appUser))")
    AppUserDto toDto(AppUser appUser);

    @Mapping(target = "roles", expression = "java(rolesToString(appUser))")
    @Mapping(target = "id", expression = "java(appUser.getId())")
    @Mapping(target = "userHistory", source = "userHistory")
    AppUserDetailsDto toDetailsDto(AppUser appUser, UserHistory userHistory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "notBanned", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "userHistory", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateAppUserFromRequest(UpdateUserRequest request, @MappingTarget AppUser appUser);

    default List<String> rolesToString(AppUser appUser) {
        return appUser.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();
    }
}
