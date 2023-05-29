package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Document
public class Post extends Base{

    @Id
    private String id;
    private String userId;
    private String username;
    private String avatar;
    private String content;
    @NotNull
    private List<String> mediaUrl;
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes  = new ArrayList<>();
    private List<String> comments  = new ArrayList<>();
}
