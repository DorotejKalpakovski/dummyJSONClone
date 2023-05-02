
package com.example.restapi.domain.users;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address",
    "department",
    "name",
    "title"
})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Company {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("address")
    @ManyToOne
    private Address address = new Address();
    @JsonProperty("department")
    private String department;
    @JsonProperty("name")
    private String name;
    @JsonProperty("title")
    private String title;

}
