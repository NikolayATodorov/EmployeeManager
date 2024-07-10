package com.nitdrv.employeemanager.service.mapper;

import com.nitdrv.employeemanager.domain.Employee;
import com.nitdrv.employeemanager.domain.EmployeeProject;
import com.nitdrv.employeemanager.domain.Project;
import com.nitdrv.employeemanager.service.dto.EmployeeDTO;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeProject} and its DTO {@link EmployeeProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeProjectMapper extends EntityMapper<EmployeeProjectDTO, EmployeeProject> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
    EmployeeProjectDTO toDto(EmployeeProject s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);
}
