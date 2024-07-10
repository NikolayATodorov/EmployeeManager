package com.nitdrv.employeemanager.domain;

import static com.nitdrv.employeemanager.domain.EmployeeProjectTestSamples.*;
import static com.nitdrv.employeemanager.domain.EmployeeTestSamples.*;
import static com.nitdrv.employeemanager.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.employeemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeProject.class);
        EmployeeProject employeeProject1 = getEmployeeProjectSample1();
        EmployeeProject employeeProject2 = new EmployeeProject();
        assertThat(employeeProject1).isNotEqualTo(employeeProject2);

        employeeProject2.setId(employeeProject1.getId());
        assertThat(employeeProject1).isEqualTo(employeeProject2);

        employeeProject2 = getEmployeeProjectSample2();
        assertThat(employeeProject1).isNotEqualTo(employeeProject2);
    }

    @Test
    void employeeTest() {
        EmployeeProject employeeProject = getEmployeeProjectRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employeeProject.setEmployee(employeeBack);
        assertThat(employeeProject.getEmployee()).isEqualTo(employeeBack);

        employeeProject.employee(null);
        assertThat(employeeProject.getEmployee()).isNull();
    }

    @Test
    void projectTest() {
        EmployeeProject employeeProject = getEmployeeProjectRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        employeeProject.setProject(projectBack);
        assertThat(employeeProject.getProject()).isEqualTo(projectBack);

        employeeProject.project(null);
        assertThat(employeeProject.getProject()).isNull();
    }
}
