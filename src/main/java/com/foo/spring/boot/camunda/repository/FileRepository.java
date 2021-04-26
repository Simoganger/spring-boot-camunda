package com.foo.spring.boot.camunda.repository;

import com.foo.spring.boot.camunda.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository  extends JpaRepository<File, String> {
}
