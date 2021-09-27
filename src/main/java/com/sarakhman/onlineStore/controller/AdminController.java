package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.*;
import com.sarakhman.onlineStore.service.CatalogService;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.service.PropertyService;
import com.sarakhman.onlineStore.service.UserService;
import com.sarakhman.onlineStore.util.PaginationUtil;
import com.sarakhman.onlineStore.util.ProductPropertyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Processes admin requests.
 */
@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    PropertyService propertyService;

    @GetMapping("/catalog")
    public String catalogView(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                              @RequestParam(name = "priceFrom", required = false, defaultValue = "0") double priceFrom,
                              @RequestParam(name = "priceTo", required = false, defaultValue = "1.7976931348623157E308")
                                      double priceTo, HttpServletRequest request){
        double[] priceFromTo = ProductPropertyUtil.retrieveAndProcessPriceRange(priceFrom, priceTo, request);
        if(priceFromTo[0] > priceFromTo[1]){
            log.error("priceFrom greater than priceTo");
            return "redirect:/admin/catalog?priceError=true";
        }

        Map<String, Set<String>> allProperties = propertyService.findPropertyGroups();
        model.addAttribute("properties", allProperties);

        Sort sort = catalogService.getSort((String)request.getSession().getAttribute("sort"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Map<String, Set<String>> selectedProperties = ProductPropertyUtil.retrieveAndProcessSelectedProperties(
                allProperties, request);
        Page<Product> productPage = catalogService
                .findProductsSortedAndByPropertiesAndPrice(selectedProperties, priceFromTo[0], priceFromTo[1], pageable);

        PaginationUtil.setPaginationAttributes(model, productPage, page, size);
        log.info("adminCatalog page successfully set up");
        return "adminCatalog";
    }

    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId){
        catalogService.deleteProductById(productId);
        log.info("Product with id = " + productId + " successfully deleted");
        return "redirect:/admin/catalog";
    }

    @GetMapping("/catalog/sort")
    public String changeSortOrder(@RequestParam(name = "sort", required = false, defaultValue = "byNameAZ") String sort,
                                  HttpServletRequest request){
        request.getSession().setAttribute("sort", sort);
        log.info("Sort method successfully changed");
        return "redirect:/admin/catalog";
    }

    @GetMapping("/management/cart/view/{userId}")
    public String showCart(@PathVariable("userId") long userId, Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                           HttpServletRequest request){
        HttpSession session = request.getSession();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderService.findAllOrdersByUserId(userId, pageable);

        PaginationUtil.setPaginationAttributes(model, orderPage, page, size);
        session.setAttribute("cartUserId", userId);

        log.info("Admin cart page successfully set up");
        return "adminCart";
    }

    @PostMapping("/management/cart/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request){
        HttpSession session = request.getSession();
        orderService.deleteOrder(orderId);
        log.info("Admin successfully deleted order with id = " + orderId);
        return "redirect:/admin/management/cart/view/" + session.getAttribute("cartUserId");
    }

    @PostMapping("/management/cart/order/{orderId}/{status}")
    public String setOrderStatus(@PathVariable("orderId") Long orderId, @PathVariable("status") Status status,
                                 HttpServletRequest request){
        HttpSession session = request.getSession();
        orderService.updateOrderStatus(orderId, status);
        log.info("Order status successfully changed");
        return "redirect:/admin/management/cart/view/" + session.getAttribute("cartUserId");
    }

    @PostMapping("/management/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId ,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        orderService.saveOrder(user.getId(), productId);
        log.info("Product with id = " + productId + " successfully added to cart");
        return "redirect:/admin/catalog";
    }

    @GetMapping("/management")
    public String managementView(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                 HttpServletRequest request){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userService.findAllUsers(pageable);
        PaginationUtil.setPaginationAttributes(model, userPage, page, size);
        log.info("Management page successfully set up");
        return "adminManagement";
    }

    @PostMapping("/management/block")
    public String setUserActiveStatus(@RequestParam("userId") long userId){
        userService.banHandler(userId);
        log.info("User with id = " + userId + " successfully blocked");
        return "redirect:/admin/management";
    }

    @GetMapping("/product/add")
    public String addProductView(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        log.info("Product page successfully set up");
        return "addProduct";
    }

    @GetMapping("/product/edit/{productId}")
    public String editProductView(@PathVariable("productId") long productId, Model model) {
        Product product = catalogService.findProductById(productId);

        String propertyNames = product.getProperties().stream().map(Property::getPropertyName)
                .collect(Collectors.joining(","));
        String propertyValues = product.getProperties().stream().map(Property::getPropertyValue)
                .collect(Collectors.joining(","));

        model.addAttribute("product", product);
        model.addAttribute("propertyNames", propertyNames);
        model.addAttribute("propertyValues", propertyValues);
        log.info("Edit page for product with id = " + productId + " successfully set up");
        return "editProduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@RequestParam(name = "propertyNames", required = false, defaultValue = "") String propertyNames,
                             @RequestParam(name = "propertyValues", required = false, defaultValue = "") String propertyValues,
                             @Valid Product product, BindingResult bindingResult) {
        Stream<String> streamOfPropNames = Pattern.compile(",").splitAsStream(propertyNames);
        Stream<String> streamOfPropValues = Pattern.compile(",").splitAsStream(propertyValues);
        String[] names = streamOfPropNames.map(String::trim).toArray(String[]::new);
        String[] values = streamOfPropValues.map(String::trim).toArray(String[]::new);
        validateProperties(bindingResult, names, values);

        if (bindingResult.hasErrors()) {
            log.error("Errors: " + bindingResult.getAllErrors());
            return "addProduct";
        }

        Set<Property> properties = createProperties(product, names, values);
        product.setProperties(properties);
        catalogService.saveProduct(product);
        log.info("Product successfully added");
        return "redirect:/admin/catalog";
    }

    @PostMapping("/product/edit/{productId}")
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
            log.error("Errors: " + bindingResult.getAllErrors());
            return "editProduct";
        }

        Set<Property> properties = createProperties(product, names, values);
        product.setProperties(properties);

        catalogService.editProduct(product);
        log.info("Product with id = " + productId + " successfully edited");
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
