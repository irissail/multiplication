package com.gosia.multiplication.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Integer> {

    UserDTO findByEmail(String email);
}
