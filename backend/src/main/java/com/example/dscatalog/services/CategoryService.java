package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
  
  private final CategoryRepository repository;
  
  public CategoryService(CategoryRepository repository) {
    this.repository = repository;
  }
  
  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll() {
    List<Category> list = repository.findAll();
    
    return list.stream().map(CategoryDTO::new).toList();
  }
  
}