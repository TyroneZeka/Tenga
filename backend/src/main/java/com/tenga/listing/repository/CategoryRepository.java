package com.tenga.listing.repository;

import com.tenga.listing.model.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  @Query(
      "SELECT c FROM Category c WHERE c.parent IS NULL AND c.active = true ORDER BY c.sortOrder ASC")
  List<Category> findAllRootCategories();

  Optional<Category> findBySlug(String slug);

  List<Category> findByParentIdAndActiveTrue(UUID parentId);
}
