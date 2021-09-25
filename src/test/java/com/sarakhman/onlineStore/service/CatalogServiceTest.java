package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.model.Property;
import com.sarakhman.onlineStore.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/testDataCatalogService.sql"})
public class CatalogServiceTest {
    @Autowired
    CatalogService catalogService;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void shouldReturnSortByNameZA_WhenCorrespondingParam(){
        Sort actual = catalogService.getSort("byNameZA");
        Sort expected = Sort.by(Sort.Direction.DESC, "productName");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnSortByNameAZ_WhenCorrespondingParam(){
        Sort expected = Sort.by(Sort.Direction.ASC, "productName");

        Sort actual = catalogService.getSort("byNameAZ");
        Assert.assertEquals(actual, expected);

        actual = catalogService.getSort(null);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnSortByPriceLH_WhenCorrespondingParam(){
        Sort actual = catalogService.getSort("byPriceLH");
        Sort expected = Sort.by(Sort.Direction.ASC, "price");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnSortByPriceHL_WhenCorrespondingParam(){
        Sort actual = catalogService.getSort("byPriceHL");
        Sort expected = Sort.by(Sort.Direction.DESC, "price");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnSortByNewest_WhenCorrespondingParam(){
        Sort actual = catalogService.getSort("newest");
        Sort expected = Sort.by(Sort.Direction.DESC, "creationDate");
        Assert.assertEquals(actual, expected);
    }

    @Test
    @Transactional
    public void shouldReturnCorrectProduct_WhenCorrespondingParam(){
        Product expectedProduct = productRepository.findById(1);
        Map<String, Set<String>> properties = new HashMap<>();
        properties.put("Model", new HashSet<>(Arrays.asList("pixel4")));
        properties.put("Size", new HashSet<>(Arrays.asList("small")));
        List<Product> products = catalogService.findProductsSortedAndByPropertiesAndPrice(properties, 0,
                10000, PageRequest.of(0, 10)).toList();
        Assert.assertEquals(1, products.size());

        Product actualProduct = products.get(0);
        Assert.assertEquals(expectedProduct, actualProduct);

        products = catalogService.findProductsSortedAndByPropertiesAndPrice(new HashMap<>(), 0,
                10000, PageRequest.of(0, 10)).toList();
    }

    @Test
    public void shouldDeleteAndSaveNewProduct_WhenEditProduct(){
        Product oldProduct = productRepository.findById(1);
        catalogService.editProduct(oldProduct);
        Product newProduct = productRepository.findById(1);
        Assert.assertNull(newProduct);

        newProduct = productRepository.findById(3);
        Assert.assertEquals(oldProduct.getProductName(), newProduct.getProductName());
    }
}
