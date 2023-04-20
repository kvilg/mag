package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

@Entity
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String name;

    private String login;

    private String password;


    private Boolean statusActive;

    private String codeActive;




    public User() {


    }


    public User(String email,String name, String login, String password) {
        this.email = email;
        this.name = name;
        this.login = login;
        this.password = password;
        this.statusActive = false;
    }

    public User(String ssss) {
        this.name = ssss;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idUser) {
        this.id = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Boolean getStatusActive() {
        return statusActive;
    }

    public void setStatusActive(Boolean statusActive) {
        this.statusActive = statusActive;
    }

    public String getCodeActive() {
        return codeActive;
    }

    public void setCodeActive(String codeActive) {
        this.codeActive = codeActive;
    }
}