package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfileChangePasswordRequestDto {
    private Long authId;
    private String password;
}
