package com.example.interfazandroid.modelApi;

public class ProyectRegister {
    private String titulo;
    private String fecha;
    private String estado;
    private String descripcion;
    private String usuarioId;

    public ProyectRegister(String titulo, String fecha, String estado, String descripcion, String usuarioId) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.estado = estado;
        this.descripcion = descripcion;
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
