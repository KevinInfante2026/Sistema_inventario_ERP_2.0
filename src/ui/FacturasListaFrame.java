package ui;

import dao.FacturaDAO;
import model.DetalleFactura;
import model.Factura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacturasListaFrame extends JFrame {

    private final FacturaDAO dao = new FacturaDAO();
    private DefaultTableModel modeloFacturas, modeloDetalles;
    private JTable tablaFacturas, tablaDetalles;

    public FacturasListaFrame() {
        setTitle("Historial de Facturas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarFacturas();
    }

    private void construirUI() {
        setLayout(new GridLayout(2, 1, 0, 5));

        // Tabla facturas
        String[] colsF = {"ID", "Fecha", "Cajero", "Cliente", "Forma Pago", "Total", "Fianza", "Vueltos"};
        modeloFacturas = new DefaultTableModel(colsF, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaFacturas = new JTable(modeloFacturas);
        tablaFacturas.setRowHeight(22);
        tablaFacturas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaFacturas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaFacturas.getSelectedRow() >= 0) cargarDetalles();
        });

        JScrollPane scroll1 = new JScrollPane(tablaFacturas);
        scroll1.setBorder(BorderFactory.createTitledBorder("Facturas"));

        // Tabla detalles
        String[] colsD = {"ID Prod", "Descripción", "Cantidad", "Valor Unit.", "Importe"};
        modeloDetalles = new DefaultTableModel(colsD, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(22);
        tablaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDetalles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll2 = new JScrollPane(tablaDetalles);
        scroll2.setBorder(BorderFactory.createTitledBorder("Detalle de la Factura Seleccionada"));

        add(scroll1);
        add(scroll2);
    }

    private void cargarFacturas() {
        modeloFacturas.setRowCount(0);
        for (Factura f : dao.listarTodas()) {
            modeloFacturas.addRow(new Object[]{
                f.getIdFactura(), f.getFecha(), f.getNombreCajero(), f.getClienteIdNit(),
                f.getFormaPago(), f.getTotal(), f.getFianza(), f.getVueltos()
            });
        }
    }

    private void cargarDetalles() {
        int row = tablaFacturas.getSelectedRow();
        if (row < 0) return;
        int idFactura = (int) modeloFacturas.getValueAt(row, 0);
        modeloDetalles.setRowCount(0);
        for (DetalleFactura d : dao.obtenerDetalles(idFactura)) {
            modeloDetalles.addRow(new Object[]{
                d.getIdProducto(), d.getDescripcionArticulo(),
                d.getCantidad(), d.getValorUnitario(), d.getImporte()
            });
        }
    }
}
