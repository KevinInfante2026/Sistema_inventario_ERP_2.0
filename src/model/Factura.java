package model;

import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int idFactura;
    private String fecha;
    private int idCajero;
    private String nombreCajero;
    private String clienteIdNit;
    private String formaPago;       // Efectivo, Transferencia, Crédito
    private double domicilio;
    private double descuento;
    private double total;
    private double dineroRecibido;
    private double vueltos;
    private double abonoSaldo;
    private double fianza;
    private double saldoNuevo;
    private List<DetalleFactura> detalles;

    public Factura() {
        detalles = new ArrayList<>();
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }

    public String getNombreCajero() { return nombreCajero; }
    public void setNombreCajero(String nombreCajero) { this.nombreCajero = nombreCajero; }

    public String getClienteIdNit() { return clienteIdNit; }
    public void setClienteIdNit(String clienteIdNit) { this.clienteIdNit = clienteIdNit; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public double getDomicilio() { return domicilio; }
    public void setDomicilio(double domicilio) { this.domicilio = domicilio; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getDineroRecibido() { return dineroRecibido; }
    public void setDineroRecibido(double dineroRecibido) { this.dineroRecibido = dineroRecibido; }

    public double getVueltos() { return vueltos; }
    public void setVueltos(double vueltos) { this.vueltos = vueltos; }

    public double getAbonoSaldo() { return abonoSaldo; }
    public void setAbonoSaldo(double abonoSaldo) { this.abonoSaldo = abonoSaldo; }

    public double getFianza() { return fianza; }
    public void setFianza(double fianza) { this.fianza = fianza; }

    public double getSaldoNuevo() { return saldoNuevo; }
    public void setSaldoNuevo(double saldoNuevo) { this.saldoNuevo = saldoNuevo; }

    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }

    public void calcularTotal() {
        total = 0;
        for (DetalleFactura d : detalles) {
            total += d.getImporte();
        }
        total += domicilio - descuento;
        vueltos = dineroRecibido - total;
    }
}
