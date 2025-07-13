package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private String title;
    private String description;
    private String imageUrl;
    private String url;
    private String content;
}
