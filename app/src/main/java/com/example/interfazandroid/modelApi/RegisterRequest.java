package com.example.interfazandroid.modelApi;

import java.util.Date;

public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String numIdentificacion;
    private String telefono;
    private String fechaNacimiento;
    private String caracterizacion;
    private String contrasena;
    private String role;

    public RegisterRequest(String nombre, String apellido, String email, String contrasena,String numIdentificacion, String telefono, String fechaNacimiento, String caracterizacion,  String role) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.numIdentificacion = numIdentificacion;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.caracterizacion = caracterizacion;
        this.role = role;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimieto) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCaracterizacion() {
        return caracterizacion;
    }

    public void setCaracterizacion(String caracterizacion) {
        this.caracterizacion = caracterizacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
