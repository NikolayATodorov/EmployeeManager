package com.nitdrv.employeemanager.service;

import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nitdrv.employeemanager.domain.EmployeeProject}.
 */
public interface EmployeeProjectService {
    /**
     * Save a employeeProject.
     *
     * @param employeeProjectDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeProjectDTO save(EmployeeProjectDTO employeeProjectDTO);

    /**
     * Updates a employeeProject.
     *
     * @param employeeProjectDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeProjectDTO update(EmployeeProjectDTO employeeProjectDTO);

    /**
     * Partially updates a employeeProject.
     *
     * @param employeeProjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeProjectDTO> partialUpdate(EmployeeProjectDTO employeeProjectDTO);

    /**
     * Get all the employeeProjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeProjectDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employeeProject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeProjectDTO> findOne(Long id);

    /**
     * Delete the "id" employeeProject.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
