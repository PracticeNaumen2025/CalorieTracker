package ru.naumen.calorietracker.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.repository.RoleRepository;
import ru.naumen.calorietracker.service.RoleService;
import ru.naumen.calorietracker.model.Role;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefaultUserRole() {
        return roleRepository.findByRoleName("USER");
    }
}
