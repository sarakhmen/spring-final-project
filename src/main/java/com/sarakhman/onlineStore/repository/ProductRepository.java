package com.sarakhman.onlineStore.repository;

import com.sarakhman.onlineStore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByPriceBetween(double start, double end, Pageable pageable);
    Product findById(long id);
    void deleteById(long id);

    @Query(value = "SELECT p FROM Product p LEFT JOIN p.properties props WHERE p.price BETWEEN ?3 AND ?4" +
            " AND props.propertyName IN ?1 GROUP BY p HAVING COUNT(DISTINCT props.propertyName) = ?2")
    List<Product> findProductByProperties(List<String> propertyNames, long namesCount, double start, double stop,
                                          Sort sort);
}
