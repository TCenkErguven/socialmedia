package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Document
public class Follow extends Base{
    @Id
    private String id;
    private String userId;
    //userId'nin takip ettiği kişinin id'si
    private String followId;
    private String followName;
    private String followerName;
}
