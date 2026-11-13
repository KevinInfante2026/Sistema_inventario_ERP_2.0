package model;

public class DetalleFactura {
    private int idDetalle;
    private int idFactura;
    private int idProducto;
    private String descripcionArticulo;
    private int cantidad;
    private double valorUnitario;
    private double importe;

    public DetalleFactura() {}

    public DetalleFactura(int idFactura, int idProducto, String descripcionArticulo,
                           int cantidad, double valorUnitario) {
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.descripcionArticulo = descripcionArticulo;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.importe = cantidad * valorUnitario;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getDescripcionArticulo() { return descripcionArticulo; }
    public void setDescripcionArticulo(String d) { this.descripcionArticulo = d; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.importe = cantidad * valorUnitario;
    }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        this.importe = cantidad * valorUnitario;
    }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
}
