package com.nitdrv.employeemanager.domain;

import static com.nitdrv.employeemanager.domain.EmployeeProjectTestSamples.*;
import static com.nitdrv.employeemanager.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.employeemanager.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void membersTest() {
        Project project = getProjectRandomSampleGenerator();
        EmployeeProject employeeProjectBack = getEmployeeProjectRandomSampleGenerator();

        project.addMembers(employeeProjectBack);
        assertThat(project.getMembers()).containsOnly(employeeProjectBack);
        assertThat(employeeProjectBack.getProject()).isEqualTo(project);

        project.removeMembers(employeeProjectBack);
        assertThat(project.getMembers()).doesNotContain(employeeProjectBack);
        assertThat(employeeProjectBack.getProject()).isNull();

        project.members(new HashSet<>(Set.of(employeeProjectBack)));
        assertThat(project.getMembers()).containsOnly(employeeProjectBack);
        assertThat(employeeProjectBack.getProject()).isEqualTo(project);

        project.setMembers(new HashSet<>());
        assertThat(project.getMembers()).doesNotContain(employeeProjectBack);
        assertThat(employeeProjectBack.getProject()).isNull();
    }
}
