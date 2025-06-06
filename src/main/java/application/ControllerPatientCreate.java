package application;



import application.model.PatientRepository;
import application.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import application.model.*;
import view.*;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatientCreate {

	@Autowired
	DoctorRepository doctorRepository;
	@Autowired
	PatientRepository patientRepository;

	@Autowired
	SequenceService sequence;


	/*
	 * Request blank patient registration form.
	 */
	@GetMapping("/patient/new")
	public String getNewPatientForm(Model model) {
		// return blank form for new patient registration
		model.addAttribute("patient", new PatientView());
		return "patient_register";
	}

	/*
	 * Process data from the patient_register form
	 */
	@PostMapping("/patient/new")
	public String createPatient(PatientView p, Model model) {

		Doctor d = doctorRepository.findByLastName(p.getPrimaryName());

		if(d == null){
			model.addAttribute("message", "Error. Primary doctor with last name '" + p.getPrimaryName() + "' does not exist.");
			model.addAttribute("patient", p);
			return "patient_edit";
		}

		int id = sequence.getNextSequence("PATIENT_SEQUENCE");
		Patient patientM = new Patient();
		patientM.setId(id);
		patientM.setLastName(p.getLastName());
		patientM.setFirstName(p.getFirstName());
		patientM.setBirthdate(p.getBirthdate());
		patientM.setSsn(p.getSsn());
		patientM.setStreet(p.getStreet());
		patientM.setCity(p.getCity());
		patientM.setState(p.getState());
		patientM.setZipcode(p.getZipcode());
		patientM.setPrimaryName(p.getPrimaryName());
		patientRepository.insert(patientM);

		model.addAttribute("message", "Registration successful.");
		p.setId(id);
		model.addAttribute("patient", p);
		return "patient_show";

	}

	/*
	 * Request blank form to search for patient by id and name
	 */
	@GetMapping("/patient/edit")
	public String getSearchForm(Model model) {
		model.addAttribute("patient", new PatientView());
		return "patient_get";
	}

	/*
	 * Perform search for patient by patient id and name.
	 */
	@PostMapping("/patient/show")
	public String showPatient(PatientView p, Model model) {

		Patient patient = patientRepository.findByIdAndLastName(p.getId(), p.getLastName());
		if (patient != null) {
			PatientView pv = new PatientView();
			pv.setId(patient.getId());
			pv.setFirstName(patient.getFirstName());
			pv.setLastName(patient.getLastName());
			pv.setBirthdate(patient.getBirthdate());
			pv.setStreet(patient.getStreet());
			pv.setCity(patient.getCity());
			pv.setState(patient.getState());
			pv.setZipcode(patient.getZipcode());
			pv.setPrimaryName(patient.getPrimaryName());
			model.addAttribute("patient", pv);
			return "patient_show";
		} else {
			model.addAttribute("message", "Patient not found.");
			model.addAttribute("patient", p);
			return "patient_get";
		}
	}
}
