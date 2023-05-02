package com.example.restapi.mappers;

import com.example.restapi.domain.ProfileImage;
import com.example.restapi.domain.users.DTO.UserDTO;
import com.example.restapi.domain.users.User;
import com.example.restapi.services.PropertyService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected PropertyService propertyService;

    @Mapping(target = "profileImage", source = "imageUrl", qualifiedByName = "convertToUser")
    abstract public User userDTOToUser(UserDTO userDTO);
    @Mapping(target = "imageUrl", expression="java(propertyService.getDomain() + \"/users/\" + id + \"/image\")")
    abstract public UserDTO userToUserDTO(User user, @Context Integer id);

    @Named("convertToUser")
    protected ProfileImage imageUrlToImage(String imageUrl) {
        return new ProfileImage();
    }
}
