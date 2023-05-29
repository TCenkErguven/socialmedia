package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IFollowRepository extends MongoRepository<Follow,String> {
    Optional<Follow> findByUserIdAndFollowId(String userId,String followId);
}
