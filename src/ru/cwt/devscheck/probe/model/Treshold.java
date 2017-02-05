package ru.cwt.devscheck.probe.model;

/**
 * @author e.chertikhin
 * @date 02/02/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class Treshold {

    String name;

    Double warning;
    Double alert;

    public Treshold() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWarning() {
        return warning;
    }

    public void setWarning(Double warning) {
        this.warning = warning;
    }

    public Double getAlert() {
        return alert;
    }

    public void setAlert(Double alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\"=\"").append(name).append("\"");
        sb.append(", \"warning\"=\"").append(warning).append("\"");
        sb.append(", \"alert\"=\"").append(alert).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
