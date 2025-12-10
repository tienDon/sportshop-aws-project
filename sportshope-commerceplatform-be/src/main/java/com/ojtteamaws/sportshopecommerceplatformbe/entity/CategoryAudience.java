package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "category_audiences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"category_id", "audience_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoryAudience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "audience_id", nullable = false)
    private Audience audience;

    @Column(nullable = false)
    private Integer sortOrder = 0;
}
