package tn.pi.cabinetmedicalproject.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
    User save(User user);

    User save(UserRegistrationDto registrationDto);
}
