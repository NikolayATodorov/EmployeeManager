package com.nitdrv.employeemanager.web.rest;

import com.nitdrv.employeemanager.repository.EmployeeProjectRepository;
import com.nitdrv.employeemanager.service.EmployeeProjectService;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeProjectService employeeProjectService;

    private final EmployeeProjectRepository employeeProjectRepository;

    public EmployeeProjectResource(EmployeeProjectService employeeProjectService, EmployeeProjectRepository employeeProjectRepository) {
        this.employeeProjectService = employeeProjectService;
        this.employeeProjectRepository = employeeProjectRepository;
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
     * @param id the id of the employeeProjectDTO to save.
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
     * @param id the id of the employeeProjectDTO to save.
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
}
