
package com.example.restapi.domain.users;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cardExpire",
    "cardNumber",
    "cardType",
    "currency",
    "iban"
})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Bank {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("cardExpire")
    private String cardExpire;
    @JsonProperty("cardNumber")
    private String cardNumber;
    @JsonProperty("cardType")
    private String cardType;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("iban")
    private String iban;

}
