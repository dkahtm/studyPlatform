package com.study.studyplatform.JavaFX.repository;

import com.study.studyplatform.JavaFX.entity.Group;
import com.study.studyplatform.JavaFX.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    Group findByName(String name);
    List<Group> findAllByCreatedBy(User user);
}
