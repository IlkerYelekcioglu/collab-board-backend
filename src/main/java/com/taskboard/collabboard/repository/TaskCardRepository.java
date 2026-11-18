package com.taskboard.collabboard.repository;

import com.taskboard.collabboard.entity.TaskCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCardRepository extends JpaRepository<TaskCard, Long> {
}
