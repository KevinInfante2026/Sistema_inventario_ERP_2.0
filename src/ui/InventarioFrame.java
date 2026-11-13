package ui;

import dao.ProductoDAO;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InventarioFrame extends JFrame {

    private final ProductoDAO dao = new ProductoDAO();

    private JTextField txtId, txtNombre, txtCategoria, txtCantidad, txtPrecio, txtFecha;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar;

    public InventarioFrame() {
        setTitle("Módulo de Inventario");
        setSize(920, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));

        // ── Panel superior: formulario ──────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        panelForm.setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId       = crearCampo(); txtId.setEditable(false); txtId.setBackground(new Color(220,220,220));
        txtNombre   = crearCampo();
        txtCategoria= crearCampo();
        txtCantidad = crearCampo();
        txtPrecio   = crearCampo();
        txtFecha    = crearCampo();
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        agregarFila(panelForm, gbc, 0, "ID (auto):", txtId,      "Nombre:", txtNombre);
        agregarFila(panelForm, gbc, 1, "Categoría:", txtCategoria,"Cantidad:", txtCantidad);
        agregarFila(panelForm, gbc, 2, "Precio Unitario:", txtPrecio, "Fecha Ingreso:", txtFecha);

        // ── Panel de botones ────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelBotones.setBackground(new Color(245, 248, 255));

        btnGuardar   = boton("Guardar",    new Color(46, 204, 113));
        btnActualizar= boton("Actualizar", new Color(52, 152, 219));
        btnEliminar  = boton("Eliminar",   new Color(231, 76, 60));
        btnLimpiar   = boton("Limpiar",    new Color(149, 165, 166));
        btnBuscar    = boton("🔍 Buscar por ID", new Color(155, 89, 182));

        JTextField txtBuscarId = new JTextField(6);
        txtBuscarId.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(new JLabel("  ID:"));
        panelBotones.add(txtBuscarId);
        panelBotones.add(btnBuscar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm,     BorderLayout.CENTER);
        panelSuperior.add(panelBotones,  BorderLayout.SOUTH);

        // ── Tabla ───────────────────────────────────────────────────────────
        String[] cols = {"ID", "Nombre", "Categoría", "Cantidad", "Precio Unitario", "Fecha Ingreso"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) cargarEnFormulario();
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // ── Acciones ────────────────────────────────────────────────────────
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnBuscar.addActionListener(e -> {
            String val = txtBuscarId.getText().trim();
            if (val.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingresa un ID"); return; }
            try {
                Producto p = dao.buscarPorId(Integer.parseInt(val));
                if (p == null) { JOptionPane.showMessageDialog(this, "Producto no encontrado"); return; }
                cargarProductoEnFormulario(p);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido");
            }
        });
    }

    private void guardar() {
        if (!validar()) return;
        Producto p = leerFormulario();
        if (dao.guardarOActualizar(p)) {
            JOptionPane.showMessageDialog(this, "✅ Producto guardado/actualizado correctamente.");
            limpiar(); cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un producto primero."); return; }
        if (!validar()) return;
        Producto p = leerFormulario();
        p.setIdProducto(Integer.parseInt(txtId.getText()));
        if (dao.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "✅ Producto actualizado.");
            limpiar(); cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.eliminar(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Producto eliminado.");
            limpiar(); cargarTabla();
        }
    }

    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtCategoria.setText("");
        txtCantidad.setText(""); txtPrecio.setText("");
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tabla.clearSelection();
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return false; }
        try {
            int cant = Integer.parseInt(txtCantidad.getText().trim());
            if (cant < 0) { JOptionPane.showMessageDialog(this, "La cantidad no puede ser negativa."); return false; }
        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Cantidad inválida."); return false; }
        try { Double.parseDouble(txtPrecio.getText().trim()); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Precio inválido."); return false; }
        return true;
    }

    private Producto leerFormulario() {
        Producto p = new Producto();
        p.setNombre(txtNombre.getText().trim());
        p.setCategoria(txtCategoria.getText().trim());
        p.setCantidadDisponible(Integer.parseInt(txtCantidad.getText().trim()));
        p.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
        p.setFechaIngreso(txtFecha.getText().trim());
        return p;
    }

    private void cargarEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        txtId.setText(modeloTabla.getValueAt(row, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(row, 1).toString());
        txtCategoria.setText(modeloTabla.getValueAt(row, 2).toString());
        txtCantidad.setText(modeloTabla.getValueAt(row, 3).toString());
        txtPrecio.setText(modeloTabla.getValueAt(row, 4).toString());
        txtFecha.setText(modeloTabla.getValueAt(row, 5).toString());
    }

    private void cargarProductoEnFormulario(Producto p) {
        txtId.setText(String.valueOf(p.getIdProducto()));
        txtNombre.setText(p.getNombre());
        txtCategoria.setText(p.getCategoria());
        txtCantidad.setText(String.valueOf(p.getCantidadDisponible()));
        txtPrecio.setText(String.valueOf(p.getPrecioUnitario()));
        txtFecha.setText(p.getFechaIngreso());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = dao.listarTodos();
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(), p.getNombre(), p.getCategoria(),
                p.getCantidadDisponible(), p.getPrecioUnitario(), p.getFechaIngreso()
            });
        }
    }

    // ── Helpers ─────────────────────────────────────────────────────────────
    private JTextField crearCampo() {
        JTextField f = new JTextField(16);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JButton boton(String txt, Color color) {
        JButton b = new JButton(txt);
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila,
                              String lbl1, JComponent c1, String lbl2, JComponent c2) {
        gbc.gridy = fila;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel(lbl1), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(c1, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel(lbl2), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(c2, gbc);
    }
}
