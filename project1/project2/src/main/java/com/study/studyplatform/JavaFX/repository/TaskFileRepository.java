package com.study.studyplatform.JavaFX.repository;


import com.study.studyplatform.JavaFX.entity.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskFileRepository extends JpaRepository<TaskFile, Long> {

    // Способ 1: Используем правильное имя поля (taskId, а не task.id)
    List<TaskFile> findByTaskId(Long taskId);

    // Способ 2: Или используем @Query с явным указанием SQL
    @Query("SELECT tf FROM TaskFile tf WHERE tf.taskId = :taskId")
    List<TaskFile> findFilesByTaskId(@Param("taskId") Long taskId);

    // Удаление файлов по ID задачи
    void deleteByTaskId(Long taskId);
}
