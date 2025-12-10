package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="phone",  length = 15, unique = true)
    private String phone;


    @Column(name="email",  unique = true, length = 100)
    private String email;

    @Column(name="full_name", length = 100)
    String fullName;

    @Column(name="role", nullable = false)
    private String role = "USER";

    @CreationTimestamp
    @Column(name="create_at", nullable = false)
    private Instant createAt;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

}

