package com.example.demo.service.impl;

import com.example.demo.model.entity.RoleEntity;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.payload.request.UserRegister;
import com.example.demo.payload.response.DefaultResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public DefaultResponse<UserRegister> registerAccount(UserRegister userRegister){

        DefaultResponse<UserRegister> response = this.validateRegister(userRegister);
        if(response.getSuccess() != 200){
            return response;
        }

        Optional<UserEntity> findByUserEntity = this.userRepository.findByUsername(userRegister.getUsername());
        if(!findByUserEntity.isPresent()){
            UserEntity userEntity = modelMapper.map(userRegister, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userRegister.getPassword()));
            userEntity.setStatus(1);

            RoleEntity role = this.roleRepository.findByName("CUSTOMER");
            if(role == null){
                response.setMessage("Role chưa tồn tại");
                return response;
            }
            userEntity.addRole(role);
            userRegister = modelMapper.map(userRepository.save(userEntity),UserRegister.class);

            response.setData(userRegister);
        }
        else{
            response.setSuccess(500);
            response.setData(null);
            response.setMessage("Tài khoản đã tồn tại");
        }
        return response;
    }

    private DefaultResponse<UserRegister> validateRegister(UserRegister userRegister){

        DefaultResponse<UserRegister> response = new DefaultResponse<>();

        Optional<UserEntity> findByUserName = userRepository.findByUsername(userRegister.getUsername());
        if(findByUserName.isPresent()){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("username đã tồn tại bạn !!!");
            response.setData(null);
            return response;
        }

        if(Objects.isNull(userRegister.getUsername())){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("username không được để trống");
            response.setData(null);
            return response;
        }

        if(userRegister.getUsername().length() > 15){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("username không được vượt quá 15 ký tự");
            response.setData(null);
            return response;
        }


        if(Objects.isNull(userRegister.getFullname())){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("fullname không được để trống");
            response.setData(null);
            return response;
        }

        if(userRegister.getFullname().length() > 100){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("fullname không được vượt quá 100 ký tự");
            response.setData(null);
            return response;
        }

        if(Objects.isNull(userRegister.getEmail())){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("Email không được để trống");
            response.setData(null);
            return response;
        }

        if(Objects.isNull(userRegister.getPhone())){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("phone không được để trống");
            response.setData(null);
            return response;
        }

        if(Objects.isNull(userRegister.getPassword())){
            response.setSuccess(HttpStatus.NOT_FOUND.value());
            response.setMessage("password không được để trống");
            response.setData(null);
            return response;
        }

        response.setSuccess(HttpStatus.OK.value());
        response.setMessage("Thành công");
        response.setData(userRegister);

        return response;
    }
}
