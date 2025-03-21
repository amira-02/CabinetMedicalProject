package tn.pi.cabinetmedicalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.service.PatientService;
import tn.pi.cabinetmedicalproject.service.UserService;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);

        return "redirect:/registration?success";
    }
}
