package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.model.Property;
import com.sarakhman.onlineStore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/add")
    public String addProductView(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "addProduct";
    }

    @GetMapping("/edit/{productId}")
    public String editProductView(@PathVariable("productId") long productId, Model model) {
        Product product = catalogService.findProductById(productId);

        String propertyNames = product.getProperties().stream().map(Property::getPropertyName)
                .collect(Collectors.joining(","));
        String propertyValues = product.getProperties().stream().map(Property::getPropertyValue)
                .collect(Collectors.joining(","));

        model.addAttribute("product", product);
        model.addAttribute("propertyNames", propertyNames);
        model.addAttribute("propertyValues", propertyValues);
        return "editProduct";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam(name = "propertyNames", required = false, defaultValue = "") String propertyNames,
                             @RequestParam(name = "propertyValues", required = false, defaultValue = "") String propertyValues,
                             @Valid Product product, BindingResult bindingResult) {
        Stream<String> streamOfPropNames = Pattern.compile(",").splitAsStream(propertyNames);
        Stream<String> streamOfPropValues = Pattern.compile(",").splitAsStream(propertyValues);
        String[] names = streamOfPropNames.map(String::trim).toArray(String[]::new);
        String[] values = streamOfPropValues.map(String::trim).toArray(String[]::new);
        validateProperties(bindingResult, names, values);

        if (bindingResult.hasErrors()) {
            return "addProduct";
        }

        Set<Property> properties = createProperties(product, names, values);
        product.setProperties(properties);
        catalogService.saveProduct(product);
        return "redirect:/admin/catalog";
    }

    @PostMapping("/edit/{productId}")
    public String editProduct(@PathVariable("productId") long productId,
                              @RequestParam(name = "propertyNames", required = false, defaultValue = "") String propertyNames,
                              @RequestParam(name = "propertyValues", required = false, defaultValue = "") String propertyValues,
                              @Valid Product product, BindingResult bindingResult, Model model) {
        product.setId(productId);
        Stream<String> streamOfPropNames = Pattern.compile(",").splitAsStream(propertyNames);
        Stream<String> streamOfPropValues = Pattern.compile(",").splitAsStream(propertyValues);
        String[] names = streamOfPropNames.map(String::trim).toArray(String[]::new);
        String[] values = streamOfPropValues.map(String::trim).toArray(String[]::new);

        validateProperties(bindingResult, names, values);

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("propertyNames", propertyNames);
            model.addAttribute("propertyValues", propertyValues);
            return "editProduct";
        }

        Set<Property> properties = createProperties(product, names, values);
        product.setProperties(properties);

        catalogService.editProduct(product);
        return "redirect:/admin/catalog";
    }

    public Set<Property> createProperties(Product product, String[] names, String[] values) {
        if (names.length == 0) {
            return new HashSet<>();
        }

        Set<Property> properties = new HashSet<>();
        for (int i = 0; i < names.length; i++) {
            properties.add(new Property(0, product, names[i], values[i]));
        }
        return properties;
    }

    public void validateProperties(BindingResult bindingResult, String[] names, String[] values) {
        if (names.length != values.length) {
            bindingResult
                    .rejectValue("properties", "error.property",
                            "*The number of names and values does not match");
        } else {
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals("") || values[i].equals("")) {
                    bindingResult
                            .rejectValue("properties", "error.property",
                                    "*Some property names or values are empty");
                }
            }
        }
    }
}
