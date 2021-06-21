package com.tit.oxigenapp;

import java.util.Date;

public class historico {
    private String hipoxia;
    private int porcentaje;
    private Date Fecha;
    private String idUsuario;

    public historico(){

    }
    public historico(String hipoxia, int porcentaje, Date fecha, String idUsuario){
        this.hipoxia= hipoxia;
        this.porcentaje=porcentaje;
        this.Fecha=fecha;
        this.idUsuario=idUsuario;
    }

    public String getHipoxia() {
        return hipoxia;
    }

    public void setHipoxia(String hipoxia) {
        this.hipoxia = hipoxia;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
