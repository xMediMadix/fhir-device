package com.example.fhir_device;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Device implements Serializable {
    private Identifier[] identifier; // Instance identifier
    private String definition;  // The reference to the definition for the device
    private UdiCarrier udiCarrier; // Unique Device Identifier (UDI) Barcode string
    private String status; // possible values: active | inactive | entered-in-error | unknown
    private String[] statusReason; //possible values: online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
    private String distinctIdentifier; // The distinct identification string
    private String manufacturer; // Name of device manufacturer
    private Date manufacturerDate; // Date when the device was made
    private Date expirationDate; // Date and time of expiry of this device (if applicable)
    private String lotNumber; // Lot number of manufacture
    private String serialNumber; // Serial number assigned by the manufacturer
    private String deviceName; // The name of the device as given by the manufacturer
    private String modelNumber; // The model number for the device
    private String partNumber; // The part number of the device
    private CodeableConcept type; // The kind or type of device
    private Specialization specialization; // The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used for the communication
    private String version; // The actual design of the device or software version running on the device
    private String property; // The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
    private String patient; // Patient to whom Device is affixed
    private String owner; // Organization responsible for device
    private String contact; // Details for human/organization for support
    private String location; // Where the device is found
    private String url; // Network address to contact device
    private String note; // Device notes and comments
    private CodeableConcept safety; // Safety Characteristics of Device
    private String parent; // The parent device

    /**
     * A FHIR-Device szabvány alapján kardinalitás tekintetében nem minden mező kötelező.
     * Ez a konstruktor csak néhány általam fontosnak vélt adatot vár, amelyek alapján inicializálja őket.
     * Ezen paraméterek mindegyike az AddDeviceActivity-n található input mezőkről érkeznek.
     * @param status
     * @param manufacturer
     * @param manufacturerDate
     * @param serialNumber
     * @param deviceName
     * @param type
     */
    Device(String status, String manufacturer, Date manufacturerDate, String serialNumber, String deviceName, String type) {
        this.status = status;
        this.manufacturer = manufacturer;
        this.manufacturerDate = manufacturerDate;
        this.serialNumber = serialNumber;
        this.deviceName = deviceName;
        this.type = new CodeableConcept(type);
    }

    @Override
    public String toString() {
        return "Device{" +
                "status='" + status + '\'' +
                "name='" + deviceName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", manufacturerDate=" + manufacturerDate +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Date getManufacturerDate() {
        return manufacturerDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public CodeableConcept getType() {
        return type;
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

class CodeableConcept implements Serializable {
    private Coding[] coding;
    private String text;

    CodeableConcept() {
    }

    CodeableConcept(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

class Coding implements Serializable {
    private String system;
    private String version;
    private String code;
    private String display;
    private boolean userSelected;
}

class Period implements Serializable {
    private LocalDate start;
    private LocalDate end;
}

class UdiCarrier implements Serializable {
    private String deviceIdentifier; // Mandatory fixed portion of UDI
    private String issuer; // UDI Issuing Organization
    private String jurisdiction; // Regional UDI authority
    private String carrierAIDC; // UDI Machine Readable Barcode String
    private String carrierHRF; // UDI Human Readable Barcode String
    private String entryType; // possible values: barcode | rfid | manual +
}

class Specialization implements Serializable {
    private CodeableConcept systemType; // The standard that is used to operate and communicate
    private String version; // The version of the standard that is used to operate and communicate
}
