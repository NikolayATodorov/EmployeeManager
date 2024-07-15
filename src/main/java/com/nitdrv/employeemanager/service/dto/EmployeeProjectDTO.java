package com.nitdrv.employeemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nitdrv.employeemanager.domain.EmployeeProject} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeProjectDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateFrom;

    private Instant dateTo;

    @NotNull
    private EmployeeDTO employee;

    @NotNull
    private ProjectDTO project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeProjectDTO)) {
            return false;
        }

        EmployeeProjectDTO employeeProjectDTO = (EmployeeProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeProjectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeProjectDTO{" +
            "id=" + getId() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", employee=" + getEmployee() +
            ", project=" + getProject() +
            "}";
    }
}
