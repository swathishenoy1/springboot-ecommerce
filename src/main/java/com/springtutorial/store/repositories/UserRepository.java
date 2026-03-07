package com.springtutorial.store.repositories;

import com.springtutorial.store.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
