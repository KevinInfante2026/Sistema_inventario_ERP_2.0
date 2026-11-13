package model;

public class Cliente {
    private String idNit;
    private String nombreCompleto;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String correo;
    private double saldo;

    public Cliente() {}

    public Cliente(String idNit, String nombreCompleto, String direccion,
                   String ciudad, String telefono, String correo, double saldo) {
        this.idNit = idNit;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.correo = correo;
        this.saldo = saldo;
    }

    public String getIdNit() { return idNit; }
    public void setIdNit(String idNit) { this.idNit = idNit; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    @Override
    public String toString() { return idNit + " - " + nombreCompleto; }
}
