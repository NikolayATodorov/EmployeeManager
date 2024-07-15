package com.nitdrv.employeemanager.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nitdrv.employeemanager.domain.EmployeeProject} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeesPairWithCommonProjectsPeriodDTO implements Serializable {

    @NotNull
    private Long employee_id1;

    @NotNull
    private Long employee_id2;

    @NotNull
    private Integer total_overlap_days;

    public EmployeesPairWithCommonProjectsPeriodDTO(Long employee_id1, Long employee_id2, Integer total_overlap_days) {
        this.employee_id1 = employee_id1;
        this.employee_id2 = employee_id2;
        this.total_overlap_days = total_overlap_days;
    }

    public EmployeesPairWithCommonProjectsPeriodDTO() {}

    public Long getEmployee_id1() {
        return employee_id1;
    }

    public void setEmployee_id1(Long employee_id1) {
        this.employee_id1 = employee_id1;
    }

    public Long getEmployee_id2() {
        return employee_id2;
    }

    public void setEmployee_id2(Long employee_id2) {
        this.employee_id2 = employee_id2;
    }

    public Integer getTotal_overlap_days() {
        return total_overlap_days;
    }

    public void setTotal_overlap_days(Integer total_overlap_days) {
        this.total_overlap_days = total_overlap_days;
    }

    @Override
    public String toString() {
        return (
            "EmployeesPairWithCommonProjectsPeriodDTO{" +
            "employee_id1=" +
            employee_id1 +
            ", employee_id2=" +
            employee_id2 +
            ", total_overlap_days=" +
            total_overlap_days +
            '}'
        );
    }
}
