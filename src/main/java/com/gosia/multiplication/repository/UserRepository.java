package com.gosia.multiplication.repository;


import com.gosia.multiplication.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Integer> {

    UserDTO findByEmail(String email);
}
