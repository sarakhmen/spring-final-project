package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminCatalogController {

    @Autowired
    CatalogService catalogService;

    @GetMapping("/catalog")
    public String catalogView(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
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

        return "adminCatalog";
    }

    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId){
        catalogService.deleteProductById(productId);
        return "redirect:/admin/catalog";
    }
}
