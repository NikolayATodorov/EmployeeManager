package com.nitdrv.employeemanager.service.mapper;

import com.nitdrv.employeemanager.domain.Employee;
import com.nitdrv.employeemanager.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
