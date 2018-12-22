package mba.bookingsystem.controller;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.service.OrganizationService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static mba.bookingsystem.util.ModelMapper.modelToString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
public class OrganizationControllerTest {
    private static final int ORGANIZATION_LIST_SIZE = 10;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Expected Name";

    @Mock
    private OrganizationService organizationService;
    private MockMvc mockMvc;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        final OrganizationController organizationController = new OrganizationController(organizationService);
        mockMvc = MockMvcBuilders.standaloneSetup(organizationController).build();
    }

    @DataProvider
    public static Object[] getCorrectOrganization() {
        return new Organization[]{new Organization(EXPECTED_UUID, EXPECTED_NAME)};
    }

    @DataProvider
    public static Object[] getTooLongNameOrganization() {
        return new Organization[]{new Organization(EXPECTED_UUID, "Very long name up to 20 letters")};
    }


    @Test
    public void getAllOrganizationSuccess() throws Exception {
        final List<Organization> organizationList = getOrganizationList(ORGANIZATION_LIST_SIZE);
        when(organizationService.getAll()).thenReturn(organizationList);

        mockMvc.perform(get(RestUrl.ORGANIZATION_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(ORGANIZATION_LIST_SIZE)));
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void getOrganizationByIdSuccess(final Organization organization) throws Exception {
        when(organizationService.getOne(EXPECTED_UUID)).thenReturn(organization);
        mockMvc.perform(get(String.format("%s/%s", RestUrl.ORGANIZATION_URL, EXPECTED_UUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.uuid", Matchers.is(EXPECTED_UUID.toString())))
                .andExpect(jsonPath("$.name", Matchers.is(EXPECTED_NAME)));
    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void createOrganizationSuccess(final Organization organization) throws Exception {
        final String organizationJson = modelToString(organization);

        when(organizationService.create(any(Organization.class))).thenReturn(organization);
        mockMvc.perform(post(RestUrl.ORGANIZATION_URL)
                .contentType(CONTENT_TYPE)
                .content(organizationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(EXPECTED_NAME)));
    }

    @Test
    @UseDataProvider("getTooLongNameOrganization")
    public void createOrganizationBadRequest(final Organization organization) throws Exception {
        final String organizationJson = modelToString(organization);

        when(organizationService.create(any(Organization.class))).thenReturn(organization);
        mockMvc.perform(post(RestUrl.ORGANIZATION_URL)
                .contentType(CONTENT_TYPE)
                .content(organizationJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOrganizationById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(organizationService).delete(id);
        mockMvc.perform(delete(String.format("%s/%s", RestUrl.ORGANIZATION_URL, id)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @UseDataProvider("getCorrectOrganization")
    public void updateOrganization(final Organization organization) throws Exception {
        final String organizationJson = modelToString(organization);

        when(organizationService.update(any(Organization.class), eq(EXPECTED_UUID))).thenReturn(organization);
        mockMvc.perform(put(String.format("%s/%s", RestUrl.ORGANIZATION_URL, EXPECTED_UUID))
                .contentType(CONTENT_TYPE)
                .content(organizationJson))
                .andExpect(status().isOk());
    }

    private List<Organization> getOrganizationList(int count) {
        List<Organization> organizationList = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(
                        i -> organizationList.add(new Organization(UUID.randomUUID(), String.format("Organization_%s", i)))
                );
        return organizationList;
    }

}