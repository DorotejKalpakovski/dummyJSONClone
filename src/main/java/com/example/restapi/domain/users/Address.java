
package com.example.restapi.domain.users;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address",
    "city",
    "coordinates",
    "postalCode",
    "state"
})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Address {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("coordinates")
    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates = new Coordinates();
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("state")
    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address1 = (Address) o;

        if (this.id != null && address1.getId() != null) {
            return this.id.equals(address1.getId());
        } else {
            return this.getAddress().equals(address1.getAddress());
        }
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

}
