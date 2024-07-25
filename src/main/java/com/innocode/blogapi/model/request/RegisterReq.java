package com.innocode.blogapi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
