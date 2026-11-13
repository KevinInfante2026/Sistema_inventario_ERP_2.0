package model;

public class Cuenta {
    private int id;
    private String fecha;
    private String clienteId;
    private String metodoPago;
    private double totalVenta;
    private double fianza;
    private String empleado;
    private String tipo; // INGRESO, EGRESO, ABONO

    public Cuenta() {}

    public Cuenta(String fecha, String clienteId, String metodoPago,
                  double totalVenta, double fianza, String empleado, String tipo) {
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.metodoPago = metodoPago;
        this.totalVenta = totalVenta;
        this.fianza = fianza;
        this.empleado = empleado;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    public double getFianza() { return fianza; }
    public void setFianza(double fianza) { this.fianza = fianza; }

    public String getEmpleado() { return empleado; }
    public void setEmpleado(String empleado) { this.empleado = empleado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
