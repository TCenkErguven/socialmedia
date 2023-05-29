package com.bilgeadam.dto.request;

import com.bilgeadam.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserProfileDto {
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String info;
    private String address;
    private EStatus status = EStatus.PENDING;
}
