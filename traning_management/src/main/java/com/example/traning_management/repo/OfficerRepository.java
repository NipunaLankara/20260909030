package com.example.traning_management.repo;



import com.example.traning_management.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
}