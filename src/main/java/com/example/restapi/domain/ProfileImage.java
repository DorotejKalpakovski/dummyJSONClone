package com.example.restapi.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ProfileImage {
    @Id
    @GeneratedValue
    private Long id;
    private String contentType;

    public ProfileImage(String contentType) {
        this.contentType = contentType;
    }
}
