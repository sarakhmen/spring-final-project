package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.repository.ProductRepository;
import com.sarakhman.onlineStore.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product findProductById(long productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public void editProduct(Product product) {
        productRepository.deleteById(product.getId());
        productRepository.save(product);
    }
}
