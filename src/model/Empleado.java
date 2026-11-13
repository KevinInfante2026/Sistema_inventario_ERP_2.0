package model;

public class Empleado {
    private int codigoEmpleado;
    private String nombreCompleto;
    private String cargo;
    private String documentoIdentidad;

    public Empleado() {}

    public Empleado(int codigoEmpleado, String nombreCompleto,
                    String cargo, String documentoIdentidad) {
        this.codigoEmpleado = codigoEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.cargo = cargo;
        this.documentoIdentidad = documentoIdentidad;
    }

    public int getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(int codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    @Override
    public String toString() { return codigoEmpleado + " - " + nombreCompleto; }
}
