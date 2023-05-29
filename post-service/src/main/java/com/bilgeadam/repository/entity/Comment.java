package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Document
public class Comment extends Base {
    @Id
    private String id; //3
    private String userId; // yorum yapan kişinin id'si
    private String username; //yorum yapan kışinin adı
    private String postId;
    private String comment;
    private List<String> subComment = new ArrayList<>(); //yorumun altındaki yorum --> id
    private List<String> commentLikes = new ArrayList<>(); // userId tutulacak
    private List<String> commentDislikes = new ArrayList<>(); // userId tutulacak
}
