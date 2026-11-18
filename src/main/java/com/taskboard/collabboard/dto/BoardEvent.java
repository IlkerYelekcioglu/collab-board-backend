package com.taskboard.collabboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardEvent {

  private String type; // "CARD_CREATED", "CARD_MOVED"
  private Long cardId;
  private Long columnId;
  private String title;
  private Long newColumnId;

  public BoardEvent(String cardCreated, Long id, Long columnId, String title) {
  }

  public BoardEvent(String type, Long cardId, Long columnId, String title, Long newColumnId) {
    this.type = type;
    this.cardId = cardId;
    this.columnId = columnId;
    this.title = title;
    this.newColumnId = newColumnId;
  }
}
