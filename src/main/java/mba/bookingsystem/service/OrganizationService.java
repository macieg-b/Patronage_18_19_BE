package mba.bookingsystem.service;

import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.util.RepositoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization getOne(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        return organizationRepository.findByUuid(uuid);
    }

    public Organization create(Organization organization) {
        throwIfNameExists(organization.getName(), organization);
        organization = organizationRepository.save(organization);
        return organization;
    }


    public Organization update(Organization organization, UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        throwIfNameExists(organization.getName(), organization);
        Organization dbOrganization = organizationRepository.findByUuid(uuid);
        dbOrganization.setName(organization.getName());
        organizationRepository.save(dbOrganization);
        return dbOrganization;
    }


    public void delete(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        Organization organization = organizationRepository.findByUuid(uuid);
        organizationRepository.delete(organization);
    }

    private void throwIfNameExists(String name, Organization organization) {
        Organization dbOrganization = organizationRepository.findByName(name);
        if (dbOrganization == null) {
            return;
        }
        if (organization != null) {
            if (organization.getName().equals(dbOrganization.getName()) && !organization.getUuid().equals(dbOrganization.getUuid())) {
                throw new AlreadyExistsException(name);
            }
        }
    }
}
