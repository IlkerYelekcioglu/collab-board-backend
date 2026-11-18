package com.taskboard.collabboard.controller;


import com.taskboard.collabboard.entity.RoutineItem;
import com.taskboard.collabboard.entity.User;
import com.taskboard.collabboard.repository.RoutineItemRepository;
import com.taskboard.collabboard.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
  @RequestMapping("/api/routine")
  public class RoutineController {

    private final RoutineItemRepository repository;
    private final UserRepository userRepository;


    public RoutineController(RoutineItemRepository repository, UserRepository userRepository) {
      this.repository = repository;
      this.userRepository = userRepository;
    }

    @GetMapping("/{dayType}")
    public List<RoutineItem> getRoutine(@PathVariable String dayType, Authentication authentication) {
      User user = getUserFromAuth(authentication);
      return repository.findByUserAndDayTypeOrderByPositionAsc(user, dayType);
    }

    @PostMapping
    public RoutineItem addRoutineItem(@RequestBody RoutineItem item, Authentication authentication) {
      User user = getUserFromAuth(authentication);

      List<RoutineItem> existing = repository.findByUserAndDayTypeOrderByPositionAsc(user, item.getDayType());

      item.setPosition(existing.size());
      item.setUser(user);

      return repository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteRoutineItem(@PathVariable Long id, Authentication authentication) {
      User user = getUserFromAuth(authentication);

      RoutineItem item = repository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutin bulunamadı"));

      if (!item.getUser().getId().equals(user.getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu işlemi yapmaya yetkiniz yok");
      }

      repository.delete(item);
    }

    private User getUserFromAuth(Authentication authentication) {
      if (authentication == null || !authentication.isAuthenticated()) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Giriş yapmalısınız");
      }
      String username = authentication.getName();
      return userRepository.findByUsername(username)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
    }
  }

