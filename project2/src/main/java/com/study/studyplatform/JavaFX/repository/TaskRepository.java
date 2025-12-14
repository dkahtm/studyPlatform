package com.study.studyplatform.JavaFX.repository;

import com.study.studyplatform.JavaFX.entity.Tasks;
import com.study.studyplatform.JavaFX.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByGroup_Id(Integer groupId);
    @Query("SELECT t FROM Tasks t WHERE t.deadline = CURRENT_DATE + 2")
    List<Tasks> findTasksWithDeadlineInTwoDays();
    long countByStatusAndGroup_Id(Tasks.Status status, Integer groupId);





}


