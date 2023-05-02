package com.example.restapi.specifications;

import com.example.restapi.domain.users.*;
import com.example.restapi.specifications.criterias.SearchCriteriaKeyValue;
import com.example.restapi.domain.users.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;


public class UserWhereKeyEqualsValueSpec implements Specification<User>{
    private final SearchCriteriaKeyValue criteria;

    public UserWhereKeyEqualsValueSpec(SearchCriteriaKeyValue criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getKey().contains(".")) {
            String[] parts = criteria.getKey().split("\\.");

            switch (parts[0]) {
                case "hair":
                    Join<User, Hair> hairJoin = root.join("hair");
                    return criteriaBuilder.equal(hairJoin.get(parts[1]), criteria.getValue());
                case "address":
                    Join<User, Address> addressJoin = root.join("address");
                    if (parts.length == 2) {

                        return criteriaBuilder.equal(addressJoin.get(parts[1]), criteria.getValue());
                    } else {

                        Join<Join<User, Address>, Coordinates> coordinatesJoin = addressJoin.join("coordinates");
                        return criteriaBuilder.equal(coordinatesJoin.get(parts[2]), criteria.getValue());
                    }
                case "bank":
                    Join<User, Bank> bankJoin = root.join("bank");
                    return criteriaBuilder.equal(bankJoin.get(parts[1]), criteria.getValue());
                case "company":
                    Join<User, Company> companyJoin = root.join("company");
                    if (parts.length == 2) {
                        return criteriaBuilder.equal(companyJoin.get(parts[1]), criteria.getValue());
                    } else {
                        Join<Company, Address> companyAddressJoin = companyJoin.join("address");
                        if (parts.length == 3) {
                            return criteriaBuilder.equal(companyAddressJoin.get(parts[2]), criteria.getValue());
                        } else {
                            Join<Address, Coordinates> companyAddressCoordinatesJoin =
                                    companyAddressJoin.join("coordinates");
                            return criteriaBuilder.equal(companyAddressCoordinatesJoin.get(parts[3]), criteria.getValue());
                        }
                    }
            }
        }
        return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
    }

}
