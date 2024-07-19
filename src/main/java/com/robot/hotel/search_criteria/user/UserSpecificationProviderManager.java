package com.robot.hotel.search_criteria.user;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.search_criteria.SpecificationProviderManager;
import com.robot.hotel.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSpecificationProviderManager implements SpecificationProviderManager<User> {
    private final List<SpecificationProvider<User>> requestSpecificationProviders;

    @Override
    public SpecificationProvider<User> getSpecificationProvider(String key) {
        return requestSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can't find correct specification provider for key " + key));
    }
}