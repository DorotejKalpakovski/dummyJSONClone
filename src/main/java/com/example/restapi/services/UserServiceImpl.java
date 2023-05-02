package com.example.restapi.services;

import com.example.restapi.repositories.AddressRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.restapi.domain.ProfileImage;
import com.example.restapi.domain.users.Address;
import com.example.restapi.domain.users.DTO.UserDTO;
import com.example.restapi.domain.users.User;
import com.example.restapi.exceptions.NotFoundException;
import com.example.restapi.mappers.UserMapper;
import com.example.restapi.repositories.UserRepository;
import com.example.restapi.specifications.UserDefaultFilterSpec;
import com.example.restapi.specifications.criterias.SearchCriteriaKeyValue;
import com.example.restapi.specifications.UserWhereKeyEqualsValueSpec;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Value("${file.upload-dir}")
    private String imagesFolder;
    @Value("${profileImage.default.path}")
    private String defaultImageSrc;
    @Value("${profileImage.default.filename}")
    private String profileImageName;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ObjectMapper mapper;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ObjectMapper mapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map(user -> userMapper.userToUserDTO(user, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Integer id) {
        return userMapper.userToUserDTO(userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException();
        }), id);
    }

    @Override
    public List<UserDTO> getAllUsersWhereKeyEqualsValue(String key, String value) {

        try {
            UserWhereKeyEqualsValueSpec spec = new UserWhereKeyEqualsValueSpec(new SearchCriteriaKeyValue(key, value));
            return userRepository.findAll(spec).stream().map(user -> userMapper.userToUserDTO(user, user.getId())).collect(Collectors.toList());
        } catch (InvalidDataAccessApiUsageException e) {
            System.out.println("Key: " + key + " does not exist in User class");
            return new ArrayList<>();
        } catch (JpaSystemException e) {
            System.out.println("Value: " + value + " is the wrong format");
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserDTO> getAllUsersDefaultFilter(String value) {
        UserDefaultFilterSpec spec = new UserDefaultFilterSpec('%' + value + '%');

        return userRepository.findAll(spec)
                .stream().map(user -> userMapper.userToUserDTO(user, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO addUser(User userToAdd){
        Address userAddress = addressRepository.findByAddress(userToAdd.getAddress().getAddress());
        Address companyAddress = addressRepository.findByAddress(userToAdd.getCompany().getAddress().getAddress());
        if (userAddress == null) {
            userAddress = addressRepository.save(userToAdd.getAddress());
        }
        if (companyAddress == null) {
            companyAddress = addressRepository.save(userToAdd.getCompany().getAddress());
        }

        File defaultImage = new File(defaultImageSrc);

        userToAdd.setAddress(userAddress);
        userToAdd.getCompany().setAddress(companyAddress);
        userToAdd.setProfileImage(new ProfileImage(URLConnection.guessContentTypeFromName(defaultImage.getName())));

        User user = userRepository.save(userToAdd);

        File userFolder = new File(imagesFolder + "\\user" + user.getId());

        if (!userFolder.mkdir()) {
            this.deleteUser(user.getId());
            throw new RuntimeException("Could not create folder for new User");
        }

        try {
            IOUtils.copy(Files.newInputStream(defaultImage.toPath()),
                    Files.newOutputStream(userFolder.toPath().resolve(profileImageName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return userMapper.userToUserDTO(user, user.getId());
    }

    @Override
    public UserDTO updateUser(User user, Integer id) {
        Address userAddress = addressRepository.findByAddress(user.getAddress().getAddress());
        Address companyAddress = addressRepository.findByAddress(user.getCompany().getAddress().getAddress());
        if (userAddress == null) {
            userAddress = addressRepository.save(user.getAddress());
        }
        if (companyAddress == null) {
            companyAddress = addressRepository.save(user.getCompany().getAddress());
        }

        user.setAddress(userAddress);
        user.getCompany().setAddress(companyAddress);
        user.setId(id);

        User savedUser = userRepository.save(user);

        return userMapper.userToUserDTO(savedUser, savedUser.getId());
    }

    @Override
    public UserDTO deleteUser(Integer id) {
        UserDTO user = this.getById(id);
        userRepository.deleteById(id);

        return user;
    }

    @Override
    public UserDTO patchUser(JsonNode updatedFields, Integer id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(RuntimeException::new);
        Iterator<Map.Entry<String, JsonNode>> fields = updatedFields.fields();
        PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(userToUpdate);

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (entry.getKey().equals("id")) continue;

            try {
                Field field = User.class.getDeclaredField(entry.getKey());
                accessor.setPropertyValue(entry.getKey(), mapper.convertValue(entry.getValue(), field.getType()));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        User savedUser = userRepository.save(userToUpdate);
        return userMapper.userToUserDTO(savedUser, savedUser.getId());
    }

    @Override
    public Map<String, Object> getPFPById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        ProfileImage profileImageObj = userRepository.findById(id).orElseThrow(RuntimeException::new).getProfileImage();

        File imageFile = new File(Paths.get(imagesFolder + "\\user" + id).resolve(profileImageName).toString());
        map.put("content-length", imageFile.length());
        try {
            map.put("input-stream", new DataInputStream(new FileInputStream(imageFile)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        map.put("content-type", profileImageObj.getContentType().split("/")[0]);
        map.put("content-subtype", profileImageObj.getContentType().split("/")[1]);
        map.put("status", 200);

        return map;
    }

    @Override
    public void changeProfilePicture(MultipartFile image, Integer id) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        String contentType = image.getContentType();
        try {
            IOUtils.copy(image.getInputStream(), Files.newOutputStream(Paths.get(imagesFolder + "\\user" + id)
                    .resolve(profileImageName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setProfileImage(new ProfileImage(contentType));
        userRepository.save(user);
    }

}
