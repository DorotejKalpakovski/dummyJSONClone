
package com.example.restapi.domain.users.DTO;

import com.fasterxml.jackson.annotation.*;
import com.example.restapi.domain.users.Address;
import com.example.restapi.domain.users.Bank;
import com.example.restapi.domain.users.Company;
import com.example.restapi.domain.users.Hair;
import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "firstName",
        "lastName",
        "maidenName",
        "age",
        "gender",
        "email",
        "phone",
        "username",
        "password",
        "birthDate",
        "image",
        "bloodGroup",
        "height",
        "weight",
        "eyeColor",
        "hair",
        "domain",
        "ip",
        "address",
        "macAddress",
        "university",
        "bank",
        "company",
        "ein",
        "ssn",
        "userAgent"
})
@Getter
@Setter
public class UserDTO {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("maidenName")
    private String maidenName;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("birthDate")
    private String birthDate;
    @JsonProperty("image")
    private String imageUrl;
    @JsonProperty("bloodGroup")
    private String bloodGroup;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("weight")
    private Double weight;
    @JsonProperty("eyeColor")
    private String eyeColor;
    @JsonProperty("hair")
    private Hair hair = new Hair();
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("address")
    private Address address = new Address();
    @JsonProperty("macAddress")
    private String macAddress;
    @JsonProperty("university")
    private String university;
    @JsonProperty("bank")
    private Bank bank = new Bank();
    @JsonProperty("company")
    private Company company = new Company();
    @JsonProperty("ein")
    private String ein;
    @JsonProperty("ssn")
    private String ssn;
    @JsonProperty("userAgent")
    private String userAgent;

}
