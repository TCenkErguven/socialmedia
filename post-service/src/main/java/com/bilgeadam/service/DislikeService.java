package com.bilgeadam.service;

import com.bilgeadam.repository.IDislikeRepository;
import com.bilgeadam.repository.entity.Dislike;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DislikeService extends ServiceManager<Dislike,String> {
    private final IDislikeRepository dislikeRepository;
    public DislikeService(IDislikeRepository dislikeRepository){
        super(dislikeRepository);
        this.dislikeRepository = dislikeRepository;
    }

    public Optional<Dislike> findByPostIdAndUserId(String postId, String userId){
        return dislikeRepository.findByPostIdAndUserId(postId,userId);
    }


}
