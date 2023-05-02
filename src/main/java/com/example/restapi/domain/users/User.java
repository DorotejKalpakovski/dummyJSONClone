
package com.example.restapi.domain.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.restapi.domain.ProfileImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "USERS_TABLE")
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    private String maidenName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String birthDate;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private ProfileImage profileImage;
    private String bloodGroup;
    private Integer height;
    private Double weight;
    private String eyeColor;
    @OneToOne(cascade = CascadeType.ALL)
    private Hair hair = new Hair();
    private String domain;
    private String ip;
    @ManyToOne
    private Address address = new Address();
    private String macAddress;
    private String university;
    @OneToOne(cascade = CascadeType.ALL)
    private Bank bank = new Bank();
    @OneToOne(cascade = CascadeType.ALL)
    private Company company = new Company();
    private String ein;
    private String ssn;
    private String userAgent;

}
