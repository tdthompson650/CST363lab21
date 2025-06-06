package application;

import application.model.Doctor;
import application.model.Patient;
import application.model.Pharmacy;
import application.model.Prescription;
import application.model.DoctorRepository;
import application.model.PatientRepository;
import application.model.PharmacyRepository;
import application.model.PrescriptionRepository;
import application.model.Prescription.FillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import view.PrescriptionView;

import java.time.LocalDateTime;

@Controller
public class ControllerPrescriptionFill {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;  // Direct query to MongoDB

    /*
     * Patient requests form to fill prescription.
     */
    @GetMapping("/prescription/fill")
    public String getFillForm(Model model) {
        model.addAttribute("prescription", new PrescriptionView());
        return "prescription_fill";
    }

    /*
     * Process data from prescription_fill form
     */
    @PostMapping("/prescription/fill")
    public String processFillForm(@ModelAttribute("prescription") PrescriptionView p, Model model) {
        System.out.println("processFillForm: " + p);

        // Step 1: Validate the pharmacy by name and address
        Pharmacy pharmacy = pharmacyRepository.findByNameAndAddress(p.getPharmacyName(), p.getPharmacyAddress());
        if (pharmacy == null) {
            model.addAttribute("message", "Error: Invalid pharmacy details.");
            return "prescription_fill";
        }

        // Step 2: Validate the patient by last name only
        Patient patient = patientRepository.findByLastName(p.getPatientLastName());
        if (patient == null) {
            model.addAttribute("message", "Error: Patient with last name '" + p.getPatientLastName() + "' not found.");
            return "prescription_fill";
        }

        // After finding the patient, set the patient ID in the view (useful later)
        p.setPatientId(patient.getId());

        // Step 3: Validate the prescription by Rx ID and Patient Last Name using MongoTemplate
        Query query = new Query();
        query.addCriteria(Criteria.where("rxid").is(p.getRxid()).and("patientId").is(patient.getId()));
        Prescription prescription = mongoTemplate.findOne(query, Prescription.class);

        if (prescription == null) {
            model.addAttribute("message", "Error: Prescription not found for the specified patient.");
            return "prescription_fill";
        }

        // Step 4: Check if there are remaining refills
        if (prescription.getRefills() <= 0) {
            model.addAttribute("message", "Error: No refills remaining for this prescription.");
            return "prescription_fill";
        }

        // Step 5: Calculate the cost dynamically without using Optional
        double cost = -1;
        for (Pharmacy.DrugCost drugCost : pharmacy.getDrugCosts()) {
            if (drugCost.getDrugName().equalsIgnoreCase(prescription.getDrugName())) {
                cost = drugCost.getCost() * prescription.getQuantity();
                break;
            }
        }

        // If the drug was not found in the pharmacy's list
        if (cost == -1) {
            model.addAttribute("message", "Error: Drug not found in the selected pharmacy.");
            return "prescription_fill";
        }

        // Step 6: Update the prescription - create a fill request
        FillRequest fillRequest = new FillRequest();
        fillRequest.setPharmacyID(pharmacy.getId());
        fillRequest.setDateFilled(LocalDateTime.now().toString());
        fillRequest.setCost(String.valueOf(cost));

        // Add the fill request to the prescription
        prescription.getFills().add(fillRequest);

        // Decrease refills by 1
        prescription.setRefills(prescription.getRefills() - 1);

        // Step 7: Save the updated prescription
        prescriptionRepository.save(prescription);

        // Step 8: Fetch and Populate additional fields in PrescriptionView

        // Fetch Doctor information
        Doctor doctor = doctorRepository.findById(prescription.getDoctorId());
        if (doctor != null) {
            p.setDoctorId(doctor.getId());
            p.setDoctorFirstName(doctor.getFirstName());
            p.setDoctorLastName(doctor.getLastName());
        }

        // Set the Drug Name, Quantity, and Refills from the prescription
        p.setDrugName(prescription.getDrugName());
        p.setQuantity(prescription.getQuantity());
        p.setRefills(prescription.getRefills());

        // Set the Pharmacy Phone from the pharmacy
        p.setPharmacyPhone(pharmacy.getPhone());

        // Set the other fields
        p.setPatientFirstName(patient.getFirstName());
        p.setPharmacyID(pharmacy.getId());
        p.setRefillsRemaining(prescription.getRefills());
        p.setDateFilled(fillRequest.getDateFilled());
        p.setCost(fillRequest.getCost());

        // Step 9: Success message and show updated prescription
        model.addAttribute("message", "Prescription filled successfully.");
        model.addAttribute("prescription", p);  // Pass the updated PrescriptionView

        return "prescription_show";
    }}