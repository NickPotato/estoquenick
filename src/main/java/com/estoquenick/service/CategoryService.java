package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.estoquenick.repository.CategoryRepo;
import com.estoquenick.model.Category;

import java.util.List;

//check productserv for info
@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category save(Category category) {
        //failsafe, category needs a name lol
        if (category.getName() == null || category.getName().isBlank()) {
            throw new RuntimeException("Category name is required");
        }
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
