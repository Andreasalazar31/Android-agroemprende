package com.example.interfazandroid.modelApi;

public class UserDetails {

    private Sub sub;
    private String role;
    private long iat;
    private long exp;

    public static class Sub{
        private String _id;
        private String nombre;
        private String apellido;
        private String email;
        private String numIdentificacion;
        private String telefono;
        private String fechaNacimieto;
        private String caracterizacion;
        private String contrasena;
        private String role;
        private int __v;

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

        public String getFechaNacimieto() {
            return fechaNacimieto;
        }

        public void setFechaNacimieto(String fechaNacimieto) {
            this.fechaNacimieto = fechaNacimieto;
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

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }

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

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

}
