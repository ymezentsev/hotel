package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import com.robot.hotel.user.dto.UserSearchParameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;

    @GetMapping()
    public Page<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping()
    public UserDto save(@Valid @RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping(value = "/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/search")
    public Page<UserDto> search(UserSearchParameters parameters, Pageable pageable) {
        return userService.search(parameters, pageable);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}