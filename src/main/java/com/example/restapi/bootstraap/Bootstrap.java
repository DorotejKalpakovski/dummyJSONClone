package com.example.restapi.bootstraap;

import com.example.restapi.domain.ProfileImage;
import com.example.restapi.domain.users.Address;
import com.example.restapi.domain.users.Company;
import com.example.restapi.domain.users.DTO.UserDTO;
import com.example.restapi.domain.users.DTO.UserData;
import com.example.restapi.domain.users.User;
import com.example.restapi.repositories.AddressRepository;
import com.example.restapi.repositories.UserRepository;
import com.example.restapi.security.domain.UserDetailsImpl;
import com.example.restapi.security.repositories.UserDetailsRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.*;


@Component
public class Bootstrap implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RestTemplate restTemplate;
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder encoder;
    @Value("${file.upload-dir}")
    private String imagesFolder;
    @Value("${profileImage.default.filename}")
    private String profileImageFileName;
    public Bootstrap(UserRepository userRepository,
                     RestTemplate restTemplate,
                     AddressRepository addressRepository,
                     UserDetailsRepository userDetailsRepository, PasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.addressRepository = addressRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        Set<Address> addresses = new HashSet<>();

        UserData userData = restTemplate.getForObject("https://dummyjson.com/users?limit=0", UserData.class);
        userData.getUsers()
                .forEach(user -> {
                    addresses.add(user.getAddress());
                    addresses.add(user.getCompany().getAddress());
                });
        addressRepository.saveAll(addresses);
        userData.getUsers().forEach(user -> {
            user.setAddress(addressRepository.findByAddress(user.getAddress().getAddress()));
            Company company = user.getCompany();
            company.setAddress(addressRepository.findByAddress(company.getAddress().getAddress()));
            user.setCompany(company);
            userRepository.save(userDTOToUser(user));
        });


        userDetailsRepository.save(new UserDetailsImpl("user1",
                encoder.encode("password1"),
                List.of("ROLE_USER")));
        userDetailsRepository.save(new UserDetailsImpl("admin",
                encoder.encode("adminPassword"),
                List.of("ROLE_USER", "ROLE_ADMIN")));
    }

    protected ProfileImage imageUrlToImage(UserDTO user) {
        try {
            URLConnection conn = new URL(user.getImageUrl()).openConnection();

            String path = imagesFolder + "\\user" + user.getId() + "\\" + profileImageFileName;
            File image = new File(path);
            boolean created = image.getParentFile().mkdir();

            if(!created && !image.getParentFile().exists()) {
                throw new RuntimeException("Couldn't create folder");
            }

            try (OutputStream os = Files.newOutputStream(image.toPath())) {

                IOUtils.copy(conn.getInputStream(), os);
            }

            return new ProfileImage(conn.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setProfileImage( imageUrlToImage( userDTO ) );
        user.setId( null );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setMaidenName( userDTO.getMaidenName() );
        user.setAge( userDTO.getAge() );
        user.setGender( userDTO.getGender() );
        user.setEmail( userDTO.getEmail() );
        user.setPhone( userDTO.getPhone() );
        user.setUsername( userDTO.getUsername() );
        user.setPassword( userDTO.getPassword() );
        user.setBirthDate( userDTO.getBirthDate() );
        user.setBloodGroup( userDTO.getBloodGroup() );
        user.setHeight( userDTO.getHeight() );
        user.setWeight( userDTO.getWeight() );
        user.setEyeColor( userDTO.getEyeColor() );
        user.setHair( userDTO.getHair() );
        user.setDomain( userDTO.getDomain() );
        user.setIp( userDTO.getIp() );
        user.setAddress( userDTO.getAddress() );
        user.setMacAddress( userDTO.getMacAddress() );
        user.setUniversity( userDTO.getUniversity() );
        user.setBank( userDTO.getBank() );
        user.setCompany( userDTO.getCompany() );
        user.setEin( userDTO.getEin() );
        user.setSsn( userDTO.getSsn() );
        user.setUserAgent( userDTO.getUserAgent() );

        return user;
    }
}
