package cz.cvut.moviemate.userservice.dto.mapper;

import cz.cvut.moviemate.userservice.dto.UserHistoryDto;
import cz.cvut.moviemate.userservice.model.UserHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserHistoryMapper {

    UserHistoryDto toDto(UserHistory userHistory);
}