package com.taskboard.collabboard.controller;


import com.taskboard.collabboard.dto.BoardEvent;
import com.taskboard.collabboard.dto.CardMovedEvent;
import com.taskboard.collabboard.entity.BoardColumn;
import com.taskboard.collabboard.entity.TaskCard;
import com.taskboard.collabboard.repository.BoardColumnRepository;
import com.taskboard.collabboard.repository.TaskCardRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
public class BoardController {

  private final BoardColumnRepository columnRepository;
  private final TaskCardRepository cardRepository;
  private final SimpMessagingTemplate messagingTemplate; // WebSocket için


  public BoardController(BoardColumnRepository columnRepository, TaskCardRepository cardRepository,
      SimpMessagingTemplate messagingTemplate) {
    this.columnRepository = columnRepository;
    this.cardRepository = cardRepository;
    this.messagingTemplate = messagingTemplate;
  }

  // Tüm panoyu getir
  @GetMapping
  public List<BoardColumn> getBoard() {
    return columnRepository.findAll();
  }

  @PutMapping("/card/{cardId}/move")
  public ResponseEntity<?> moveCard(@PathVariable Long cardId, @RequestBody Map<String, Long> body) {
    Long newColumnId = body.get("newColumnId");

    TaskCard card = cardRepository.findById(cardId).orElseThrow();
    BoardColumn newColumn = columnRepository.findById(newColumnId).orElseThrow();

    card.setColumn(newColumn);
    cardRepository.save(card);

    messagingTemplate.convertAndSend("/topic/board", new CardMovedEvent(cardId, newColumnId));

    return ResponseEntity.ok().build();
  }

  @PostMapping("/column/{columnId}/card")
  public ResponseEntity<?> addCard(@PathVariable Long columnId, @RequestBody Map<String, String> body) {
    String title = body.get("title");
    BoardColumn column = columnRepository.findById(columnId).orElseThrow();

    TaskCard newCard = new TaskCard();
    newCard.setTitle(title);
    newCard.setPosition(column.getCards().size());
    newCard.setColumn(column);
    cardRepository.save(newCard);

    BoardEvent event = new BoardEvent("CARD_CREATED", newCard.getId(), columnId, title);
    messagingTemplate.convertAndSend("/topic/board", event);

    return ResponseEntity.ok(newCard);
  }
}
