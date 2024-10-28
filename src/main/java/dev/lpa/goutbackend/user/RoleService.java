package dev.lpa.goutbackend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }  

    public Iterable<Role> getAllRoles() {
        var roles = roleRepository.findAll();
        logger.info("Get all roles: {}", roles);
        return roles;
    }
}
