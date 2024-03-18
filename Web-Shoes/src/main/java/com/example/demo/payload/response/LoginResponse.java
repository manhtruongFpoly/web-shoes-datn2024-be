package com.example.demo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String fullname;
    private String username;
    private String phone;
    private Collection<? extends GrantedAuthority> role;

    public LoginResponse(String token,
                         String fullname,
                         Collection<? extends GrantedAuthority> role,
                         String username,
                         Long id,
                         String phone) {
        this.token = token;
        this.fullname = fullname;
        this.role  = role;
        this.username = username;
        this.id = id;
        this.phone = phone;
    }
}
