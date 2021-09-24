package com.sarakhman.onlineStore.util;

import com.sarakhman.onlineStore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtil {
    private PaginationUtil(){}

    public static <T> void setPaginationAttributes(Model model, Page<T> page, int currentPage, int size){
        model.addAttribute("page", page);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", size);

        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

    public static <T> Page<T> getPageOf(Pageable pageable, List<T> objects){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        int objectsSize;

        if (objects == null || objects.size() < startItem) {
            objectsSize = 0;
            objects = Collections.emptyList();
        } else {
            objectsSize = objects.size();
            int toIndex = Math.min(startItem + pageSize, objectsSize);
            objects = objects.subList(startItem, toIndex);
        }

        return new PageImpl<>(objects, PageRequest.of(currentPage, pageSize), objectsSize);
    }

}
