package com.example.demo.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue
    @NotNull
    private Integer id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    public Admin(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
