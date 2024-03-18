package com.example.demo.payload.request;

import lombok.Data;

@Data
public class UserRegister {

    private String username;

    private String fullname;

    private String phone;

    private String email;

    private String password;

    private String roleName;

}
