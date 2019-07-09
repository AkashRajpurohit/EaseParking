package com.bougainvillea.sanchit.easeparking.Beans;

import android.widget.EditText;
import android.widget.Spinner;

public class VehicleTypeLayout {
    Spinner vehicle_type;
    EditText state_edittxt;
    EditText number_plate_edittxt;

    public VehicleTypeLayout(Spinner vehicle_type, EditText state_edittxt, EditText number_plate_edittxt) {
        this.vehicle_type = vehicle_type;
        this.state_edittxt = state_edittxt;
        this.number_plate_edittxt = number_plate_edittxt;
    }

    public Spinner getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(Spinner vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public EditText getState_edittxt() {
        return state_edittxt;
    }

    public void setState_edittxt(EditText state_edittxt) {
        this.state_edittxt = state_edittxt;
    }

    public EditText getNumber_plate_edittxt() {
        return number_plate_edittxt;
    }

    public void setNumber_plate_edittxt(EditText number_plate_edittxt) {
        this.number_plate_edittxt = number_plate_edittxt;
    }
}
