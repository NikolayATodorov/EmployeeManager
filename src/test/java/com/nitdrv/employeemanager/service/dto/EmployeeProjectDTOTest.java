package com.nitdrv.employeemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.employeemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeProjectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeProjectDTO.class);
        EmployeeProjectDTO employeeProjectDTO1 = new EmployeeProjectDTO();
        employeeProjectDTO1.setId(1L);
        EmployeeProjectDTO employeeProjectDTO2 = new EmployeeProjectDTO();
        assertThat(employeeProjectDTO1).isNotEqualTo(employeeProjectDTO2);
        employeeProjectDTO2.setId(employeeProjectDTO1.getId());
        assertThat(employeeProjectDTO1).isEqualTo(employeeProjectDTO2);
        employeeProjectDTO2.setId(2L);
        assertThat(employeeProjectDTO1).isNotEqualTo(employeeProjectDTO2);
        employeeProjectDTO1.setId(null);
        assertThat(employeeProjectDTO1).isNotEqualTo(employeeProjectDTO2);
    }
}
