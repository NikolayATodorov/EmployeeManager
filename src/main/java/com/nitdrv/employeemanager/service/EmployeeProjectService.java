package com.nitdrv.employeemanager.service;

import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import java.time.Instant;
import java.util.List;
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

    List<EmployeesPairWithCommonProjectsPeriodDTO> findPairsWithLongestPeriodsOnCommonProjects();

    /**
     * Get the employeeProject.
     *
     * @param dateFrom the dateFrom of the entity.
     * @param dateTo the dateTo of the entity.
     * @param employeeId the employeeId of the entity.
     * @param projectId the projectId of the entity.
     *
     * @return the entity.
     */
    Optional<EmployeeProjectDTO> findByDateFromAndDateToAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );

    /**
     * Get the employeeProject.
     *
     * @param dateFrom the dateFrom of the entity.
     * @param dateTo the dateTo of the entity.
     * @param employeeId the employeeId of the entity.
     * @param projectId the projectId of the entity.
     *
     * @return the entity.
     */
    Optional<EmployeeProjectDTO> findByDateFromBetweenAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );

    /**
     * Get the employeeProject.
     *
     * @param dateFrom the dateFrom of the entity.
     * @param dateTo the dateTo of the entity.
     * @param employeeId the employeeId of the entity.
     * @param projectId the projectId of the entity.
     *
     * @return the entity.
     */
    Optional<EmployeeProjectDTO> findByDateToBetweenAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );
    //    /**
    //     * Get the employeeProject.
    //     *
    //     * @param dateFrom the dateFrom of the entity.
    //     * @param dateTo the dateTo of the entity.
    //     * @param employeeId the employeeId of the entity.
    //     * @param projectId the projectId of the entity.
    //     *
    //     * @return the entity.
    //     */
    //    Optional<EmployeeProjectDTO> findByDateFromBetweenOrDateToBetweenAndEmployeeAndProject(Instant dateFrom, Instant dateTo, Long employeeId, Long projectId);

}
