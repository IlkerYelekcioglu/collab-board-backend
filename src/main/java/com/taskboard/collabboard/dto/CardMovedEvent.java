package com.taskboard.collabboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CardMovedEvent {

  private Long cardId;
  private Long newColumnId;

}
