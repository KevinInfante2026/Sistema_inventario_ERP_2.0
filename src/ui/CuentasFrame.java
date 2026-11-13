package ui;

import dao.CuentaDAO;
import model.Cuenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CuentasFrame extends JFrame {

    private final CuentaDAO dao = new CuentaDAO();

    private JTextField txtFecha, txtClienteId, txtEmpleado, txtTotal, txtFianza;
    private JComboBox<String> cmbMetodoPago, cmbTipo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalVentas, lblTotalDeuda, lblTotalCaja;

    public CuentasFrame() {
        setTitle("Módulo de Cuentas");
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarTabla();
        actualizarResumen();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar Movimiento"));
        panelForm.setBackground(new Color(240, 255, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtFecha     = campo(12); txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtClienteId = campo(12);
        txtEmpleado  = campo(14);
        txtTotal     = campo(12); txtTotal.setText("0");
        txtFianza    = campo(12); txtFianza.setText("0");
        cmbMetodoPago= new JComboBox<>(new String[]{"Efectivo","Transferencia","Crédito","Débito"});
        cmbTipo      = new JComboBox<>(new String[]{"INGRESO","EGRESO","ABONO"});
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridy=0; addPar(panelForm,gbc,"Fecha:",         txtFecha,     "Cliente ID:", txtClienteId);
        gbc.gridy=1; addPar(panelForm,gbc,"Método de Pago:",cmbMetodoPago,"Tipo:",       cmbTipo);
        gbc.gridy=2; addPar(panelForm,gbc,"Total Venta:",   txtTotal,     "Fianza:",     txtFianza);
        gbc.gridy=3;
        gbc.gridx=0; gbc.weightx=0; panelForm.add(new JLabel("Empleado:"), gbc);
        gbc.gridx=1; gbc.weightx=1; panelForm.add(txtEmpleado, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        panelBotones.setBackground(new Color(240, 255, 245));
        JButton btnGuardar = boton("Guardar", new Color(26, 188, 156));
        JButton btnLimpiar = boton("Limpiar", new Color(149, 165, 166));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);

        // Resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        panelResumen.setBackground(new Color(30, 60, 114));
        lblTotalVentas = resumenLabel("Ventas: $0");
        lblTotalDeuda  = resumenLabel("Deuda: $0");
        lblTotalCaja   = resumenLabel("Caja: $0");
        panelResumen.add(lblTotalVentas);
        panelResumen.add(new JLabel(" | ")).setForeground(Color.WHITE);
        panelResumen.add(lblTotalDeuda);
        panelResumen.add(new JLabel(" | ")).setForeground(Color.WHITE);
        panelResumen.add(lblTotalCaja);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm,    BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla
        String[] cols = {"ID","Fecha","Cliente","Método Pago","Total Venta","Fianza","Empleado","Tipo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(panelResumen,  BorderLayout.NORTH);
        add(panelSuperior, BorderLayout.CENTER);
        add(new JScrollPane(tabla), BorderLayout.SOUTH);

        // Forzamos que la tabla tome todo el espacio sobrante
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelSuperior, new JScrollPane(tabla));
        split.setDividerLocation(220);
        getContentPane().removeAll();
        add(panelResumen, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void guardar() {
        if (txtTotal.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Ingresa el total."); return; }
        try { Double.parseDouble(txtTotal.getText().trim()); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Total inválido."); return; }

        Cuenta cu = new Cuenta(
                txtFecha.getText().trim(),
                txtClienteId.getText().trim(),
                (String) cmbMetodoPago.getSelectedItem(),
                Double.parseDouble(txtTotal.getText().trim()),
                parseDouble(txtFianza.getText()),
                txtEmpleado.getText().trim(),
                (String) cmbTipo.getSelectedItem()
        );
        if (dao.guardar(cu)) {
            JOptionPane.showMessageDialog(this, "✅ Movimiento registrado.");
            limpiar(); cargarTabla(); actualizarResumen();
        }
    }

    private void limpiar() {
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtClienteId.setText(""); txtEmpleado.setText("");
        txtTotal.setText("0"); txtFianza.setText("0");
        cmbMetodoPago.setSelectedIndex(0); cmbTipo.setSelectedIndex(0);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cuenta cu : dao.listarTodas()) {
            modeloTabla.addRow(new Object[]{
                cu.getId(), cu.getFecha(), cu.getClienteId(), cu.getMetodoPago(),
                cu.getTotalVenta(), cu.getFianza(), cu.getEmpleado(), cu.getTipo()
            });
        }
    }

    private void actualizarResumen() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es","CO"));
        double ventas = dao.totalVentas();
        double deuda  = dao.totalDeuda();
        double caja   = ventas - deuda;
        lblTotalVentas.setText("Total Ventas: " + nf.format(ventas));
        lblTotalDeuda.setText("Total Deuda: " + nf.format(deuda));
        lblTotalCaja.setText("Total Caja: " + nf.format(caja));
    }

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
    private JLabel resumenLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(Color.WHITE); return l;
    }
    private void addPar(JPanel p, GridBagConstraints gbc, String l1, JComponent c1, String l2, JComponent c2) {
        gbc.gridx=0; gbc.weightx=0; p.add(new JLabel(l1), gbc);
        gbc.gridx=1; gbc.weightx=1; p.add(c1, gbc);
        gbc.gridx=2; gbc.weightx=0; p.add(new JLabel(l2), gbc);
        gbc.gridx=3; gbc.weightx=1; p.add(c2, gbc);
    }
}
