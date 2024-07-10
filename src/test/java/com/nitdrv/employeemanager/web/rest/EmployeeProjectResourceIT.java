package com.nitdrv.employeemanager.web.rest;

import static com.nitdrv.employeemanager.domain.EmployeeProjectAsserts.*;
import static com.nitdrv.employeemanager.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitdrv.employeemanager.IntegrationTest;
import com.nitdrv.employeemanager.domain.Employee;
import com.nitdrv.employeemanager.domain.EmployeeProject;
import com.nitdrv.employeemanager.domain.Project;
import com.nitdrv.employeemanager.repository.EmployeeProjectRepository;
import com.nitdrv.employeemanager.service.dto.EmployeeProjectDTO;
import com.nitdrv.employeemanager.service.mapper.EmployeeProjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeProjectResourceIT {

    private static final Instant DEFAULT_DATE_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employee-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private EmployeeProjectMapper employeeProjectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeProjectMockMvc;

    private EmployeeProject employeeProject;

    private EmployeeProject insertedEmployeeProject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeProject createEntity(EntityManager em) {
        EmployeeProject employeeProject = new EmployeeProject().dateFrom(DEFAULT_DATE_FROM).dateTo(DEFAULT_DATE_TO);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeProject.setEmployee(employee);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        employeeProject.setProject(project);
        return employeeProject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeProject createUpdatedEntity(EntityManager em) {
        EmployeeProject employeeProject = new EmployeeProject().dateFrom(UPDATED_DATE_FROM).dateTo(UPDATED_DATE_TO);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeProject.setEmployee(employee);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        employeeProject.setProject(project);
        return employeeProject;
    }

    @BeforeEach
    public void initTest() {
        employeeProject = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeProject != null) {
            employeeProjectRepository.delete(insertedEmployeeProject);
            insertedEmployeeProject = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeProject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);
        var returnedEmployeeProjectDTO = om.readValue(
            restEmployeeProjectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeProjectDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeProjectDTO.class
        );

        // Validate the EmployeeProject in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmployeeProject = employeeProjectMapper.toEntity(returnedEmployeeProjectDTO);
        assertEmployeeProjectUpdatableFieldsEquals(returnedEmployeeProject, getPersistedEmployeeProject(returnedEmployeeProject));

        insertedEmployeeProject = returnedEmployeeProject;
    }

    @Test
    @Transactional
    void createEmployeeProjectWithExistingId() throws Exception {
        // Create the EmployeeProject with an existing ID
        employeeProject.setId(1L);
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeProject.setDateFrom(null);

        // Create the EmployeeProject, which fails.
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        restEmployeeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateToIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeProject.setDateTo(null);

        // Create the EmployeeProject, which fails.
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        restEmployeeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeProjects() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        // Get all the employeeProjectList
        restEmployeeProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())));
    }

    @Test
    @Transactional
    void getEmployeeProject() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        // Get the employeeProject
        restEmployeeProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeProject.getId().intValue()))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeProject() throws Exception {
        // Get the employeeProject
        restEmployeeProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeProject() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeProject
        EmployeeProject updatedEmployeeProject = employeeProjectRepository.findById(employeeProject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeProject are not directly saved in db
        em.detach(updatedEmployeeProject);
        updatedEmployeeProject.dateFrom(UPDATED_DATE_FROM).dateTo(UPDATED_DATE_TO);
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(updatedEmployeeProject);

        restEmployeeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeProjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeProjectDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeProjectToMatchAllProperties(updatedEmployeeProject);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeProjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeProjectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeProjectWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeProject using partial update
        EmployeeProject partialUpdatedEmployeeProject = new EmployeeProject();
        partialUpdatedEmployeeProject.setId(employeeProject.getId());

        partialUpdatedEmployeeProject.dateFrom(UPDATED_DATE_FROM);

        restEmployeeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeProject))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeProjectUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeProject, employeeProject),
            getPersistedEmployeeProject(employeeProject)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeProjectWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeProject using partial update
        EmployeeProject partialUpdatedEmployeeProject = new EmployeeProject();
        partialUpdatedEmployeeProject.setId(employeeProject.getId());

        partialUpdatedEmployeeProject.dateFrom(UPDATED_DATE_FROM).dateTo(UPDATED_DATE_TO);

        restEmployeeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeProject))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeProjectUpdatableFieldsEquals(
            partialUpdatedEmployeeProject,
            getPersistedEmployeeProject(partialUpdatedEmployeeProject)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeProjectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeProject.setId(longCount.incrementAndGet());

        // Create the EmployeeProject
        EmployeeProjectDTO employeeProjectDTO = employeeProjectMapper.toDto(employeeProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeProjectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeProject() throws Exception {
        // Initialize the database
        insertedEmployeeProject = employeeProjectRepository.saveAndFlush(employeeProject);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeProject
        restEmployeeProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeProject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeProjectRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EmployeeProject getPersistedEmployeeProject(EmployeeProject employeeProject) {
        return employeeProjectRepository.findById(employeeProject.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeProjectToMatchAllProperties(EmployeeProject expectedEmployeeProject) {
        assertEmployeeProjectAllPropertiesEquals(expectedEmployeeProject, getPersistedEmployeeProject(expectedEmployeeProject));
    }

    protected void assertPersistedEmployeeProjectToMatchUpdatableProperties(EmployeeProject expectedEmployeeProject) {
        assertEmployeeProjectAllUpdatablePropertiesEquals(expectedEmployeeProject, getPersistedEmployeeProject(expectedEmployeeProject));
    }
}
