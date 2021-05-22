package com.example.fhir_device;

import java.util.Date;

public class Device {
    private Identifier[] identifier; // Instance identifier
    private String definition;  // The reference to the definition for the device
    private UdiCarrier udiCarrier; // Unique Device Identifier (UDI) Barcode string
    private String status; // possible values: active | inactive | entered-in-error | unknown (TODO: ezt)
    private String[] statusReason; //possible values: online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
    private String distinctIdentifier; // The distinct identification string
    private String manufacturer; // Name of device manufacturer (TODO: ezt)
    private Date manufacturerDate; // Date when the device was made (TODO: ezt)
    private Date expirationDate; // Date and time of expiry of this device (if applicable)
    private String lotNumber; // Lot number of manufacture
    private String serialNumber; // Serial number assigned by the manufacturer (TODO: ezt)
    private String[] deviceName; // The name of the device as given by the manufacturer (TODO: ezt)
    private String modelNumber; // The model number for the device
    private String partNumber; // The part number of the device
    private CodeableConcept type; // The kind or type of device (TODO: esetleg ezt is, stringként megadva)
    private Specialization specialization; // The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used for the communication
    private String version; // The actual design of the device or software version running on the device (TODO: esetleg)
    private String property; // The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
    private String patient; // Patient to whom Device is affixed
    private String owner; // Organization responsible for device
    private String contact; // Details for human/organization for support
    private String location; // Where the device is found
    private String url; // Network address to contact device
    private String note; // Device notes and comments
    private CodeableConcept safety; // Safety Characteristics of Device
    private String parent; // The parent device

    Device(String status, String manufacturer, Date manufacturerDate, String serialNumber, String deviceName, String type) {
        this.status = status;
        this.manufacturer = manufacturer;
        this.manufacturerDate = manufacturerDate;
        this.serialNumber = serialNumber;
        this.deviceName = new String[]{deviceName};
        this.type = new CodeableConcept(type);
    }
}

class Identifier {
    private String use;
    private CodeableConcept type;
    private String system;
    private String value;
    private Period period;
    private String assigner;
}

class CodeableConcept {
    private Coding[] coding;
    private String text;

    CodeableConcept() {
    }

    CodeableConcept(String text) {
        this.text = text;
    }
}

class Coding {
    private String system;
    private String version;
    private String code;
    private String display;
    private boolean userSelected;
}

class Period {
    private Date start;
    private Date end;
}

class UdiCarrier {
    private String deviceIdentifier; // Mandatory fixed portion of UDI
    private String issuer; // UDI Issuing Organization
    private String jurisdiction; // Regional UDI authority
    private String carrierAIDC; // UDI Machine Readable Barcode String
    private String carrierHRF; // UDI Human Readable Barcode String
    private String entryType; // possible values: barcode | rfid | manual +
}

class Specialization {
    private CodeableConcept systemType; // The standard that is used to operate and communicate
    private String version; // The version of the standard that is used to operate and communicate
}
