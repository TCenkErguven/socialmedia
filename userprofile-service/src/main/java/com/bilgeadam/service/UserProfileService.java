package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.UserProfileChangePasswordResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserProfileManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserProfileMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    private final CacheManager cacheManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterProducer registerProducer;
    private final PasswordEncoder passwordEncoder;
    public UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, JwtTokenProvider jwtTokenProvider,CacheManager cacheManager,
                              RegisterProducer registerProducer, PasswordEncoder passwordEncoder) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.passwordEncoder = passwordEncoder;
    }


    @CacheEvict(value = "find-by-role", allEntries = true)
    public Boolean createUser(NewCreateUserRequestDto dto){
        try {
            save(IUserProfileMapper.INSTANCE.fromDtoToUserProfile(dto));
           // cacheManager.getCache("find-by-role").clear();
            cacheManager.getCache("find-all-with-cache").clear();
            return true;
        }catch (Exception e){
            throw new RuntimeException("Beklenmeyen bir hata oluştu.");
        }
    }
    @CacheEvict(value = "find-by-username", key = "#model.username.toLowerCase()")
    public Boolean createUserWithRabbitMq(RegisterModel model){
        try {
            System.out.println(model);
            UserProfile userProfile = IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model);
            save(userProfile);
            //registerProducer.registerElastic(IUserProfileMapper.INSTANCE.fromUserProfileToRegisterElasticModel(userProfile));
            return true;
        }catch (Exception e){
            throw new RuntimeException("Beklenmeyen bir hata oluştu.");
        }
    }

    public Boolean activateStatus(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()){
            throw new RuntimeException("Auth id bulunamadı");
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }
    @Caching(
            evict = {
                    @CacheEvict(value = "find-by-role", allEntries = true)
            },
            put = {
                    @CachePut(value = "find-by-username", key = "#dto.username.toLowerCase()")  //oluşan değişiklikler sonucunda cache'in update edilmesini sağlar içerisinde unless olursa şart olur
            }
            )
        public UserProfile update(UserProfileUpdateRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        //cache delete
        //cacheManager.getCache("find-by-username").evict(userProfile.get().getUsername().toLowerCase());
        /*
        ----UserProfile Çözümü----
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUserProfile(dto, userProfile.get()));
        authManager.updateUsernameOrEmail(IUserProfileMapper.INSTANCE.toUpdateUsernameEmail(userProfile.get()));
        */
        UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserProfileMapper.INSTANCE.toUpdateUsernameEmail(dto);
        updateEmailOrUsernameRequestDto.setAuthId(authId.get());
        authManager.updateUsernameOrEmail(updateEmailOrUsernameRequestDto);
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUserProfile(dto, userProfile.get()));
    //    cacheManager.getCache("find-by-username").put(userProfile.get().getUsername().toLowerCase(),userProfile.get());
        return userProfile.get();
    }


    public Boolean delete(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    @Cacheable(value = "find-by-username", key = "#username.toLowerCase()") //key cachelenen verirdir.
    public UserProfile findByUsernameIgnoreCase(String username){
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            throw new RuntimeException();
        }
        Optional<UserProfile> userList = userProfileRepository.findByUsernameIgnoreCase(username);
        if(userList.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userList.get();
    }

    @Cacheable(value = "find-all-with-cache")
    public List<UserProfile> findAllWithCache(){
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        if(userProfileList.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            throw new RuntimeException();
        }
        return userProfileList;
    }

    @Cacheable(value = "find-by-role", key = "#role.toUpperCase()") //Roller veri tabanında büyük harfle tutulduğu için
    public List<UserProfile> findByRole(String role){
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            throw new RuntimeException();
        }
        List<Long> authIds = authManager.findByRole(role).getBody();    //getBody'le ResponseEntity'den kurtarıyor gelen nesneyi
        return authIds.stream().map(x ->
                userProfileRepository.findOptionalByAuthId(x)
                        .orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);}))
                .collect(Collectors.toList());
    }
    //for FollowService
    public Optional<UserProfile> findByAuthId(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if(userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile;
    }

    //ChangePassword
    @CacheEvict(value = "find-by-username",allEntries = true)
    public Boolean changePassword(ChangePasswordDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if(authId.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if(optionalUserProfile.isPresent() ){
            if(passwordEncoder.matches(dto.getOldPassword(), optionalUserProfile.get().getPassword())){
                String newPass = dto.getOldPassword();
                optionalUserProfile.get().setPassword(passwordEncoder.encode(newPass));
                cacheManager.getCache("findAll").clear();
                userProfileRepository.save(optionalUserProfile.get());
                authManager.changePassword(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(optionalUserProfile.get()));
                return true;
            }else {
                throw new UserProfileManagerException(ErrorType.PASSWORD_ERROR);
            }
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean updatePassword(UserProfileChangePasswordResponseDto dto) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        optionalUserProfile.get().setPassword(dto.getPassword());
        update(optionalUserProfile.get());
        return true;
    }
}
