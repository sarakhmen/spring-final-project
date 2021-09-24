package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.model.Property;
import com.sarakhman.onlineStore.repository.ProductRepository;
import com.sarakhman.onlineStore.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private ProductRepository productRepository;

    @Autowired
    public CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public Page<Product> findProductsSortedAndByPropertiesAndPrice(Map<String, Set<String>> properties,
                                                                   double start, double stop,
                                                                   Pageable pageable){
        if(properties.isEmpty()){
            return productRepository.findByPriceBetween(start, stop, pageable);
        }

        List<String> propertyNames = new ArrayList<>(properties.keySet());
        List<Product> products = productRepository.findProductByProperties(propertyNames, propertyNames.size(),
                start, stop, pageable.getSort());
        products = products.stream().filter(x -> {
            Set<Property> productProperties = x.getProperties();
            for(Property productProperty : productProperties){
                String propertyName = productProperty.getPropertyName();
                String propertyValue = productProperty.getPropertyValue();
                if(properties.containsKey(propertyName)){
                    Set<String> values = properties.get(propertyName);
                    if (!values.contains(propertyValue)) {
                        return false;
                    }
                }
            }
            return true;
        }).collect(Collectors.toList());

        return PaginationUtil.getPageOf(pageable, products);
    }


    public Sort getSort(String sortParam){
        if(sortParam == null){
            return Sort.by(Sort.Direction.ASC, "productName");
        }

        Sort sort = null;
        switch(sortParam){
            case "byPriceLH":
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "byPriceHL":
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            case "newest":
                sort = Sort.by(Sort.Direction.ASC, "creationDate");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "productName");
                break;
        }
        return sort;
    }
}
