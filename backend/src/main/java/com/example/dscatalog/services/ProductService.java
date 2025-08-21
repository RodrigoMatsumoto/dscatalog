package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.ProductRepository;
import com.example.dscatalog.services.exceptions.DatabaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {
  
  private final ProductRepository repository;
  private final CategoryRepository categoryRepository;
  
  public ProductService(ProductRepository repository, CategoryRepository categoryRepository) {
    this.repository = repository;
    this.categoryRepository = categoryRepository;
  }
  
  @Transactional(readOnly = true)
  public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
    Page<Product> page = repository.findAll(pageRequest);
    
    return page.map(ProductDTO::new);
  }
  
  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Optional<Product> obj = repository.findById(id);
    
    Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    
    return new ProductDTO(entity, entity.getCategories());
  }
  
  @Transactional
  public ProductDTO insert(ProductDTO dto) {
    Product entity = new Product();
    
    copyDtoToEntity(dto, entity);
    
    entity = repository.save(entity);
    
    return new ProductDTO(entity);
  }
  
  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    try {
      Product entity = repository.getReferenceById(id);
      
      copyDtoToEntity(dto, entity);
      
      entity = repository.save(entity);
      
      return new ProductDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
  }
  
  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Integrity violation");
    }
  }
  
  private void copyDtoToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());
    entity.setDate(dto.getDate());
    
    entity.getCategories().clear();
    for (CategoryDTO categoryDTO : dto.getCategories()) {
      Category category = categoryRepository.getReferenceById(categoryDTO.getId());
      entity.getCategories().add(category);
    }
  }
  
}