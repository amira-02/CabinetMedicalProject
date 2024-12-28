package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;

import java.util.List;

@Controller
@Slf4j
public class PatientController {

    private final PatientRepository patientRepository; //k t3ml final par obligation t3ml constructeur

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/index")
    public String index(Model model ,
                        @RequestParam(name = "page" , defaultValue = "0") int page ,
                        @RequestParam(name = "size" , defaultValue = "4")int size ,
                        @RequestParam(name = "keyword" , defaultValue = "")String keyword
    )
    {
//        List<Patient> patients = patientRepository.findAll();
//        Page<Patient> patients = patientRepository.findAll(PageRequest.of(page, size));
        Page<Patient> patients = patientRepository.findByNameContains(keyword ,PageRequest.of(page ,size));
        model.addAttribute("patients", patients.getContent());
        model.addAttribute("pages", new int[patients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword" ,keyword);
        return "patients"; //retourne le nom de la page html sans .html
    }

    @GetMapping("/delete")
    public String delete(Long id ,String keyword , int page ){
        patientRepository.deleteById(id);

        return "redirect:/index?page="+page+"&keyword="+keyword;
    }



    @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());

        return "formPatients";
    }


    @PostMapping("/savePatient")
    public String savePatient(Model model,@Valid Patient patient,BindingResult bindingResult ){
        if(bindingResult.hasErrors())
            return "formPatients";
        log.info("The patient sended is:"+ patient);
        patientRepository.save(patient);
        model.addAttribute("patient", new Patient());
        return "redirect:/index";
    }


    @GetMapping("/EditPatients")
    public String EditPatients(Model model,@RequestParam(name = "id") Long id ){
        Patient patient=patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "EditPatients";
    }

    @GetMapping("/ConsultationPatient")
    public String ConsultationPatient(Model model,@RequestParam(name = "id") Long id ) {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "ConsultationPatient";
    }}