# HealthHub

HealthHub is a Spring Boot application designed to manage health data, focusing on patient information and heath metrics. It provides a MQTT Listener to receive health data from IoT devices, processes them, including making interpretations and stores them in MongoDB for future retrieval. It also provides a RESTFul API for managing patient and device documents, as well as linking them for seamless health documentation.

<!-- ## API Calls
 -->

## Layout
### Services
- `MQTTListenerService`: Listens for observations from health IoT devices over MQTT and saves them to MongoDB
- `HL7Service`: Formats data in HL7 FHIR format and adds metadata
- `InterpretationService`: Interprets data to resolve into findings using industry standards

### Controllers
- `PatientController`: CRUD Operations for creating Patient documents using HL7 FHIR
- `DeviceController`: CRUD Operations for Device documents and linking devices to patients

### Repositories
- `PatientRepository`: Stores Patient information in HL7 FHIR
- `DeviceRepository`: Stores device and linking information in HL7 FHIR
- `ObservationRepository`: Stores Observations and interpretations from devices linked to patients in HL7 FHIR

### Project Flow
Patient and device documents as well as links are created from the REST Endpoints.

IoT measurement devices send formatted JSON Strings to HealthHub. `MQTTListenerService` receives them and gets the associated patient. Each observable is sent to `HL7Service` to add metadata and interpretations from `InterpretationService`. Finally these observations are saved to `MongoDB/observation`.

## Services
### MQTTListenerService
Listens for incoming readings from health measurement devices over MQTT on topic `observations`

JSON format for receiving messages:<br>
```
{
    "device_id": "device_id",
    "observables": {
        "SCTID1": reading1,
        "SCTID2": reading2,
        ...
    }
}
```

#### MQTTConfig
`MQTT_BROKER`: IP Address of MQTT Broker<br>
`DEVICE_ID = "HealthHub-0"`: Device_id of HealthHub instance<br>
`TOPIC = "observations`: The MQTT Topic where observations are read from

### InterpretationService
Reads interpretation parameters from `.\resources\InterpretationRanges.json` and decides on interpretations to add.