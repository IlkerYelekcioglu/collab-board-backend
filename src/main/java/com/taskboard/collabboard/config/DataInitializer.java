package com.taskboard.collabboard.config;

import com.taskboard.collabboard.entity.BoardColumn;
import com.taskboard.collabboard.entity.TaskCard;
import com.taskboard.collabboard.repository.BoardColumnRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

@Bean
CommandLineRunner initDatabase(BoardColumnRepository boardRepository) {
  return args -> {
    if (boardRepository.count() == 0) {
      BoardColumn todo = new BoardColumn();
      todo.setTitle("Yapılacaklar");
      todo.setPosition(0);

      TaskCard card1 = new TaskCard();
      card1.setTitle("Projeyi bitir");
      card1.setPosition(0);
      card1.setColumn(todo);
      todo.getCards().add(card1);

      BoardColumn inProgress = new BoardColumn();
      inProgress.setTitle("Devam Edenler");
      inProgress.setPosition(1);

      BoardColumn done = new BoardColumn();
      done.setTitle("Tamamlananlar");
      done.setPosition(2);

      boardRepository.saveAll(List.of(todo, inProgress, done));
    }
  };
 }
}
