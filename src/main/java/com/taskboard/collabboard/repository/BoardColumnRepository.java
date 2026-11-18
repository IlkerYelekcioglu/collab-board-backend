package com.taskboard.collabboard.repository;

import com.taskboard.collabboard.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}
