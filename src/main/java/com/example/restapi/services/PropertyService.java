package com.example.restapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    @Value("${application.domain}")
    protected String domain;

    public String getDomain() {
        return domain;
    }
}
