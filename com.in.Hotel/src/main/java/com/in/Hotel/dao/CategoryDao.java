package com.in.Hotel.dao;

import com.in.Hotel.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {

    List<Category>getAllCategory();
}
