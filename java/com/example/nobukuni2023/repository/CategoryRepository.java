
package com.example.nobukuni2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Category;

public interface CategoryRepository extends JpaRepository<Category , Integer>{
	public  void findByid(Category category);
	public  void findByname(String category);
}
