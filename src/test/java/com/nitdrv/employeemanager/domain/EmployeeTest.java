package com.nitdrv.employeemanager.domain;

import static com.nitdrv.employeemanager.domain.EmployeeProjectTestSamples.*;
import static com.nitdrv.employeemanager.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.employeemanager.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void projectsTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        EmployeeProject employeeProjectBack = getEmployeeProjectRandomSampleGenerator();

        employee.addProjects(employeeProjectBack);
        assertThat(employee.getProjects()).containsOnly(employeeProjectBack);
        assertThat(employeeProjectBack.getEmployee()).isEqualTo(employee);

        employee.removeProjects(employeeProjectBack);
        assertThat(employee.getProjects()).doesNotContain(employeeProjectBack);
        assertThat(employeeProjectBack.getEmployee()).isNull();

        employee.projects(new HashSet<>(Set.of(employeeProjectBack)));
        assertThat(employee.getProjects()).containsOnly(employeeProjectBack);
        assertThat(employeeProjectBack.getEmployee()).isEqualTo(employee);

        employee.setProjects(new HashSet<>());
        assertThat(employee.getProjects()).doesNotContain(employeeProjectBack);
        assertThat(employeeProjectBack.getEmployee()).isNull();
    }
}
