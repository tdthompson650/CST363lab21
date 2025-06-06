package application;

import application.model.Doctor;
import application.model.Patient;
import application.model.Drug;
import application.model.Prescription;
import application.model.DoctorRepository;
import application.model.PatientRepository;
import application.model.DrugRepository;
import application.model.PrescriptionRepository;
import application.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import view.PrescriptionView;

import java.time.LocalDateTime;

@Controller
public class ControllerPrescriptionCreate {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private SequenceService sequenceService;

    /*
     * Display a blank form for new prescription creation.
     */
    @GetMapping("/prescription/new")
    public String getPrescriptionForm(Model model) {
        model.addAttribute("prescription", new PrescriptionView());
        return "prescription_create";
    }

    /*
     * Process the form submission for creating a new prescription.
     */
    @PostMapping("/prescription")
    public String createPrescription(@ModelAttribute("prescription") PrescriptionView p, Model model) {
        System.out.println("createPrescription: " + p);

        // Step 1: Validate the doctor by doctor ID, first name, and last name
        Doctor doctor = doctorRepository.findByIdAndFirstNameAndLastName(p.getDoctorId(), p.getDoctorFirstName(), p.getDoctorLastName());
        if (doctor == null) {
            model.addAttribute("message", "Error: Doctor with ID " + p.getDoctorId() +
                    " and name '" + p.getDoctorFirstName() + " " + p.getDoctorLastName() + "' not found.");
            return "prescription_create";
        }

        // Step 2: Validate the patient by patient ID, first name, and last name
        Patient patient = patientRepository.findByIdAndFirstNameAndLastName(p.getPatientId(), p.getPatientFirstName(), p.getPatientLastName());
        if (patient == null) {
            model.addAttribute("message", "Error: Patient with ID " + p.getPatientId() +
                    " and name '" + p.getPatientFirstName() + " " + p.getPatientLastName() + "' not found.");
            return "prescription_create";
        }

        // Step 3: Validate the drug by name
        Drug drug = drugRepository.findByName(p.getDrugName());
        if (drug == null) {
            model.addAttribute("message", "Error: Drug '" + p.getDrugName() + "' not found.");
            return "prescription_create";
        }

        // Step 4: Generate the next sequence value for the prescription ID
        int prescriptionId = sequenceService.getNextSequence("PRESCRIPTION_SEQUENCE");

        // Step 5: Insert prescription into the prescription repository
        Prescription newPrescription = new Prescription();
        newPrescription.setRxid(prescriptionId);  // Use the generated sequence ID
        newPrescription.setQuantity(p.getQuantity());
        newPrescription.setRefills(p.getRefills());
        newPrescription.setDrugName(drug.getName());  // Store the drug name only
        newPrescription.setDoctorId(doctor.getId());  // Use doctor ID
        newPrescription.setPatientId(patient.getId());  // Use patient ID
        newPrescription.setDateCreated(LocalDateTime.now().toString());  // Use current date and time
        prescriptionRepository.insert(newPrescription);

        // Step 6: Update the PrescriptionView with the generated rxid
        p.setRxid(prescriptionId);  // Set the generated prescription ID in the view

        // Step 7: Success
        model.addAttribute("message", "Prescription created successfully.");
        model.addAttribute("prescription", p);  // Pass the updated PrescriptionView (with rxid)

        return "prescription_show";
    }
}