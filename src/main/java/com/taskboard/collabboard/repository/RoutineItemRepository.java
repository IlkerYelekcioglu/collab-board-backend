package com.taskboard.collabboard.repository;

import com.taskboard.collabboard.entity.RoutineItem;
import com.taskboard.collabboard.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineItemRepository extends JpaRepository<RoutineItem, Long> {
  List<RoutineItem> findByUserAndDayTypeOrderByPositionAsc(User user, String dayType);

}
