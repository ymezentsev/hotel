package com.robot.hotel.guest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public GuestDto save(@Valid @RequestBody GuestRequest guestRequest) {
        return guestService.save(guestRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guest by id")
    public GuestDto findById(@PathVariable Long id) {
        return guestService.findById(id);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get guest by email")
    public GuestDto findByEmail(@PathVariable String email) {
        return guestService.findByEmail(email);
    }

    @GetMapping("/telNumber/{telNumber}")
    @Operation(summary = "Get guest by tel. number")
    public GuestDto findByTelNumber(@PathVariable String telNumber) {
        return guestService.findByTelNumber(telNumber);
    }

    @GetMapping("/passport/{passportSerialNumber}")
    @Operation(summary = "Get guest by passport number")
    public GuestDto findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return guestService.findByPassportSerialNumber(passportSerialNumber);
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
    public void update(@PathVariable Long id, @Valid @RequestBody GuestRequest guestRequest) {
        guestService.update(id, guestRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete guest")
    public void deleteById(@PathVariable Long id) {
        guestService.deleteById(id);
    }
}
