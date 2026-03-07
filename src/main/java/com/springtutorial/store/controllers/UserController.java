package com.springtutorial.store.controllers;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;
import org.springframework.web.util.UriComponentsBuilder;

import com.springtutorial.store.dtos.ChangePasswordRequest;
import com.springtutorial.store.dtos.RegisterUserRequest;
import com.springtutorial.store.dtos.UpdateUserRequest;
import com.springtutorial.store.dtos.UserDto;
import com.springtutorial.store.mappers.UserMapper;
import com.springtutorial.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    private Iterable<UserDto> getAllUsers(
        @RequestParam (required = false, defaultValue ="", name = "sort") String sortBy){
        if(!Set.of("name", "email").contains(sortBy)){
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy))
        .stream()
        .map(userMapper::toDto)
        .toList();
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);

        if(user == null){
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userMapper.toDto(user));
        
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser( @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriComponentsBuilder) 
    {
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
    @PathVariable(name = "id") Long id, 
    @RequestBody UpdateUserRequest updateUserRequest) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
     
        userMapper.updateUser(updateUserRequest, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user); 
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, 
        @RequestBody ChangePasswordRequest request){
            var user = userRepository.findById(id).orElse(null);
            if(user == null){
                return ResponseEntity.notFound().build();
            }
            if(!user.getPassword().equals(request.getOldPassword())){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            user.setPassword(request.getNewPassword());
            userRepository.save(user);

            return ResponseEntity.noContent().build();
        }
 }
