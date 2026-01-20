package com.alumni.career.repository;

import com.alumni.career.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByIsActiveTrue();
    List<Job> findByCompanyNameContainingIgnoreCase(String companyName);
    List<Job> findByJobTitleContainingIgnoreCase(String jobTitle);
}
