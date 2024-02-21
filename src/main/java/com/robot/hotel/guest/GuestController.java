package com.robot.hotel.guest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
@Tag(name = "Guests", description = "API to work with Guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping()
    @Operation(summary = "Get all guests")
    public List<GuestDto> findAll() {
        return guestService.findAll();
    }

    @PostMapping()
    @Operation(summary = "Create new guest")
    public void save(@RequestBody GuestDto guestsDto) {
        guestService.save(guestsDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guest by id")
    public Optional<GuestDto> findById(@PathVariable Long id) {
        return Optional.of(guestService.findById(id).orElseThrow());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get guest by email")
    public Optional<GuestDto> findByEmail(@PathVariable String email) {
        return Optional.of(guestService.findByEmail(email).orElseThrow());
    }

    @GetMapping("/telNumber/{telNumber}")
    @Operation(summary = "Get guest by tel. number")
    public Optional<GuestDto> findByTelNumber(@PathVariable String telNumber) {
        return Optional.of(guestService.findByTelNumber(telNumber).orElseThrow());
    }

    @GetMapping("/passport/{passportSerialNumber}")
    @Operation(summary = "Get guest by passport number")
    public Optional<GuestDto> findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return Optional.of(guestService.findByPassportSerialNumber(passportSerialNumber).orElseThrow());
    }

    @GetMapping("/lastName/{lastName}")
    @Operation(summary = "Get guests by lastname")
    public List<GuestDto> findByLastName(@PathVariable String lastName) {
        return guestService.findByLastName(lastName);
    }

    @GetMapping("/reservations/{id}")
    @Operation(summary = "Get guests by reservation")
    public List<GuestDto> findGuestByReservation(@PathVariable Long id) {
        return guestService.findGuestByReservation(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update guest")
    public void update(@PathVariable Long id, @RequestBody GuestDto guestsDto) {
        guestService.update(id, guestsDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete guest")
    public void deleteById(@PathVariable Long id) {
        guestService.deleteById(id);
    }
}
