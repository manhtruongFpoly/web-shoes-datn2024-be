package com.example.demo.service;

import com.example.demo.payload.request.UserRegister;
import com.example.demo.payload.response.DefaultResponse;

public interface AuthService {
    DefaultResponse<UserRegister> registerAccount(UserRegister userRegister);
}
