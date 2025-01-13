package com.faithoyebode.dreamshops.repository;

import com.faithoyebode.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);

    boolean existsByName(String name);
}
