package com.nitdrv.employeemanager.repository;

import com.nitdrv.employeemanager.domain.EmployeeProject;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {
    @Query(name = "EmployeesPairWithCommonProjectsPeriod.findPairsWithLongestPeriodsOnCommonProjects", nativeQuery = true)
    List<EmployeesPairWithCommonProjectsPeriodDTO> findPairsWithLongestPeriodsOnCommonProjects();

    //    @Query(name = "EmployeesPairWithCommonProjectsPeriod.findPairWithLongestPeriodsOnCommonProjects", nativeQuery = true)
    //    EmployeesPairWithCommonProjectsPeriodDTO findPairWithLongestPeriodsOnCommonProjects();

    Optional<EmployeeProject> findByDateFromAndDateToAndEmployeeIdAndProjectId(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );

    Optional<EmployeeProject> findByDateFromBetweenAndEmployeeIdAndProjectId(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );

    Optional<EmployeeProject> findByDateToBetweenAndEmployeeIdAndProjectId(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    );
    //    Optional<EmployeeProject> findByDateFromBetweenOrDateToBetweenAndEmployeeIdAndProjectId(Instant dateFrom, Instant dateTo, Long employeeId, Long projectId);

}
