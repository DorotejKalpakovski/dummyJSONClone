package com.example.restapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.restapi.domain.users.DTO.UserDTO;
import com.example.restapi.domain.users.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getById(Integer id);
    List<UserDTO> getAllUsersWhereKeyEqualsValue(String key, String value);
    List<UserDTO> getAllUsersDefaultFilter(String value);
    UserDTO addUser(User userToAdd);
    UserDTO updateUser(User user, Integer id);
    UserDTO deleteUser(Integer id);
    UserDTO patchUser(JsonNode updatedFields, Integer id);
    Map<String, Object> getPFPById(Integer id);
    void changeProfilePicture(MultipartFile image, Integer id);
}
