package com.nitdrv.employeemanager.service.mapper;

import com.nitdrv.employeemanager.domain.Project;
import com.nitdrv.employeemanager.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
