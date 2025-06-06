package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import application.model.*;
import view.*;



/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatientUpdate {

	@Autowired
	PatientRepository patientRepository;
	@Autowired
	DoctorRepository doctorRepository;

	/*
	 *  Display patient profile for patient id.
	 */
	@GetMapping("/patient/edit/{id}")
	public String getUpdateForm(@PathVariable int id, Model model) {

		Patient p = patientRepository.findById(id);

		if (p != null) {
			PatientView pv = new PatientView();
			pv.setId(p.getId());
			pv.setFirstName(p.getFirstName());
			pv.setLastName(p.getLastName());
			pv.setBirthdate(p.getBirthdate());
			pv.setStreet(p.getStreet());
			pv.setCity(p.getCity());
			pv.setState(p.getState());
			pv.setZipcode(p.getZipcode());
			pv.setPrimaryName(p.getPrimaryName());

			model.addAttribute("patient", pv);
			return "patient_edit";
		} else {
			model.addAttribute("message", "Patient not found.");
			return "patient_edit";
		}
	}
	/*
	 * Process changes from patient_edit form
	 *  Primary doctor, street, city, state, zip can be changed
	 *  ssn, patient id, name, birthdate, ssn are read only in template.
	 */
	@PostMapping("/patient/edit")
	public String updatePatient(PatientView patient, Model model) {

		Patient p = patientRepository.findById(patient.getId());
		Doctor d = doctorRepository.findByLastName(patient.getPrimaryName());

		if(d == null){
			model.addAttribute("message", "Error. Primary doctor with last name '" + patient.getPrimaryName() + "' does not exist.");
			model.addAttribute("patient", patient);
			return "patient_edit";
		}

		p.setStreet(patient.getStreet());
		p.setCity(patient.getCity());
		p.setState(patient.getState());
		p.setZipcode(patient.getZipcode());
		p.setPrimaryName(patient.getPrimaryName());
		patientRepository.save(p);
		model.addAttribute("message", "Update successful");
		model.addAttribute("patient", p);
		return "patient_show";

	}

}
