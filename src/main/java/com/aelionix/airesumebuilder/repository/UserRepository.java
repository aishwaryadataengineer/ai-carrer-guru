package com.aelionix.airesumebuilder.repository;

import com.aelionix.airesumebuilder.model.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByUserName(String userName);

     Optional<User>  findByEmailId(String emailId);

     Optional<User> findByPhone(String phone);

     Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByUserNameOrEmailId(String userName, String emailId);
}
