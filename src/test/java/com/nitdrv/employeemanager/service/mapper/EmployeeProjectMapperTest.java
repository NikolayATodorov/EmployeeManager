package com.nitdrv.employeemanager.service.mapper;

import static com.nitdrv.employeemanager.domain.EmployeeProjectAsserts.*;
import static com.nitdrv.employeemanager.domain.EmployeeProjectTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeProjectMapperTest {

    private EmployeeProjectMapper employeeProjectMapper;

    @BeforeEach
    void setUp() {
        employeeProjectMapper = new EmployeeProjectMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeProjectSample1();
        var actual = employeeProjectMapper.toEntity(employeeProjectMapper.toDto(expected));
        assertEmployeeProjectAllPropertiesEquals(expected, actual);
    }
}
