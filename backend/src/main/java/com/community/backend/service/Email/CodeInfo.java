package com.community.backend.service.Email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeInfo {
    private final String code;
    private final long createdAt;
}
