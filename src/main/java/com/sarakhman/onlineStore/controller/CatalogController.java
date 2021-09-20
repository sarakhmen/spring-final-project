package com.sarakhman.onlineStore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CatalogController {

    @GetMapping("/catalog")
    public String showCatalog(){
        return "catalog";
    }
}
