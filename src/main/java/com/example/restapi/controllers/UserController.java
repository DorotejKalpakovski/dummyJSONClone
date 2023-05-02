package com.example.restapi.controllers;

import com.example.restapi.domain.users.DTO.UserDTO;
import com.example.restapi.mappers.UserMapper;
import com.example.restapi.services.UserService;
import com.example.restapi.utils.UserUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final ObjectMapper mapper;
    private final UserMapper userMapper;

    public UserController(UserService userService, ObjectMapper mapper, UserMapper userMapper) {
        this.userService = userService;
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    @GetMapping({"/", ""})
    @ResponseStatus(HttpStatus.OK)
    public JsonNode getUsers(@RequestParam(defaultValue = "30", name = "limit") Integer limit,
                             @RequestParam(defaultValue = "0", name = "skip") Integer skip,
                             @RequestParam(defaultValue = "", name = "select") List<String> select) {
        List<UserDTO> users = userService.getAllUsers();

        return UserUtils.selectUserFieldsAll(users, mapper, select, limit, skip);
    }

    @GetMapping({"/{id}", "/{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public JsonNode getUser(@RequestParam(defaultValue = "", name = "select") List<String> select,
                            @PathVariable Integer id) {
        UserDTO user = userService.getById(id);

        return UserUtils.selectUserFields(user, mapper, select);
    }

    @GetMapping({"/search", "/search/"})
    @ResponseStatus(HttpStatus.OK)
    public JsonNode searchUsers(@RequestParam(defaultValue = "30", name = "limit") Integer limit,
                                @RequestParam(defaultValue = "0", name = "skip") Integer skip,
                                @RequestParam(defaultValue = "", name = "select") List<String> select,
                                @RequestParam(defaultValue = "", name = "q") String toMatch) {
        List<UserDTO> filteredUsers = userService.getAllUsersDefaultFilter(toMatch);

        return UserUtils.selectUserFieldsAll(filteredUsers, mapper, select, limit, skip);
    }

    @GetMapping({"/filter", "/filter/"})
    @ResponseStatus(HttpStatus.OK)
    public JsonNode filterUsersKeyValue(@RequestParam(defaultValue = "30", name = "limit") Integer limit,
                                        @RequestParam(defaultValue = "0", name = "skip") Integer skip,
                                        @RequestParam(defaultValue = "", name = "select") List<String> select,
                                        @RequestParam(name = "key") String key,
                                        @RequestParam(name = "value") String value) {

        return UserUtils.selectUserFieldsAll(
                userService.getAllUsersWhereKeyEqualsValue(key, value),
                mapper, select, limit, skip);
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@RequestBody UserDTO user){

        return userService.addUser(userMapper.userDTOToUser(user));
    }

    @PutMapping({"/{id}", "/{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@RequestBody UserDTO user, @PathVariable Integer id) {
        System.out.println(mapper.valueToTree(user));

        return userService.updateUser(userMapper.userDTOToUser(user), id);
    }

    @PatchMapping({"/{id}", "/{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO patchUser(@RequestBody JsonNode updatedFields, @PathVariable Integer id) {

        return userService.patchUser(updatedFields, id);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public JsonNode deleteUser(@PathVariable Integer id) {
        UserDTO deletedUser = userService.deleteUser(id);
        JsonNode node = mapper.valueToTree(deletedUser);
        ((ObjectNode) node).put("isDeleted", true);
        ((ObjectNode) node).put("deletedOn", new Date().toString());

        return node;
    }

    @GetMapping({"/{id}/image", "/{id}/image/"})
    public ResponseEntity<InputStreamResource> getImageForUser(@PathVariable Integer id) {
        Map<String, Object> res = userService.getPFPById(id);

        if ((int) res.get("status") == 404) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentLength((Long) res.get("content-length"))
                .contentType(new MediaType((String) res.get("content-type"), (String) res.get("content-subtype")))
                .body(new InputStreamResource((InputStream) res.get("input-stream")));
    }

    @PostMapping({"/{id}/image", "/{id}/image/"})
    @ResponseStatus(HttpStatus.OK)
    public void addImageToUser(@RequestBody MultipartFile image, @PathVariable Integer id){
        userService.changeProfilePicture(image, id);
    }
}
