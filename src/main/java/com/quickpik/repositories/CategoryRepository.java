package com.quickpik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
