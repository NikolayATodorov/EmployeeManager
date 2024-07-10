package com.nitdrv.employeemanager.repository;

import com.nitdrv.employeemanager.domain.EmployeeProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {}
