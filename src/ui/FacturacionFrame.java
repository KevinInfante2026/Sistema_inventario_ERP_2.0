package ui;

import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.FacturaDAO;
import dao.ProductoDAO;
import dao.VentaDiariaDAO;
import dao.CuentaDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FacturacionFrame extends JFrame {

    private final FacturaDAO    facturaDAO    = new FacturaDAO();
    private final ProductoDAO   productoDAO   = new ProductoDAO();
    private final ClienteDAO    clienteDAO    = new ClienteDAO();
    private final EmpleadoDAO   empleadoDAO   = new EmpleadoDAO();
    private final VentaDiariaDAO ventaDAO     = new VentaDiariaDAO();
    private final CuentaDAO     cuentaDAO     = new CuentaDAO();

    // Encabezado factura
    private JTextField txtFecha, txtIdCajero, txtNombreCajero;
    // Datos cliente
    private JTextField txtClienteId, txtClienteNombre, txtClienteDireccion,
            txtClienteCiudad, txtClienteTelefono, txtClienteCorreo, txtClienteSaldo;
    // Detalles
    private JTextField txtCodArt, txtDescArt, txtCantArt, txtValorArt;
    // Pagos
    private JComboBox<String> cmbFormaPago;
    private JTextField txtDomicilio, txtDescuento, txtDineroRecibido, txtAbonoSaldo, txtFianza;
    // Totales
    private JLabel lblTotal, lblVueltos, lblSaldoNuevo, lblCantProductos;
    // Tabla detalles
    private DefaultTableModel modeloDetalle;
    private JTable tablaDetalle;
    // Estado
    private double totalCalculado = 0;

    public FacturacionFrame() {
        setTitle("Módulo de Facturación");
        setSize(1100, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));
        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ─── FILA 1: Encabezado + Datos Cliente ────────────────────────────
        JPanel panelFila1 = new JPanel(new GridLayout(1, 2, 8, 0));
        panelFila1.add(panelEncabezado());
        panelFila1.add(panelCliente());

        // ─── FILA 2: Tabla de productos ─────────────────────────────────────
        JPanel panelFila2 = panelDetalles();

        // ─── FILA 3: Pagos + Totales + Botones ──────────────────────────────
        JPanel panelFila3 = new JPanel(new GridLayout(1, 2, 8, 0));
        panelFila3.add(panelPagos());
        panelFila3.add(panelTotales());

        // ─── Botones principales ─────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        JButton btnGuardar = boton("✅ GUARDAR FACTURA", new Color(46, 204, 113));
        JButton btnLimpiar = boton("🧹 LIMPIAR",         new Color(149, 165, 166));
        JButton btnVerFacturas = boton("📋 VER FACTURAS", new Color(52, 152, 219));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVerFacturas);

        panelPrincipal.add(panelFila1,  BorderLayout.NORTH);
        panelPrincipal.add(panelFila2,  BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelFila3,   BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);

        // Acciones
        btnGuardar.addActionListener(e -> guardarFactura());
        btnLimpiar.addActionListener(e -> limpiarTodo());
        btnVerFacturas.addActionListener(e -> new FacturasListaFrame().setVisible(true));
    }

    private JPanel panelEncabezado() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del Cajero"));
        p.setBackground(new Color(255, 252, 240));
        GridBagConstraints g = gbc();

        txtFecha       = campo(15); txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); txtFecha.setEditable(false);
        txtIdCajero    = campo(8);
        txtNombreCajero= campo(15);

        addFila(p,g,0,"Fecha:",         txtFecha);
        addFila(p,g,1,"ID Cajero:",     txtIdCajero);
        addFila(p,g,2,"Nombre Cajero:", txtNombreCajero);

        JButton btnBuscarCajero = smallBoton("Buscar Cajero", new Color(155, 89, 182));
        g.gridy=3; g.gridx=1; p.add(btnBuscarCajero, g);
        btnBuscarCajero.addActionListener(e -> buscarEmpleado());
        return p;
    }

    private JPanel panelCliente() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        p.setBackground(new Color(240, 255, 250));
        GridBagConstraints g = gbc();

        txtClienteId        = campo(12);
        txtClienteNombre    = campo(18); txtClienteNombre.setEditable(false);
        txtClienteDireccion = campo(18); txtClienteDireccion.setEditable(false);
        txtClienteCiudad    = campo(12); txtClienteCiudad.setEditable(false);
        txtClienteTelefono  = campo(12); txtClienteTelefono.setEditable(false);
        txtClienteCorreo    = campo(18); txtClienteCorreo.setEditable(false);
        txtClienteSaldo     = campo(10); txtClienteSaldo.setEditable(false);

        addFila(p,g,0,"N° ID/NIT:", txtClienteId);
        addFila(p,g,1,"Nombre:",     txtClienteNombre);
        addFila(p,g,2,"Dirección:",  txtClienteDireccion);
        addFila(p,g,3,"Ciudad:",     txtClienteCiudad);
        addFila(p,g,4,"Saldo:",      txtClienteSaldo);

        JButton btnBuscarCliente = smallBoton("Buscar Cliente", new Color(46, 204, 113));
        g.gridy=5; g.gridx=1; p.add(btnBuscarCliente, g);
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        return p;
    }

    private JPanel panelDetalles() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(BorderFactory.createTitledBorder("Detalles de Compra"));

        // Fila para agregar producto
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        txtCodArt   = campo(6);
        txtDescArt  = campo(18);
        txtCantArt  = campo(5);
        txtValorArt = campo(8);

        panelAgregar.add(new JLabel("Cód.Art:")); panelAgregar.add(txtCodArt);
        panelAgregar.add(new JLabel("Descripción:")); panelAgregar.add(txtDescArt);
        panelAgregar.add(new JLabel("Cantidad:")); panelAgregar.add(txtCantArt);
        panelAgregar.add(new JLabel("Valor Unit:")); panelAgregar.add(txtValorArt);

        JButton btnBuscarProd = smallBoton("🔍 Buscar Producto", new Color(52, 152, 219));
        JButton btnAgregarFila= smallBoton("➕ Agregar", new Color(46, 204, 113));
        JButton btnQuitarFila = smallBoton("➖ Quitar fila", new Color(231, 76, 60));

        panelAgregar.add(btnBuscarProd);
        panelAgregar.add(btnAgregarFila);
        panelAgregar.add(btnQuitarFila);

        // Tabla
        String[] cols = {"ID Prod", "Descripción", "Cantidad", "Valor Unitario", "Importe"};
        modeloDetalle = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setRowHeight(22);
        tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDetalle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        p.add(panelAgregar, BorderLayout.NORTH);
        p.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        btnBuscarProd.addActionListener(e -> buscarProducto());
        btnAgregarFila.addActionListener(e -> agregarFila());
        btnQuitarFila.addActionListener(e -> quitarFila());
        return p;
    }

    private JPanel panelPagos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Formas de Pago"));
        p.setBackground(new Color(255, 245, 245));
        GridBagConstraints g = gbc();

        cmbFormaPago    = new JComboBox<>(new String[]{"Efectivo","Transferencia","Crédito","Débito"});
        cmbFormaPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtDomicilio    = campo(10); txtDomicilio.setText("0");
        txtDescuento    = campo(10); txtDescuento.setText("0");
        txtDineroRecibido= campo(10); txtDineroRecibido.setText("0");
        txtAbonoSaldo   = campo(10); txtAbonoSaldo.setText("0");
        txtFianza       = campo(10); txtFianza.setText("0");

        g.gridy=0; addFila2(p,g,"Forma de Pago:", cmbFormaPago);
        g.gridy=1; addFila2(p,g,"Domicilio ($):", txtDomicilio);
        g.gridy=2; addFila2(p,g,"Descuento ($):", txtDescuento);
        g.gridy=3; addFila2(p,g,"Dinero Recibido:", txtDineroRecibido);
        g.gridy=4; addFila2(p,g,"Abono a Saldo:", txtAbonoSaldo);
        g.gridy=5; addFila2(p,g,"Fianza ($):", txtFianza);

        // Recalcular al cambiar valores
        ActionListener recalc = e -> recalcularTotales();
        txtDomicilio.addActionListener(recalc); txtDescuento.addActionListener(recalc);
        txtDineroRecibido.addActionListener(recalc);
        txtDomicilio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { recalcularTotales(); }
        });
        txtDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { recalcularTotales(); }
        });
        txtDineroRecibido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { recalcularTotales(); }
        });
        return p;
    }

    private JPanel panelTotales() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Totales"));
        p.setBackground(new Color(240, 250, 255));
        GridBagConstraints g = gbc();

        lblCantProductos = labelValor("0");
        lblTotal         = labelValor("$ 0.00");
        lblVueltos       = labelValor("$ 0.00");
        lblSaldoNuevo    = labelValor("$ 0.00");

        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(0, 128, 0));

        addFila2(p,g, "Cant. Productos:", lblCantProductos); g.gridy++;
        addFila2(p,g, "TOTAL:",           lblTotal);         g.gridy++;
        addFila2(p,g, "Vueltos:",         lblVueltos);       g.gridy++;
        addFila2(p,g, "Saldo Nuevo:",     lblSaldoNuevo);

        JButton btnRecalc = boton("🔄 Recalcular", new Color(52, 152, 219));
        g.gridy++; g.gridx=0; g.gridwidth=2; p.add(btnRecalc, g);
        btnRecalc.addActionListener(e -> recalcularTotales());
        return p;
    }

    // ─── Lógica ────────────────────────────────────────────────────────────

    private void buscarEmpleado() {
        String id = txtIdCajero.getText().trim();
        if (id.isEmpty()) return;
        try {
            Empleado em = empleadoDAO.buscarPorId(Integer.parseInt(id));
            if (em != null) txtNombreCajero.setText(em.getNombreCompleto());
            else JOptionPane.showMessageDialog(this, "Empleado no encontrado.");
        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "ID inválido."); }
    }

    private void buscarCliente() {
        String id = txtClienteId.getText().trim();
        if (id.isEmpty()) return;
        Cliente cl = clienteDAO.buscarPorId(id);
        if (cl != null) {
            txtClienteNombre.setText(cl.getNombreCompleto());
            txtClienteDireccion.setText(cl.getDireccion());
            txtClienteCiudad.setText(cl.getCiudad());
            txtClienteTelefono.setText(cl.getTelefono());
            txtClienteCorreo.setText(cl.getCorreo());
            txtClienteSaldo.setText(String.valueOf(cl.getSaldo()));
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
        }
    }

    private void buscarProducto() {
        String cod = txtCodArt.getText().trim();
        if (cod.isEmpty()) return;
        try {
            Producto p = productoDAO.buscarPorId(Integer.parseInt(cod));
            if (p != null) {
                txtDescArt.setText(p.getNombre());
                txtValorArt.setText(String.valueOf(p.getPrecioUnitario()));
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            }
        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Código inválido."); }
    }

    private void agregarFila() {
        String cod  = txtCodArt.getText().trim();
        String desc = txtDescArt.getText().trim();
        String cant = txtCantArt.getText().trim();
        String val  = txtValorArt.getText().trim();

        if (desc.isEmpty() || cant.isEmpty() || val.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete Descripción, Cantidad y Valor.");
            return;
        }
        int cantidad;
        double valorUnit;
        try { cantidad = Integer.parseInt(cant); if (cantidad <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Cantidad inválida (debe ser > 0)."); return; }
        try { valorUnit = Double.parseDouble(val); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Valor unitario inválido."); return; }

        // Verificar stock si hay ID de producto
        if (!cod.isEmpty()) {
            try {
                Producto p = productoDAO.buscarPorId(Integer.parseInt(cod));
                if (p != null && p.getCantidadDisponible() < cantidad) {
                    JOptionPane.showMessageDialog(this, "⚠ Stock insuficiente. Disponible: " + p.getCantidadDisponible());
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        double importe = cantidad * valorUnit;
        modeloDetalle.addRow(new Object[]{ cod.isEmpty() ? 0 : Integer.parseInt(cod), desc, cantidad, valorUnit, importe });
        txtCodArt.setText(""); txtDescArt.setText(""); txtCantArt.setText(""); txtValorArt.setText("");
        recalcularTotales();
    }

    private void quitarFila() {
        int row = tablaDetalle.getSelectedRow();
        if (row >= 0) { modeloDetalle.removeRow(row); recalcularTotales(); }
        else JOptionPane.showMessageDialog(this, "Selecciona una fila para quitar.");
    }

    private void recalcularTotales() {
        double subtotal = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            subtotal += (double) modeloDetalle.getValueAt(i, 4);
        }
        double domicilio = parseDouble(txtDomicilio.getText());
        double descuento = parseDouble(txtDescuento.getText());
        totalCalculado = subtotal + domicilio - descuento;
        double dineroRecibido = parseDouble(txtDineroRecibido.getText());
        double vueltos = dineroRecibido - totalCalculado;

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es","CO"));
        lblTotal.setText(nf.format(totalCalculado));
        lblVueltos.setText(nf.format(vueltos));
        lblCantProductos.setText(String.valueOf(modeloDetalle.getRowCount()));

        // Saldo nuevo del cliente
        double saldoActual = parseDouble(txtClienteSaldo.getText());
        double abono       = parseDouble(txtAbonoSaldo.getText());
        double fianza      = parseDouble(txtFianza.getText());
        double saldoNuevo  = saldoActual - abono + fianza;
        lblSaldoNuevo.setText(nf.format(saldoNuevo));
    }

    private void guardarFactura() {
        if (modeloDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto."); return;
        }
        if (txtIdCajero.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID del cajero es obligatorio."); return;
        }

        Factura f = new Factura();
        f.setFecha(txtFecha.getText());
        try { f.setIdCajero(Integer.parseInt(txtIdCajero.getText().trim())); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "ID cajero inválido."); return; }
        f.setNombreCajero(txtNombreCajero.getText().trim());
        f.setClienteIdNit(txtClienteId.getText().trim());
        f.setFormaPago((String) cmbFormaPago.getSelectedItem());
        f.setDomicilio(parseDouble(txtDomicilio.getText()));
        f.setDescuento(parseDouble(txtDescuento.getText()));
        f.setTotal(totalCalculado);
        f.setDineroRecibido(parseDouble(txtDineroRecibido.getText()));
        f.setVueltos(parseDouble(txtDineroRecibido.getText()) - totalCalculado);
        f.setAbonoSaldo(parseDouble(txtAbonoSaldo.getText()));
        f.setFianza(parseDouble(txtFianza.getText()));
        double saldoActual = parseDouble(txtClienteSaldo.getText());
        f.setSaldoNuevo(saldoActual - f.getAbonoSaldo() + f.getFianza());

        // Armar detalles
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            DetalleFactura d = new DetalleFactura();
            d.setIdProducto((int) modeloDetalle.getValueAt(i, 0));
            d.setDescripcionArticulo(modeloDetalle.getValueAt(i, 1).toString());
            d.setCantidad((int) modeloDetalle.getValueAt(i, 2));
            d.setValorUnitario((double) modeloDetalle.getValueAt(i, 3));
            d.setImporte((double) modeloDetalle.getValueAt(i, 4));
            f.getDetalles().add(d);
        }

        int idFactura = facturaDAO.guardar(f);
        if (idFactura < 0) { JOptionPane.showMessageDialog(this, "❌ Error al guardar la factura."); return; }

        // Descontar inventario y registrar ventas diarias
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        for (DetalleFactura d : f.getDetalles()) {
            if (d.getIdProducto() > 0) {
                boolean ok = productoDAO.descontarStock(d.getIdProducto(), d.getCantidad());
                if (!ok) JOptionPane.showMessageDialog(this,
                        "⚠ Sin stock suficiente para: " + d.getDescripcionArticulo() + ". Inventario no descontado.");
            }
            VentaDiaria vd = new VentaDiaria(fechaHoy, d.getIdProducto(),
                    d.getDescripcionArticulo(), d.getCantidad(), d.getValorUnitario());
            ventaDAO.guardar(vd);
        }

        // Actualizar saldo del cliente
        if (!f.getClienteIdNit().isEmpty()) {
            clienteDAO.actualizarSaldo(f.getClienteIdNit(), f.getSaldoNuevo());
        }

        // Registrar en cuentas
        Cuenta cuenta = new Cuenta(fechaHoy, f.getClienteIdNit(),
                f.getFormaPago(), totalCalculado, f.getFianza(), f.getNombreCajero(), "INGRESO");
        cuentaDAO.guardar(cuenta);

        JOptionPane.showMessageDialog(this, "✅ Factura #" + idFactura + " guardada correctamente.");
        limpiarTodo();
    }

    private void limpiarTodo() {
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtIdCajero.setText(""); txtNombreCajero.setText("");
        txtClienteId.setText(""); txtClienteNombre.setText(""); txtClienteDireccion.setText("");
        txtClienteCiudad.setText(""); txtClienteTelefono.setText(""); txtClienteCorreo.setText("");
        txtClienteSaldo.setText("0");
        txtCodArt.setText(""); txtDescArt.setText(""); txtCantArt.setText(""); txtValorArt.setText("");
        txtDomicilio.setText("0"); txtDescuento.setText("0"); txtDineroRecibido.setText("0");
        txtAbonoSaldo.setText("0"); txtFianza.setText("0");
        modeloDetalle.setRowCount(0);
        totalCalculado = 0;
        lblTotal.setText("$ 0.00"); lblVueltos.setText("$ 0.00");
        lblSaldoNuevo.setText("$ 0.00"); lblCantProductos.setText("0");
    }

    // ─── Helpers ───────────────────────────────────────────────────────────

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0; }
    }

    private JTextField campo(int cols) {
        JTextField f = new JTextField(cols); f.setFont(new Font("Segoe UI", Font.PLAIN, 12)); return f;
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }

    private JButton smallBoton(String t, Color c) {
        JButton b = boton(t, c); b.setFont(new Font("Segoe UI", Font.BOLD, 11)); return b;
    }

    private JLabel labelValor(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 14)); return l;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6); g.anchor = GridBagConstraints.WEST; return g;
    }

    private void addFila(JPanel p, GridBagConstraints g, int row, String lbl, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0; p.add(new JLabel(lbl), g);
        g.gridx = 1; g.weightx = 1; p.add(c, g);
    }

    private void addFila2(JPanel p, GridBagConstraints g, String lbl, JComponent c) {
        g.gridx = 0; g.weightx = 0; p.add(new JLabel(lbl), g);
        g.gridx = 1; g.weightx = 1; p.add(c, g);
    }
}
