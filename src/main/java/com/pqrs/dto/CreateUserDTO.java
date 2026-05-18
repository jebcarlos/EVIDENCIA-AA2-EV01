package com.pqrs.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserDTO {
    
    @JsonProperty("tpd")
    private int tpd;
    
    @JsonProperty("identificacion")
    private String identificacion;
    
    @JsonProperty("dv")
    private Integer dv;
    
    @JsonProperty("primerApellido")
    private String primerApellido;
    
    @JsonProperty("segundoApellido")
    private String segundoApellido;
    
    @JsonProperty("primerNombre")
    private String primerNombre;
    
    @JsonProperty("segundoNombre")
    private String segundoNombre;
    
    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;
    
    @JsonProperty("sexo")
    private String sexo;
    
    @JsonProperty("tipoSangre")
    private Integer tipoSangre;
    
    // Constructores
    public CreateUserDTO() {}
    
    public CreateUserDTO(int tpd, String identificacion, Integer dv, String primerApellido,
                        String segundoApellido, String primerNombre, String segundoNombre,
                        LocalDate fechaNacimiento, String sexo, Integer tipoSangre) {
        this.tpd = tpd;
        this.identificacion = identificacion;
        this.dv = dv;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.tipoSangre = tipoSangre;
    }
    
    // Getters y Setters
    public int getTpd() { return tpd; }
    public void setTpd(int tpd) { this.tpd = tpd; }
    
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    
    public Integer getDv() { return dv; }
    public void setDv(Integer dv) { this.dv = dv; }
    
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    
    public Integer getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(Integer tipoSangre) { this.tipoSangre = tipoSangre; }
}
