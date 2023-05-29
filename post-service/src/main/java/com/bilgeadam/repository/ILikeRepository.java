package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ILikeRepository extends MongoRepository<Like,String> {
    Optional<Like> findByPostIdAndUserId(String postId, String userId);
    void deleteByUserIdAndPostId(String userId,String postId);
    void deleteAllByPostId(String postId);
}
