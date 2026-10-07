package com.nvrsocial.market.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_-]{3,32}")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
