package mba.bookingsystem.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.exception.NotFoundException;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.repository.OrganizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class OrganizationServiceTest {

    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Expected Name";

    @Mock
    private OrganizationRepository organizationRepository;
    private OrganizationService organizationService;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        organizationService = new OrganizationService(organizationRepository);
    }

    @DataProvider
    public static Object[] getCorrectOrganization() {
        return new Organization[]{new Organization(EXPECTED_UUID, EXPECTED_NAME)};
    }


    @Test
    public void getAllSuccess() {
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());
        var organizationList = organizationService.getAll();
        assertTrue(organizationList.isEmpty());
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void getOneSuccess(final Organization organization) {
        final var organizationUuid = organization.getUuid();
        when(organizationRepository.existsById(organizationUuid)).thenReturn(true);
        when(organizationRepository.findByUuid(organizationUuid)).thenReturn(organization);

        var dbOrganization = organizationService.getOne(organizationUuid);
        assertEquals(EXPECTED_NAME, dbOrganization.getName());
        assertEquals(EXPECTED_UUID, dbOrganization.getUuid());
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void createSuccess(final Organization organization) {
        when(organizationRepository.save(organization)).thenReturn(organization);

        var dbOrganization = organizationService.create(organization);
        assertEquals(EXPECTED_NAME, dbOrganization.getName());
    }

    @Test(expected = AlreadyExistsException.class)
    @UseDataProvider("getCorrectOrganization")
    public void createThrowAlreadyExists(final Organization organization) {
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationRepository.findByName(any(String.class))).thenReturn(Organization.builder()
                .name(EXPECTED_NAME)
                .uuid(UUID.randomUUID())
                .build());
        organizationService.create(organization);
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void updateSuccess(final Organization organization) {
        final var organizationUuid = organization.getUuid();
        when(organizationRepository.existsById(organizationUuid)).thenReturn(true);
        when(organizationRepository.findByUuid(organizationUuid)).thenReturn(organization);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization dbOrganization = organizationService.update(organization, organizationUuid);
        assertEquals(EXPECTED_NAME, dbOrganization.getName());
        assertEquals(EXPECTED_UUID, dbOrganization.getUuid());
    }

    @Test(expected = AlreadyExistsException.class)
    @UseDataProvider("getCorrectOrganization")
    public void updateThrowAlreadyExists(final Organization organization) {
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationRepository.existsById(any(UUID.class))).thenReturn(true);
        when(organizationRepository.existsByName(any(String.class))).thenReturn(true);
        when(organizationRepository.findByUuid(any(UUID.class))).thenReturn(organization);
        when(organizationRepository.findByName(any(String.class))).thenReturn(Organization.builder()
                .name(EXPECTED_NAME)
                .uuid(UUID.randomUUID())
                .build());
        organizationService.update(organization, EXPECTED_UUID);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectOrganization")
    public void updateThrowNotFound(final Organization organization) {
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationRepository.existsById(any(UUID.class))).thenReturn(false);
        when(organizationRepository.findByUuid(any(UUID.class))).thenReturn(organization);
        organizationService.update(organization, EXPECTED_UUID);
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void deleteSuccess(final Organization organization) {
        final var organizationUuid = organization.getUuid();
        when(organizationRepository.existsById(organizationUuid)).thenReturn(true);
        organizationService.delete(organizationUuid);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectOrganization")
    public void deleteThrowNotFoundException(final Organization organization) {
        final var organizationUuid = organization.getUuid();
        when(organizationRepository.existsById(organizationUuid)).thenReturn(false);
        organizationService.delete(organizationUuid);
    }
}