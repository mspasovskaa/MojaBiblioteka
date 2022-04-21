package com.finki.library.repository;

import com.finki.library.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {
List<Copy> findAllByBook_Id(Long id);
}
