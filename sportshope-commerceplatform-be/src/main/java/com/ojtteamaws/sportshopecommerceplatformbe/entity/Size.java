package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.SizeChartType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SizeChartType chartType;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "size")
    private List<ProductVariant> variants = new ArrayList<>();

    // @@unique([name, chartType]) → unique constraint
    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        // Có thể dùng UniqueConstraint ở @Table nếu muốn:
        // @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","chart_type"}))
    }
}
