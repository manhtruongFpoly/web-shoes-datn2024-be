package com.example.demo.controller;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.UserRegister;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.payload.response.LoginResponse;
import com.example.demo.payload.response.SampleResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.AuthService;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;


    //  todo: API này dùng để đăng ký tài khoản
    @PostMapping("/signup")
    public ResponseEntity<DefaultResponse<UserRegister>> registerAccount(@RequestBody UserRegister userRegister){
        return ResponseEntity.ok().body(authService.registerAccount(userRegister));
    }

    // todo: API này dùng để login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // tạo token bằng thông tin username khi login
            String token = jwtProvider.createToken(authentication);
            CustomerDetailService customerDetailService = (CustomerDetailService) authentication.getPrincipal();

            UserEntity userEntity = userRepository.findByUsername(customerDetailService.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User name not found"));

            if(userEntity.getStatus() == 0){
                throw new BadRequestException("Tài khoản của bạn chưa được kích hoạt");
            }

            return ResponseEntity.ok(
                    SampleResponse
                            .builder()
                            .success(true)
                            .message("Login success")
                            .data(new LoginResponse(
                                    token,
                                    customerDetailService.getFullname(),
                                    customerDetailService.getAuthorities(),
                                    customerDetailService.getUsername(),
                                    customerDetailService.getId(),
                                    customerDetailService.getPhone()
                            ))
                            .build());
        } catch (AuthenticationException e) {
            throw e;
        }
    }
}
