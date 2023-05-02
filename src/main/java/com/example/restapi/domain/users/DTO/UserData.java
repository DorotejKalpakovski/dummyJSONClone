package com.example.restapi.domain.users.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "users",
        "total",
        "skip",
        "limit"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    @JsonProperty("users")
    private List<UserDTO> users;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("skip")
    private Integer skip;
    @JsonProperty("limit")
    private Integer limit;

}
