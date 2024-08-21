package com.robot.hotel.search_criteria.user.specification_providers;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.user.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PhoneNumberSpecificationProvider implements SpecificationProvider<User> {
    @Override
    public String getKey() {
        return "phoneNumber";
    }

    @Override
    public Specification<User> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> fullPhoneNumPredicates  = Arrays.stream(params)
                    .filter(param -> param.startsWith("+"))
                    .map(param -> criteriaBuilder.like(
                            criteriaBuilder.concat(root.get("country").get("phoneCode"), root.get("phoneNumber")),
                            "%" + param.strip() + "%"))
                    .toList();

            List<Predicate> shortPhoneNumPredicates = Arrays.stream(params)
                    .filter(param -> !param.startsWith("+"))
                    .map(param -> criteriaBuilder.like(root.get("phoneNumber"),
                            "%" + param.strip() + "%"))
                    .toList();

            List<Predicate> allPhoneNumPredicates = new ArrayList<>(fullPhoneNumPredicates);
            allPhoneNumPredicates.addAll(shortPhoneNumPredicates);
            return criteriaBuilder.or(allPhoneNumPredicates.toArray(new Predicate[0]));
        };
    }
}