package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UpdatePostRequest {
    private String content;
    private List<String> addMediaUrls;
    private List<String> removeMediaUrls;
}
