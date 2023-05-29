package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Dislike;
import com.bilgeadam.repository.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IDislikeRepository extends MongoRepository<Dislike,String> {
    Optional<Dislike> findByPostIdAndUserId(String postId, String userId);

}
