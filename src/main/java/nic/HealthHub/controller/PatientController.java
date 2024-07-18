package nic.HealthHub.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nic.HealthHub.repository.PatientRepository;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;


@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepo;
    
    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientRepo.save(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable IdType id) {
        return patientRepo.findById(id).get();
    }

    @PutMapping
    public Patient updatePatient(@RequestBody Patient patient) {
        return patientRepo.save(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable IdType id) {
        Patient patient = patientRepo.findById(id).get();
        patientRepo.delete(patient);
    }
}
