package com.nitdrv.employeemanager.service.impl;

import com.nitdrv.employeemanager.domain.EmployeeProject;
import com.nitdrv.employeemanager.repository.EmployeeProjectRepository;
import com.nitdrv.employeemanager.service.EmployeeProjectService;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import com.nitdrv.employeemanager.service.mapper.EmployeeProjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nitdrv.employeemanager.domain.EmployeeProject}.
 */
@Service
@Transactional
public class EmployeeProjectServiceImpl implements EmployeeProjectService {

    private final Logger log = LoggerFactory.getLogger(EmployeeProjectServiceImpl.class);

    private final EmployeeProjectRepository employeeProjectRepository;

    private final EmployeeProjectMapper employeeProjectMapper;

    //    private final EmployeesPairWithCommonProjectsPeriodRepository еmployeesPairWithCommonProjectsPeriodRepository;

    public EmployeeProjectServiceImpl(EmployeeProjectRepository employeeProjectRepository, EmployeeProjectMapper employeeProjectMapper) {
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeProjectMapper = employeeProjectMapper;
    }

    @Override
    public EmployeeProjectDTO save(EmployeeProjectDTO employeeProjectDTO) {
        log.debug("Request to save EmployeeProject : {}", employeeProjectDTO);
        EmployeeProject employeeProject = employeeProjectMapper.toEntity(employeeProjectDTO);
        employeeProject = employeeProjectRepository.save(employeeProject);
        return employeeProjectMapper.toDto(employeeProject);
    }

    @Override
    public EmployeeProjectDTO update(EmployeeProjectDTO employeeProjectDTO) {
        log.debug("Request to update EmployeeProject : {}", employeeProjectDTO);
        EmployeeProject employeeProject = employeeProjectMapper.toEntity(employeeProjectDTO);
        employeeProject = employeeProjectRepository.save(employeeProject);
        return employeeProjectMapper.toDto(employeeProject);
    }

    @Override
    public Optional<EmployeeProjectDTO> partialUpdate(EmployeeProjectDTO employeeProjectDTO) {
        log.debug("Request to partially update EmployeeProject : {}", employeeProjectDTO);

        return employeeProjectRepository
            .findById(employeeProjectDTO.getId())
            .map(existingEmployeeProject -> {
                employeeProjectMapper.partialUpdate(existingEmployeeProject, employeeProjectDTO);

                return existingEmployeeProject;
            })
            .map(employeeProjectRepository::save)
            .map(employeeProjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeProjects");
        return employeeProjectRepository.findAll(pageable).map(employeeProjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeProjectDTO> findOne(Long id) {
        log.debug("Request to get EmployeeProject : {}", id);
        return employeeProjectRepository.findById(id).map(employeeProjectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeProject : {}", id);
        employeeProjectRepository.deleteById(id);
    }

    public List<EmployeesPairWithCommonProjectsPeriodDTO> findPairsWithLongestPeriodsOnCommonProjects() {
        log.debug("Request to find Employees pairs with longest periods spent working on common projects" + "(in descending order) : {}");
        List<EmployeesPairWithCommonProjectsPeriodDTO> list = employeeProjectRepository.findPairsWithLongestPeriodsOnCommonProjects();
        if (list == null && list.isEmpty()) {
            return list;
        }
        List<EmployeesPairWithCommonProjectsPeriodDTO> result = new ArrayList<>();
        result.add(list.remove(0));
        // if multiple pairs with the same max period - return them all
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (result.get(i).getTotal_overlap_days().intValue() == list.get(i).getTotal_overlap_days().intValue()) {
                    result.add(list.get(i));
                } else break;
            }
        }
        for (EmployeesPairWithCommonProjectsPeriodDTO el : list) {
            System.out.println(el.toString());
        }
        return result;
    }

    @Override
    public Optional<EmployeeProjectDTO> findByDateFromAndDateToAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    ) {
        Optional<EmployeeProject> ep = employeeProjectRepository.findByDateFromAndDateToAndEmployeeIdAndProjectId(
            dateFrom,
            dateTo,
            employeeId,
            projectId
        );
        return ep.map(employeeProjectMapper::toDto);
    }

    @Override
    public Optional<EmployeeProjectDTO> findByDateFromBetweenAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    ) {
        return employeeProjectRepository
            .findByDateFromBetweenAndEmployeeIdAndProjectId(dateFrom, dateTo, employeeId, projectId)
            .map(employeeProjectMapper::toDto);
    }

    @Override
    public Optional<EmployeeProjectDTO> findByDateToBetweenAndEmployeeAndProject(
        Instant dateFrom,
        Instant dateTo,
        Long employeeId,
        Long projectId
    ) {
        return employeeProjectRepository
            .findByDateToBetweenAndEmployeeIdAndProjectId(dateFrom, dateTo, employeeId, projectId)
            .map(employeeProjectMapper::toDto);
    }
    //    @Override
    //    public Optional<EmployeeProjectDTO> findByDateFromBetweenOrDateToBetweenAndEmployeeIdAndProjectId(Instant dateFrom, Instant dateTo, Long employeeId, Long projectId) {
    //        return employeeProjectRepository.findByDateFromBetweenOrDateToBetweenAndEmployeeIdAndProjectId(dateFrom, dateTo, employeeId, projectId).map(employeeProjectMapper::toDto);
    //    }

    //    public EmployeesPairWithCommonProjectsPeriodDTO getPair() {
    //        log.debug("Request to find Employees pair with longest periods spent working on common projects : {}");
    //        EmployeesPairWithCommonProjectsPeriodDTO pair = employeeProjectRepository.findPairWithLongestPeriodsOnCommonProjects();
    //        System.out.println(pair.toString());
    //        return pair;
    //    }

}
