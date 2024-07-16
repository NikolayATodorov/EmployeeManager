package com.nitdrv.employeemanager.web.rest;

import com.nitdrv.employeemanager.domain.Employee;
import com.nitdrv.employeemanager.domain.Project;
import com.nitdrv.employeemanager.repository.EmployeeProjectRepository;
import com.nitdrv.employeemanager.repository.EmployeeRepository;
import com.nitdrv.employeemanager.repository.ProjectRepository;
import com.nitdrv.employeemanager.service.EmployeeProjectService;
import com.nitdrv.employeemanager.service.EmployeeService;
import com.nitdrv.employeemanager.service.ProjectService;
import com.nitdrv.employeemanager.service.dto.EmployeeDTO;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.dto.EmployeesPairWithCommonProjectsPeriodDTO;
import com.nitdrv.employeemanager.service.dto.ProjectDTO;
import com.nitdrv.employeemanager.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nitdrv.employeemanager.domain.EmployeeProject}.
 */
@RestController
@RequestMapping("/api/employee-projects")
public class EmployeeProjectResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeProjectResource.class);

    private static final String ENTITY_NAME = "employeeProject";
    private static final String ENTITY_NAME_EMPLOYEE = "employee";
    private static final String ENTITY_NAME_PROJECT = "project";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeProjectService employeeProjectService;

    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeService employeeService;

    private final ProjectService projectService;

    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository;

    public EmployeeProjectResource(
        EmployeeProjectService employeeProjectService,
        EmployeeProjectRepository employeeProjectRepository,
        EmployeeService employeeService,
        ProjectService projectService,
        EmployeeRepository employeeRepository,
        ProjectRepository projectRepository
    ) {
        this.employeeProjectService = employeeProjectService;
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * {@code POST  /employee-projects} : Create a new employeeProject.
     *
     * @param employeeProjectDTO the employeeProjectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeProjectDTO, or with status {@code 400 (Bad Request)} if the employeeProject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeProjectDTO> createEmployeeProject(@Valid @RequestBody EmployeeProjectDTO employeeProjectDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeProject : {}", employeeProjectDTO);
        if (employeeProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeProjectDTO = employeeProjectService.save(employeeProjectDTO);
        return ResponseEntity.created(new URI("/api/employee-projects/" + employeeProjectDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, employeeProjectDTO.getId().toString()))
            .body(employeeProjectDTO);
    }

    /**
     * {@code PUT  /employee-projects/:id} : Updates an existing employeeProject.
     *
     * @param id                 the id of the employeeProjectDTO to save.
     * @param employeeProjectDTO the employeeProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeProjectDTO,
     * or with status {@code 400 (Bad Request)} if the employeeProjectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProjectDTO> updateEmployeeProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeProjectDTO employeeProjectDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeProject : {}, {}", id, employeeProjectDTO);
        if (employeeProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeProjectDTO = employeeProjectService.update(employeeProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeProjectDTO.getId().toString()))
            .body(employeeProjectDTO);
    }

    /**
     * {@code PATCH  /employee-projects/:id} : Partial updates given fields of an existing employeeProject, field will ignore if it is null
     *
     * @param id                 the id of the employeeProjectDTO to save.
     * @param employeeProjectDTO the employeeProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeProjectDTO,
     * or with status {@code 400 (Bad Request)} if the employeeProjectDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeProjectDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeProjectDTO> partialUpdateEmployeeProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeProjectDTO employeeProjectDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeProject partially : {}, {}", id, employeeProjectDTO);
        if (employeeProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeProjectDTO> result = employeeProjectService.partialUpdate(employeeProjectDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeProjectDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-projects} : get all the employeeProjects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeProjects in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeProjectDTO>> getAllEmployeeProjects(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeProjects");
        Page<EmployeeProjectDTO> page = employeeProjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-projects/:id} : get the "id" employeeProject.
     *
     * @param id the id of the employeeProjectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeProjectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProjectDTO> getEmployeeProject(@PathVariable("id") Long id) {
        log.debug("REST request to get EmployeeProject : {}", id);
        Optional<EmployeeProjectDTO> employeeProjectDTO = employeeProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeProjectDTO);
    }

    /**
     * {@code DELETE  /employee-projects/:id} : delete the "id" employeeProject.
     *
     * @param id the id of the employeeProjectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeProject(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmployeeProject : {}", id);
        employeeProjectService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /employee-projects/emp-pairs-with-longest-periods-on-common-projects : get the
     * employee pairs with longest periods working on common projects
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of EmployeesPairWithCommonProjectsPeriodDTOs
     * or with status {@code 404 (OK)} not found}.
     */
    @GetMapping("/emp-pairs-with-longest-periods-on-common-projects")
    public ResponseEntity<?> getEmployeePairsWithLongestPeriodsWorkingOnCommonProjects() {
        log.debug("REST request to get EmployeeProject : {}");

        List<EmployeesPairWithCommonProjectsPeriodDTO> result = new ArrayList<>();
        result = employeeProjectService.findPairsWithLongestPeriodsOnCommonProjects();
        if (result.isEmpty()) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/load-file")
    public ResponseEntity<?> loadFile(@RequestBody String absoluteFilePath) throws FileNotFoundException {
        log.debug("REST request to get EmployeeProject : {}");

        List<String> result = new ArrayList<>();

        File file = new File(absoluteFilePath);
        Scanner sc = new Scanner(file);
        int lineCounter = 0;
        while (sc.hasNextLine()) {
            lineCounter++;
            String nextLine = sc.nextLine();
            // validate line
            // String[] values = nextLine.split(COMMA_DELIMITER);
            String[] values = nextLine.split(",");
            if (values.length != 4) {
                // add line to the list of lines with wrong format
                throw new BadRequestAlertException(
                    "Wrong line on line " + lineCounter + " ! Expected comma separated values: " + "EmpID, ProjectID, DateFrom, DateTo",
                    ENTITY_NAME,
                    "wronglineformat"
                );
            }
            EmployeeProjectDTO dto = new EmployeeProjectDTO();
            // get employee & project
            Optional<EmployeeDTO> employee = employeeService.findOne(Long.parseLong(values[0].trim()));
            if (employee.isEmpty()) {
                throw new BadRequestAlertException(
                    "Employee with ID " + " on line " + lineCounter + " does not exist!",
                    ENTITY_NAME_EMPLOYEE,
                    "employeedoesnotexist"
                );
            }
            Optional<ProjectDTO> project = projectService.findOne(Long.parseLong(values[1].trim()));
            if (project.isEmpty()) {
                throw new BadRequestAlertException(
                    "Project with ID " + " on line " + lineCounter + " does not exist!",
                    ENTITY_NAME_PROJECT,
                    "projectedoesnotexist"
                );
            }

            dto.setEmployee((employee.get()));
            dto.setProject(project.get());

            try {
                dto.setDateFrom(parseDate(values[2].trim(), LocalTime.MIN));
            } catch (DateTimeParseException e) {
                throw new BadRequestAlertException(
                    "Wrong date format for DateFrom on line " +
                    lineCounter +
                    " ! " +
                    "Supported date formats: [MM/dd/yyyy]; [dd-MM-yyyy]; [yyyy-MM-dd]; [yyyy-MM-dd'T'HH:mm:ss]; [yyyyMMdd]",
                    ENTITY_NAME,
                    "wrongdateformat"
                );
            }
            if ("null".equalsIgnoreCase(values[3].trim())) {
                dto.setDateTo(null);
            } else try {
                dto.setDateTo(parseDate(values[3].trim(), LocalTime.MIN));
            } catch (DateTimeParseException e) {
                throw new BadRequestAlertException(
                    "Wrong date format for DateTo on line " +
                    lineCounter +
                    " ! " +
                    "Supported date formats: [MM/dd/yyyy]; [dd-MM-yyyy]; [yyyy-MM-dd]; [yyyy-MM-dd'T'HH:mm:ss]; [yyyyMMdd]; NULL",
                    ENTITY_NAME,
                    "wrongdateformat"
                );
            }

            if (dto.getDateTo() != null && dto.getDateFrom().isAfter(dto.getDateTo())) {
                throw new BadRequestAlertException(
                    "DateFrom is after DateTo on line " + lineCounter + " !",
                    ENTITY_NAME,
                    "DateFromisafterDateTo"
                );
            }

            // check if the entry already exists
            Optional<EmployeeProjectDTO> ep1 = employeeProjectService.findByDateFromAndDateToAndEmployeeAndProject(
                dto.getDateFrom(),
                dto.getDateTo(),
                dto.getEmployee().getId(),
                dto.getProject().getId()
            );

            // if entry already exists don't save it

            // check if an entry for the same employee and the same project exists and the periods overlap
            Optional<EmployeeProjectDTO> ep2 = employeeProjectService.findByDateFromBetweenAndEmployeeAndProject(
                dto.getDateFrom(),
                dto.getDateTo(),
                dto.getEmployee().getId(),
                dto.getProject().getId()
            );
            Optional<EmployeeProjectDTO> ep3 = employeeProjectService.findByDateToBetweenAndEmployeeAndProject(
                dto.getDateFrom(),
                dto.getDateTo(),
                dto.getEmployee().getId(),
                dto.getProject().getId()
            );

            if (ep1.isEmpty() && ep2.isEmpty() && ep3.isEmpty()) {
                EmployeeProjectDTO saved = employeeProjectService.save(dto);
            } else {
                if (result.isEmpty()) {
                    result.add("The following lines of your input file were not saved due to the explained issues: ");
                }
                if (!ep1.isEmpty()) {
                    // entry already exists
                    result.add("Entry " + nextLine + " already exists.");
                }
                if (!ep2.isEmpty()) {
                    // period overlaps
                    result.add(
                        "For Entry " + nextLine + " period overlaps for the same employee and the same project with " + ep2.toString()
                    );
                }
                if (!ep3.isEmpty()) {
                    // period overlaps
                    result.add(
                        "For Entry " + nextLine + " period overlaps for the same employee and the same project with " + ep3.toString()
                    );
                }
            }
        }
        return ResponseEntity.ok().body(result);
    }

    // move to a util class and use application property in yml for the date pattern
    private Instant parseDate(String string, LocalTime defaultTime) {
        return new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[MM/dd/yyyy]" + "[dd-MM-yyyy]" + "[yyyy-MM-dd]" + "[yyyyMMdd]"))
            .parseDefaulting(ChronoField.HOUR_OF_DAY, defaultTime.getHour())
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, defaultTime.getMinute())
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, defaultTime.getSecond())
            .parseDefaulting(ChronoField.NANO_OF_SECOND, defaultTime.getNano())
            .toFormatter()
            .withZone(ZoneId.of("UTC"))
            .parse(string, Instant::from);
    }
}
