package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.service.CatalogService;
import com.sarakhman.onlineStore.service.PropertyService;
import com.sarakhman.onlineStore.util.PaginationUtil;
import com.sarakhman.onlineStore.util.ProductPropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@Controller
public class CatalogController {

    @Autowired
    CatalogService catalogService;
    @Autowired
    PropertyService propertyService;

    @GetMapping("/user/catalog")
    public String catalogView(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                              @RequestParam(name = "priceFrom", required = false, defaultValue = "0") double priceFrom,
                              @RequestParam(name = "priceTo", required = false, defaultValue = "1.7976931348623157E308")
                                          double priceTo, HttpServletRequest request){
        double[] priceFromTo = ProductPropertyUtil.retrieveAndProcessPriceRange(priceFrom, priceTo, request);
        if(priceFromTo[0] > priceFromTo[1]){
            return "redirect:/user/catalog?priceError=true";
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

        return "catalog";
    }

    @GetMapping("/user/catalog/sort")
    public String changeSortOrder(@RequestParam(name = "sort", required = false, defaultValue = "byNameAZ") String sort,
                                  HttpServletRequest request){
        request.getSession().setAttribute("sort", sort);
        return "redirect:/user/catalog";
    }
}
