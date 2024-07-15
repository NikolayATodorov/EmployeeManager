package com.nitdrv.employeemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeProject.
 */
@Entity
//@NamedNativeQuery(name = "EmployeesPairWithCommonProjectsPeriod.findPairWithLongestPeriodsOnCommonProjects",
@NamedNativeQuery(
    name = "EmployeesPairWithCommonProjectsPeriod.findPairsWithLongestPeriodsOnCommonProjects",
    query = "WITH EmployeePairs AS (\n" +
    "    SELECT\n" +
    "        e1.employee_id AS employee_id1,\n" +
    "        e2.employee_id AS employee_id2,\n" +
    "        e1.project_id,\n" +
    "        GREATEST(e1.date_from, e2.date_from) AS overlap_start,\n" +
    "        LEAST(e1.date_to, e2.date_to) AS overlap_end\n" +
    "    FROM\n" +
    "        employee_project e1\n" +
    "    JOIN\n" +
    "        employee_project e2 ON e1.project_id = e2.project_id\n" +
    "    WHERE\n" +
    "        e1.employee_id < e2.employee_id\n" +
    "        AND GREATEST(e1.date_from, e2.date_from) <= LEAST(e1.date_to, e2.date_to)\n" +
    "),\n" +
    "OverlapPeriods AS (\n" +
    "-- total_overlap_days AS (\t\n" +
    "    SELECT\n" +
    "        employee_id1,\n" +
    "        employee_id2,\n" +
    "        SUM(EXTRACT(EPOCH FROM (overlap_end - overlap_start)) / 86400 + 1) AS total_overlap_days\n" +
    "    FROM\n" +
    "        EmployeePairs\n" +
    "    GROUP BY\n" +
    "        employee_id1, employee_id2\n" +
    ")\n" +
    "SELECT\n" +
    "    employee_id1,\n" +
    "    employee_id2,\n" +
    "    total_overlap_days\n" +
    "FROM\n" +
    "    OverlapPeriods\n" +
    "ORDER BY\n" +
    "    total_overlap_days DESC;",
    // add LIMIT 1 to get only the first record
    resultSetMapping = "Mapping.EmployeesPairWithCommonProjectsPeriodDTO"
)
@SqlResultSetMapping(
    name = "Mapping.EmployeesPairWithCommonProjectsPeriodDTO",
    classes = @ConstructorResult(
        targetClass = EmployeesPairWithCommonProjectsPeriodDTO.class,
        columns = {
            @ColumnResult(name = "employee_id1", type = Long.class),
            @ColumnResult(name = "employee_id2", type = Long.class),
            @ColumnResult(name = "total_overlap_days", type = Integer.class),
        }
    )
)
@Table(name = "employee_project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private Instant dateFrom;

    @Column(name = "date_to", nullable = false)
    private Instant dateTo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "projects" }, allowSetters = true)
    private Employee employee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "members" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeProject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFrom() {
        return this.dateFrom;
    }

    public EmployeeProject dateFrom(Instant dateFrom) {
        this.setDateFrom(dateFrom);
        return this;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return this.dateTo;
    }

    public EmployeeProject dateTo(Instant dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeProject employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public EmployeeProject project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeProject)) {
            return false;
        }
        return getId() != null && getId().equals(((EmployeeProject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeProject{" +
            "id=" + getId() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            "}";
    }
}
