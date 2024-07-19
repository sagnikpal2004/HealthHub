package nic.HealthHub.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.IdType;


@Repository
public interface ObservationRepository extends MongoRepository<Observation, IdType> { }
