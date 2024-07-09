package com.robot.hotel.search_criteria.user;

import com.robot.hotel.room.dto.RoomSearchParameters;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.search_criteria.SpecificationProviderManager;
import com.robot.hotel.user.User;
import com.robot.hotel.user.dto.UserSearchParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class UserSpecificationBuilder implements SpecificationBuilder<User> {
    private final SpecificationProviderManager<User> specificationProviderManager;

    @Override
    public Specification<User> build(UserSearchParameters searchParameters) {
        Specification<User> spec = Specification.where(null);

        if (searchParameters != null
                && searchParameters.firstnames() != null
                && searchParameters.firstnames().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("firstname")
                    .getSpecification(searchParameters.firstnames()));
        }

        if (searchParameters != null
                && searchParameters.lastnames() != null
                && searchParameters.lastnames().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("lastname")
                    .getSpecification(searchParameters.lastnames()));
        }

        if (searchParameters != null
                && searchParameters.phoneNumbers() != null
                && searchParameters.phoneNumbers().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("phoneNumber")
                    .getSpecification(searchParameters.phoneNumbers()));
        }

        if (searchParameters != null
                && searchParameters.emails() != null
                && searchParameters.emails().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("email")
                    .getSpecification(searchParameters.emails()));
        }

        if (searchParameters != null
                && searchParameters.roles() != null
                && searchParameters.roles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("role")
                    .getSpecification(searchParameters.roles()));
        }

        if (searchParameters != null
                && searchParameters.passportSerialNumbers() != null
                && searchParameters.passportSerialNumbers().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("passportSerialNumber")
                    .getSpecification(searchParameters.passportSerialNumbers()));
        }

        if (searchParameters != null
                && searchParameters.reservations() != null
                && searchParameters.reservations().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("reservation")
                    .getSpecification(searchParameters.reservations()));
        }

        if (searchParameters != null
                && searchParameters.countryCodes() != null
                && searchParameters.countryCodes().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("countryCode")
                    .getSpecification(searchParameters.countryCodes()));
        }
        return spec;
    }

    @Override
    public Specification<User> build(RoomSearchParameters searchParameters) {
        return null;
    }
}
