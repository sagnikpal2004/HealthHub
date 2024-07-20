package nic.HealthHub.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import nic.HealthHub.repository.DeviceRepository;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Device;


@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired private DeviceRepository deviceRepo;

    @PostMapping
    public Device createDevice(@RequestBody Device device) {
        return deviceRepo.save(device);
    }

    @PostMapping("/{id}/link")
    public Device createLink(@PathVariable IdType id, @RequestParam IdType patient_id) {
        Device device = deviceRepo.findById(id).get();
        device.setPatient(new Reference("Patient/" + patient_id));
        return deviceRepo.save(device);
    }

    @GetMapping("/{id}")
    public Device getDevice(@PathVariable IdType id) {
        return deviceRepo.findById(id).get();
    }

    // @PutMapping("/{id}")
    // public Device updateDevice(@PathVariable IdType id, @RequestBody Device device) {
    //     deviceRepo.findById(id).update(device);
    // }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable IdType id) {
        deviceRepo.deleteById(id);
    }

    @DeleteMapping("/{id}/unlink")
    public void deleteLink(@PathVariable IdType id) {
        Device device = deviceRepo.findById(id).get();
        device.setPatient(null);
        deviceRepo.save(device);
    }
}