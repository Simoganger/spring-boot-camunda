package com.foo.spring.boot.camunda.repository;

import com.foo.spring.boot.camunda.model.CustomProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomProcessRepository extends JpaRepository<CustomProcess, String> {
}
