package com.bilgeadam.service;

import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {
    private final IUserProfileRepository userProfileRepository;
    public UserProfileService(IUserProfileRepository userProfileRepository){
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }
    public void deleteAll(){
        userProfileRepository.deleteAll();
    }
    //elasticsearch'de metinsel değerler(String, char, vb.) değerler üzerinde herhangi bir sıralama işlemi yapılamamaktadır.
    //Bu işlemin gerçekleşebilmesi için elasticsearch arayüzüne kibana vb. araçlar ile ulaşarak bazı index ayarlarının
    //yapılması gerekmektedir. Burada sıralama sayısal değerler üzerinde yapılabilmektedir.
    public Page<UserProfile> findAll(int page,int size, String sortParameter, String sortType){
        Sort sort = null;
        Pageable pageable = null;

        if(sortParameter != null)
            sort = Sort.by(Sort.Direction.fromString(sortType == null ? "ASC" : sortType),sortParameter);

        if(sort != null && page != 0){
            pageable = PageRequest.of(page,size == 0 ? 10 : size,sort);
        }else {
            pageable = PageRequest.of(page, size == 0 ? 10 : size);
        }
        return userProfileRepository.findAll(pageable);


    }
}
