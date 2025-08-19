package com.example.dscatalog.services;

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
  public List<Category> findAll() {
    return repository.findAll();
  }
  
}