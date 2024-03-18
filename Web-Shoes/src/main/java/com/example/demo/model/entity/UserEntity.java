package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private LocalDateTime createDate;
    private String fullname;
    private String password;
    private String phone;
    private String username;
    private String email;
    private Integer status;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    @JoinTable(name = "role_details",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<RoleEntity> roles;

    public void addRole(RoleEntity role) {
        if (role != null) {
            if (roles == null) {
                roles = new ArrayList<>();
            }
            roles.add(role);

            // Kiểm tra và khởi tạo danh sách tài khoản của vai trò
            if (role.getUsers() == null) {
                role.setUsers(new ArrayList<>());
            }
            role.getUsers().add(this);
        }
    }


}
