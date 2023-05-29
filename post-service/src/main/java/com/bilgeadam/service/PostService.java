package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.request.DeleteCommentRequest;
import com.bilgeadam.dto.request.PostCommentRequestDto;
import com.bilgeadam.dto.request.UpdatePostRequest;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import com.bilgeadam.manager.IUserProfileManager;
import com.bilgeadam.mapper.IPostMapper;
import com.bilgeadam.rabbitmq.model.AuthIdModel;
import com.bilgeadam.rabbitmq.model.UserProfileModel;
import com.bilgeadam.rabbitmq.producer.FindByAuthIdProducer;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.repository.entity.Dislike;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService extends ServiceManager<Post,String> {

    private final IPostRepository postRepository ;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserProfileManager userProfileManager;
    private final FindByAuthIdProducer findByAuthIdProducer;
    private final LikeService likeService;
    private final DislikeService dislikeService;
    private final CommentService commentService;

    public PostService(IPostRepository postRepository, JwtTokenProvider jwtTokenProvider, IUserProfileManager userProfileManager, FindByAuthIdProducer findByAuthIdProducer, LikeService likeService, DislikeService dislikeService, CommentService commentService){
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileManager = userProfileManager;
        this.findByAuthIdProducer = findByAuthIdProducer;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
        this.commentService = commentService;
    }

    public Post createPost(String token, CreateNewPostRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        //UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        System.out.println(authId.get());
        UserProfileModel userProfile = (UserProfileModel) findByAuthIdProducer.findByAuthIdRabbit(AuthIdModel.builder().authId(authId.get()).build());
        System.out.println(userProfile);
        Post post = IPostMapper.INSTANCE.toPost(dto);
        post.setUserId(userProfile.getId());
        post.setUsername(userProfile.getUsername());
        post.setAvatar(userProfile.getAvatar());
        return save(post);
    }


    public Post createPostFeign(String token, CreateNewPostRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        System.out.println(dto);
        Post post = IPostMapper.INSTANCE.toPost(dto);
        System.out.println(post);
        post.setUserId(userProfile.getId());
        post.setUsername(userProfile.getUsername());
        post.setAvatar(userProfile.getAvatar());
        System.out.println(post);
        return save(post);
    }

    public Boolean likePost(String token,String postId){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto dto = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = findById(postId);
        if(optionalPost.isEmpty())
            throw new PostManagerException(ErrorType.POST_NOT_FOUND); //NOT FOUND POST YAPILACAK
        Optional<Like> optionalLike = likeService.findByPostIdAndUserId(postId,dto.getId());
        if(optionalLike.isEmpty()){
            Like newLike = Like.builder().postId(postId)
                    .userId(dto.getId())
                    .avatar(dto.getAvatar())
                    .username(dto.getUsername())
                    .build();
           /*
            likeService.save(newLike);
            optionalPost.get().getLikes().add(newLike.getId());
            update(optionalPost.get());
            return true;
            */
            if(postId.isEmpty()){
                throw new PostManagerException(ErrorType.POST_NOT_FOUND);
            }
            optionalPost.get().getLikes().add(dto.getId());
            update(optionalPost.get());
            newLike.setPostId(postId);
            likeService.save(newLike);
            return true;
        }
        optionalPost.get().getLikes().remove(optionalLike.get().getUserId());
        update(optionalPost.get());
        likeService.deleteById(optionalLike.get().getId());
        return false;
    }

    public Boolean dislikePost(String token,String postId){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto dto = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = findById(postId);
        if(optionalPost.isEmpty())
            throw new PostManagerException(ErrorType.POST_NOT_FOUND);
        Optional<Dislike> optionaDislike = dislikeService.findByPostIdAndUserId(postId,dto.getId());
        if(optionaDislike.isEmpty()){
            Dislike newDislike = Dislike.builder().postId(postId)
                    .userId(dto.getId())
                    .avatar(dto.getAvatar())
                    .username(dto.getUsername())
                    .build();
            optionalPost.get().getDislikes().add(newDislike.getUserId());
            update(optionalPost.get());
            dislikeService.save(newDislike);
            return true;
        }
        optionalPost.get().getDislikes().remove(optionaDislike.get().getUserId());
        update(optionalPost.get());
        dislikeService.deleteById(optionaDislike.get().getId());
        return false;
    }


    public void deletePostById(String token,String postId){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isEmpty())
            throw new PostManagerException(ErrorType.POST_NOT_FOUND);
        if(userProfile.getId().equals(optionalPost.get().getUserId())) {

                optionalPost.get().getLikes().forEach(likeId -> {
                   // likeService.deleteById(likeId);
                    likeService.deleteByUserIdAndPostId(likeId,postId);
                });
                optionalPost.get().getDislikes().forEach(dislikeId -> {
                    dislikeService.deleteById(dislikeId);
                });


            deleteById(postId);
        }else{
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
    }
    //TODO: content burada set ediliyor, listeye ekleme çıkarma ve conten aksiyonlarının mapper üzerinden yönetilmesi yapılacak.
    public Post updatePost(String token, String postId, UpdatePostRequest dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        authId.orElseThrow(() -> new PostManagerException(ErrorType.INVALID_TOKEN));
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(userProfile.getId().equals(optionalPost.get().getUserId())){
           // dto.getAddMediaUrls().forEach(x -> {optionalPost.get().getMediaUrl().add(x);});
            optionalPost.get().getMediaUrl().addAll(dto.getAddMediaUrls());
            optionalPost.get().getMediaUrl().removeAll(dto.getRemoveMediaUrls());
            optionalPost.get().setContent(dto.getContent());
            return update(optionalPost.get());
        }
        throw new PostManagerException(ErrorType.POST_NOT_FOUND);
    }

    public Post postComment(String token, PostCommentRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty())
            throw new RuntimeException("NO USER FOUND");
      //  UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
      //  Optional<Post> optionalPost = postRepository.findById(dto.getPostId());
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = postRepository.findById(dto.getPostId());
        if(optionalPost.isPresent()){
            Comment newComment = Comment.builder()
                    .userId(userProfile.getId())
                    .postId(optionalPost.get().getId())
                    .comment(dto.getComment())
                    .username(userProfile.getUsername())
                    .build();
            commentService.save(newComment);
            if(dto.getCommentId() != null){
                Optional<Comment> optionalComment = commentService.findById(dto.getCommentId());
                if(optionalComment.isEmpty())
                    throw new RuntimeException("NO COMMENT FOUND");
                optionalComment.get().getSubComment().add(newComment.getId());
                commentService.update(optionalComment.get());
            }
            optionalPost.get().getComments().add(newComment.getId());
            update(optionalPost.get());
            System.out.println(newComment);
            return optionalPost.get();
        }
        throw new PostManagerException(ErrorType.POST_NOT_FOUND);
    }

    public boolean updateComment(String token, PostCommentRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty())
            throw new RuntimeException("NO USER FOUND");
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> optionalPost = postRepository.findById(dto.getPostId());
        if(userProfile.getId().equals(optionalPost.get().getUserId())) {
            update(optionalPost.get());
            Optional<Comment> optionalComment = commentService.findById(dto.getCommentId());
            if(optionalComment.isPresent()){
                optionalComment.get().setComment(dto.getComment());
                commentService.update(optionalComment.get());
                return true;
            }
            throw new RuntimeException("NO COMMENT FOUND");
        }
        throw new PostManagerException(ErrorType.INVALID_TOKEN);
    }

    public Boolean deleteCommentById(String token, DeleteCommentRequest dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty())
            throw new RuntimeException("NO USER FOUND");
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        System.out.println(userProfile);
        Optional<Comment> optionalComment = commentService.findById(dto.getCommentId());
        if (optionalComment.isEmpty())
            throw new RuntimeException("NO COMMENT EXIST");
        Optional<Post> optionalPost = findById(optionalComment.get().getPostId());
        if(optionalPost.isEmpty())
            throw new PostManagerException(ErrorType.POST_NOT_FOUND);
        if(userProfile.getId().equals(optionalComment.get().getUserId())){
            //SubComment'leri silme
            optionalComment.get().getSubComment().forEach(subCommentId -> {
                Optional<Comment> subComment = commentService.findById(subCommentId);
                if(subComment.isPresent()){
                    System.out.println(subCommentId);
                    commentService.deleteById(subCommentId);
                    optionalPost.get().getComments().remove(subCommentId);
                }
            });
            System.out.println("SUBCOMMENTLER SILINDI");
            //SubComment olduğu yorumdan silme
            List<Comment> comments = commentService.findAll();
            for(Comment comment : comments){
                if(comment.equals(dto.getCommentId())) {
                    comment.getSubComment().remove(optionalComment.get().getId());
                    System.out.println(comment);
                    commentService.save(comment);
                    break;
                }
            }
            System.out.println("SUBCOMMENT OLDUĞU SILINDI");
            //Post'ttan comment silme
            optionalPost.get().getComments().remove(dto.getCommentId());
            System.out.println(optionalPost.get());
            System.out.println("Comment silindi");
            update(optionalPost.get());
            commentService.deleteById(dto.getCommentId());
            return true;

        }
            throw new RuntimeException("BAD REQUEST");
    }
}
