
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
    "color",
    "type"
})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Hair {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("color")
    private String color;
    @JsonProperty("type")
    private String type;

}
