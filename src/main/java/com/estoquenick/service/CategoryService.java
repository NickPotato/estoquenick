package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.estoquenick.repository.CategoryRepo;
import com.estoquenick.model.Category;
import com.estoquenick.dto.CategoryRequest;
import com.estoquenick.dto.CategoryResponse;

import java.util.List;

//check productserv for info, it's really similar to this
@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id) //finds the category by its id
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        
        return toResponse(category); //returns that in dto form
    }

    public CategoryResponse save(CategoryRequest dto) {
        Category category = new Category(); //new category object
        category.setName(dto.name()); //set its name to the name we got from the dto argument
        categoryRepository.save(category); //(i'd forgotten to actually save it into the repository...)
        return toResponse(category); //return the category we just created, in dto form
    }

    //nearly the same deal as save
    public CategoryResponse update(Long id, CategoryRequest dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(dto.name());
        categoryRepository.save(category); //make sure to save it over the one we had before
        return toResponse(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    //transforms into dto
    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
            c.getId(),
            c.getName()
        );
    }
}
