package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audiences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // "Nam", "Nữ", "Trẻ em"

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "audience")
    private List<CategoryAudience> categoryAudiences = new ArrayList<>();

    @OneToMany(mappedBy = "audience")
    private List<ProductAudience> productAudiences = new ArrayList<>();
}
