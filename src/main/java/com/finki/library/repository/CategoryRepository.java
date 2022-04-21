package com.finki.library.repository;

import com.finki.library.model.Book;
import com.finki.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
Category findByName(String name);
List<Category> findAllByBooksNotContaining(Book book);
}
