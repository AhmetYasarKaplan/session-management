package com.yasar.sessionservice.model;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSession implements Serializable {
    // bu da anlık aktif oturumları tutacak.
    // Redis için özel olarak JPA değil, Serializable olmasına dikkat etmemiz gerekiyor.
    //Bu sınıf, Redis’e doğrudan yazılacak (Spring Data Redis kullanacağız).
    //Redis key: active::user:{id} olacak.
    private Long userId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime loginAt;
}