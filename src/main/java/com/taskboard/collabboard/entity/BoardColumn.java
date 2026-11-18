package com.taskboard.collabboard.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "board_columns")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private Integer position;

  @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("position ASC")
  @JsonManagedReference
  private List<TaskCard> cards = new ArrayList<>();
}
