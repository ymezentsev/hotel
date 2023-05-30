package com.robot.hotel.rest;

import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping()
    public List<GuestDto> findAll() {
        return guestService.findAll();
    }

    @GetMapping("/reservations/{id}")
    public List<GuestDto> findGuestByReservation(@PathVariable Long id) {
        return guestService.findGuestByReservation(id);
    }

    @PostMapping()
    public void save(@RequestBody GuestDto guestsDto) {
        guestService.save(guestsDto);
    }

    @GetMapping("/{id}")
    public Optional<GuestDto> findById(@PathVariable Long id) {
        return Optional.of(guestService.findById(id).orElseThrow());
    }

    @GetMapping("/email/{email}")
    public Optional<GuestDto> findByEmail(@PathVariable String email) {
        return Optional.of(guestService.findByEmail(email).orElseThrow());
    }

    @GetMapping("/telNumber/{telNumber}")
    public Optional<GuestDto> findByTelNumber(@PathVariable String telNumber) {
        return Optional.of(guestService.findByTelNumber(telNumber).orElseThrow());
    }

    @GetMapping("/passport/{passportSerialNumber}")
    public Optional<GuestDto> findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return Optional.of(guestService.findByPassportSerialNumber(passportSerialNumber).orElseThrow());
    }

    @GetMapping("/lastName/{lastName}")
    public List<GuestDto> findByLastName(@PathVariable String lastName) {
        return guestService.findByLastName(lastName);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody GuestDto guestsDto) {
        guestService.update(id, guestsDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        guestService.deleteById(id);
    }
}
