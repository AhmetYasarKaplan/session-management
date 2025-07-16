package com.yasar.sessionservice.model;

import jakarta.persistence.*;
import lombok.*;

//User class’ı veritabanında users tablosuna karşılık geliyor
//Her kullanıcı bir id, username ve password içeriyor
//Giriş yaparken bu bilgileri kullanacağız

@Entity //veritabanında tabloya karşılık gelir
@Table(name = "users")
@Data // getter ve setter metodlarını otomatik olarak oluşturur
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}

