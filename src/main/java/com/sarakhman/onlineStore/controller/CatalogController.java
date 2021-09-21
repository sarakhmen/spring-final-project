package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @GetMapping("/catalog")
    public String showCatalog(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(name = "size", required = false, defaultValue = "10") int size){
        Page<Product> productPage = catalogService.findAllProducts(PageRequest.of(page - 1, size));
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);


        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "catalog";
    }
}
