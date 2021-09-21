package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.repository.ProductRepository;
import com.sarakhman.onlineStore.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private ProductRepository productRepository;
    private PropertyRepository propertyRepository;

    @Autowired
    public CatalogService(ProductRepository productRepository, PropertyRepository propertyRepository) {
        this.productRepository = productRepository;
        this.propertyRepository = propertyRepository;
    }

    public Page<Product> findAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }
}
