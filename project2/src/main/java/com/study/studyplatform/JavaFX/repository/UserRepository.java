package com.study.studyplatform.JavaFX.repository;

import com.study.studyplatform.JavaFX.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.study.studyplatform.JavaFX.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findById(int id);
    User findByName(String name);
    @Query("SELECT u FROM User u WHERE u.group.id = :groupId")
    List<User> findByGroupId(@Param("groupId") Integer groupId);
    List<User> findByNameContaining(String name);

}
