package com.lifepill.inventoryservice.entity.enums;

/**
 * Measuring unit types for items.
 * Includes both singular and plural forms for database compatibility.
 */
public enum MeasuringUnitType {
    // Generic units
    UNIT,
    UNITS,
    PIECE,
    PIECES,
    
    // Container types
    BOX,
    BOXES,
    BOTTLE,
    BOTTLES,
    PACK,
    PACKS,
    STRIP,
    STRIPS,
    TUBE,
    TUBES,
    SACHET,
    SACHETS,
    VIAL,
    VIALS,
    AMPOULE,
    AMPOULES,
    
    // Medication forms
    TABLET,
    TABLETS,
    CAPSULE,
    CAPSULES,
    
    // Volume measurements
    ML,
    L,
    
    // Weight measurements
    MG,
    G,
    KG,
    
    // Other
    OTHER
}
