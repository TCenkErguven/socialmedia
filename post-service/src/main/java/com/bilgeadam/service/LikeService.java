package com.bilgeadam.service;

import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import com.bilgeadam.manager.IUserProfileManager;
import com.bilgeadam.repository.ILikeRepository;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService extends ServiceManager<Like,String> {
    private final ILikeRepository likeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserProfileManager userProfileManager;

    public LikeService(ILikeRepository likeRepository, JwtTokenProvider jwtTokenProvider, IUserProfileManager userProfileManager){
        super(likeRepository);
        this.likeRepository = likeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileManager = userProfileManager;
    }

    /*
        Buradaki metot kullanıcının bir postu beğendiği zaman çalışacak metottur.
        Post beğenildiğinde post'a beğenenin bilgisi gitmelidir.
        Ayrıca tek tek hangi postun kimin tarafından beğenildiğinin bilgisi de Like entity' sinin tablosunda tutulmalıdır.
        - Postu beğenebilmek için giriş yapılmalıdır.
        - Postu beğenenin bilgileri gereklidir.
     */
    public Optional<Like> findByPostIdAndUserId(String postId,String userId){
        return likeRepository.findByPostIdAndUserId(postId,userId);
    }

    public void deleteByUserIdAndPostId(String userId,String postId){
        likeRepository.deleteByUserIdAndPostId(userId,postId);
    }





}
