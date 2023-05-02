
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
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lat",
    "lng"
})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Coordinates {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;

}
