package com.example.springbootangular.Model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "tb_user")
public class User {
    @Id
    @Column(name = "username")
    private String userName;

    @Column(name = "pw")
    private String pwd;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "email")
    private String email;
}
