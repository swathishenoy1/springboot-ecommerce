package com.springtutorial.store.controllers;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private Iterable<UserDto> getAllUsers(@RequestParam (required = false, defaultValue ="", name = "sort") String sortBy){
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
 }
