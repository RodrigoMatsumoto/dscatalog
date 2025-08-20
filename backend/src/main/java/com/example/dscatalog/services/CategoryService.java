package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
  
  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    Optional<Category> obj = repository.findById(id);
    
    Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    
    return new CategoryDTO(entity);
  }
  
}