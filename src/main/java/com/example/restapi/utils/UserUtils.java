package com.example.restapi.utils;

import com.example.restapi.domain.users.DTO.UserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserUtils {

    public static JsonNode selectUserFields(UserDTO user, ObjectMapper mapper, List<String> select) {
        JsonNode userNode = mapper.valueToTree(user);

        if (select.isEmpty()) {
            return userNode;
        }

        if (!select.contains("id")) {
            select.add(0, "id");
        }

        Map<String, JsonNode> map = new LinkedHashMap<>();
        select.forEach(field -> {
            JsonNode node = userNode.get(field);
            if (node != null) {
                map.put(field, node);
            }
        });

        return mapper.valueToTree(map);
    }

    public static JsonNode selectUserFieldsAll(List<UserDTO> users, ObjectMapper mapper, List<String> select,
                                               Integer limit, Integer skip) {

        Map<String, JsonNode> map = new LinkedHashMap<>();
        if (users.size() < limit) {
            limit = users.size();
        }
        limit = limit == 0 ? 100 : limit;

        List<JsonNode> filteredUsers = users.stream()
                .skip(skip)
                .limit(limit)
                .map(user -> selectUserFields(user, mapper, select))
                .collect(Collectors.toList());

        map.put("users", mapper.valueToTree(filteredUsers));
        map.put("total", mapper.valueToTree(users.size()));
        map.put("skip", mapper.valueToTree(skip));
        map.put("limit", mapper.valueToTree(limit));

        return mapper.valueToTree(map);
    }

}
