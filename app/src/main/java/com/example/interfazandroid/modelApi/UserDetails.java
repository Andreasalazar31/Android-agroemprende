package com.example.interfazandroid.modelApi;

import java.util.List;

public class UserDetails {
    private Sub sub;
    private String role;
    private long iat;

    // Getters y setters
    public Sub getSub() {
        return sub;
    }

    public void setSub(Sub sub) {
        this.sub = sub;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public static class Sub {
        private String _id;
        private String nombre;
        private String apellido;
        private String email;
        private String numIdentificacion;
        private String telefono;
        private String fechaNacimiento;
        private String caracterizacion;
        private String contrasena;
        private String role;
        private List<Proyecto> proyectos;
        private int __v;

        // Getters y setters
        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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

        public void setFechaNacimiento(String fechaNacimiento) {
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

        public List<Proyecto> getProyectos() {
            return proyectos;
        }

        public void setProyectos(List<Proyecto> proyectos) {
            this.proyectos = proyectos;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }
}