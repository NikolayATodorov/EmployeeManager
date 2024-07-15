package com.nitdrv.employeemanager.repository;

import com.nitdrv.employeemanager.domain.EmployeeProject;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import java.util.List;
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

}
