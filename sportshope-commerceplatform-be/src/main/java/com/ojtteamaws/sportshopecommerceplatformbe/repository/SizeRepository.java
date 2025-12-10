package com.ojtteamaws.sportshopecommerceplatformbe.repository;


import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Size;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.SizeChartType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findSizeById(long id) ;

    List<Size> findByChartType(SizeChartType chartType);
    List<Size> findAllByIsActiveTrue();

    List<Size> findAllByChartType(SizeChartType type);
    List<Size> findAllByChartTypeAndIsActiveTrue(SizeChartType chartType);

}