package com.robot.hotel.search_criteria.room;

import com.robot.hotel.room.Room;
import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.search_criteria.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomSpecificationProviderManager implements SpecificationProviderManager<Room> {
    private final List<SpecificationProvider<Room>> requestSpecificationProviders;

    @Override
    public SpecificationProvider<Room> getSpecificationProvider(String key) {
        return requestSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can't find correct specification provider for key " + key));
    }
}