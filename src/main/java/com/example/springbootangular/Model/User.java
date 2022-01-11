package com.example.springbootangular.Model;


import com.example.springbootangular.Audit.Auditable;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_user1")
public class User extends Auditable<String> {//extends Auditable<String>
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "pw")
    @NotEmpty(message = "Please provide password")
    private String pwd;

    @Column(name = "first_name")
    @NotEmpty(message = "Please provide firstname")
    private String firstname;

    @Column(name = "last_name")
    @NotEmpty(message = "Please provide lastname")
    private String lastname;

    @Column(name = "email")
    @Email(message = "Invalid email!")
    @NotBlank(message = "Please provide email")
    private String email;

    @Column(name = "role")
    private String role;
}
