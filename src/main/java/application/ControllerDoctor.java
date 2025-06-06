package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import application.model.*;
import application.service.*;
import view.*;

/*
 * Controller class for doctor registration and profile update.
 * This class is complete and provided as an example.
 */
@Controller
public class ControllerDoctor {
	@Autowired
	DoctorRepository doctorRepository;
	@Autowired
	SequenceService sequence;
	/*
	 * Request for new doctor registration form.
	 */
	@GetMapping("/doctor/register")
	public String getNewDoctorForm(Model model) {
		// return blank form for new patient registration
		model.addAttribute("doctor", new DoctorView());
		return "doctor_register";
	}

	/*
	 * Process doctor registration.
	 */
	@PostMapping("/doctor/register")
	public String createDoctor(DoctorView doctor, Model model) {
		// get the next unique id for doctor.
		int id = sequence.getNextSequence("DOCTOR_SEQUENCE");
		// create a model.doctor instance 
		// copy data from DoctorView to model
		Doctor doctorM = new Doctor();
		doctorM.setId(id);
		doctorM.setFirstName(doctor.getFirstName());
		doctorM.setLastName(doctor.getLastName());
		doctorM.setPracticeSinceYear(doctor.getPracticeSinceYear());
		doctorM.setSpecialty(doctor.getSpecialty());
		doctorM.setSsn(doctor.getSsn());
		
		doctor.setId(id);
		doctorRepository.insert(doctorM);
		// display message and patient information
		model.addAttribute("message", "Registration successful.");
		doctor.setId(id);
		model.addAttribute("doctor", doctor);
		return "doctor_show";

	}

	/*
	 * Request blank form for doctor search.
	 */
	@GetMapping("/doctor/get")
	public String getSearchForm(Model model) {
		// return bank form to enter doctor id and name
		model.addAttribute("doctor", new Doctor());
		return "doctor_get";
	}

	/*
	 * Search for doctor by id and name.
	 */
	@PostMapping("/doctor/get")
	public String showDoctor(DoctorView doctor, Model model) {
		// retrieve doctor using the id, last_name entered by user
		Doctor d = doctorRepository.findByIdAndLastName(doctor.getId(), doctor.getLastName());
		if (d != null) {
			// copy data from model to view 
			doctor.setFirstName(d.getFirstName());
			doctor.setPracticeSinceYear(d.getPracticeSinceYear());
			doctor.setSpecialty(d.getSpecialty());
			model.addAttribute("doctor", doctor);
			return "doctor_show";

		} else {
			model.addAttribute("message", "Doctor not found.");
			model.addAttribute("doctor", doctor);
			return "doctor_get";
		}
	}

	/*
	 * search for doctor by id.
	 */
	@GetMapping("/doctor/edit/{id}")
	public String getUpdateForm(@PathVariable int id, Model model) {

		// retrieve doctor by id 
		// copy data to doctor view for edit
		
		Doctor d = doctorRepository.findById(id);
		if (d != null) {
			DoctorView dv = new DoctorView();
			dv.setId(id);
			dv.setFirstName(d.getFirstName());
			dv.setLastName(d.getLastName());
			dv.setPracticeSinceYear(d.getPracticeSinceYear());
			dv.setSpecialty(d.getSpecialty());
			model.addAttribute("doctor", dv);
			return "doctor_edit";
		} else {
			model.addAttribute("message", "Doctor not found.");
			return "doctor_get";
		}
	}

	/*
	 * process profile update for doctor. 
	 * Only specialty or year of practice fields are allowed to be changed.
	 */
	@PostMapping("/doctor/edit")
	public String updateDoctor(DoctorView doctor, Model model) {
		
		Doctor d = doctorRepository.findById(doctor.getId());
		// only year and specialty can be updated
		d.setPracticeSinceYear(doctor.getPracticeSinceYear());
		d.setSpecialty(doctor.getSpecialty());
		doctorRepository.save(d);
		model.addAttribute("message", "Update successful");
		model.addAttribute("doctor", doctor);
		return "doctor_show";
		
	}
	
}
