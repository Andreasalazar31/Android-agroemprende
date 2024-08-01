package com.example.interfazandroid.modelApi;

import java.util.List;

public class Proyecto {
    private String _id;
    private String titulo;
    private String fecha;
    private String estado;
    private String descripcion;
    private String usuarioId;
    private List<String> secciones;
    private List<String> revisiones;
    private int __v;

    // Getters and setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public List<String> getSecciones() { return secciones; }
    public void setSecciones(List<String> secciones) { this.secciones = secciones; }
    public List<String> getRevisiones() { return revisiones; }
    public void setRevisiones(List<String> revisiones) { this.revisiones = revisiones; }
    public int get__v() { return __v; }
    public void set__v(int __v) { this.__v = __v; }
}
