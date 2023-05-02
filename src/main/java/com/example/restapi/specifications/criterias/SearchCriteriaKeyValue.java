package com.example.restapi.specifications.criterias;

import lombok.Getter;

@Getter
public class SearchCriteriaKeyValue {
    private String key;
    private String value;

    public SearchCriteriaKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
