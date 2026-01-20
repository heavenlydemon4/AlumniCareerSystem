package com.alumni.career.repository;

import com.alumni.career.model.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {
    Optional<Alumni> findByUser_UserId(Long userId);
    List<Alumni> findByGraduationYear(Integer year);
}
