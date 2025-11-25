package com.estoquenick.controller;

import com.estoquenick.dto.CategoryRequest;
import com.estoquenick.dto.CategoryResponse;
import com.estoquenick.dto.ProductResponse;
import com.estoquenick.service.CategoryService;
import com.estoquenick.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//check product controller for more info, quite similar

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired //do we really need this
    private ProductService productService; //yes we do, yogurt

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}") //check by id specifically
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/{categoryId}/products") //list products inside a category
    public List<ProductResponse> findProductsByCategory(@PathVariable Long categoryId) {
        return productService.findByCategoryId(categoryId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping //add a new one
    public CategoryResponse save(@RequestBody @Valid CategoryRequest dto) {
        return categoryService.save(dto);
    }

    @PutMapping("/{id}") //update an existing one
    public CategoryResponse update(@PathVariable Long id, @RequestBody @Valid CategoryRequest dto) {
        return categoryService.update(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
