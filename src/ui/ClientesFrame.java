package ui;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesFrame extends JFrame {

    private final ClienteDAO dao = new ClienteDAO();

    private JTextField txtId, txtNombre, txtDireccion, txtCiudad, txtTelefono, txtCorreo, txtSaldo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public ClientesFrame() {
        setTitle("Módulo de Clientes");
        setSize(1000, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelForm.setBackground(new Color(245, 255, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId        = campo(); 
        txtNombre    = campo();
        txtDireccion = campo();
        txtCiudad    = campo();
        txtTelefono  = campo();
        txtCorreo    = campo();
        txtSaldo     = campo(); txtSaldo.setText("0");

        gbc.gridy=0; addPar(panelForm,gbc, "N° ID/NIT:", txtId,       "Nombre completo:", txtNombre);
        gbc.gridy=1; addPar(panelForm,gbc, "Dirección:",  txtDireccion,"Ciudad:",           txtCiudad);
        gbc.gridy=2; addPar(panelForm,gbc, "Teléfono:",   txtTelefono, "Correo electrónico:",txtCorreo);
        gbc.gridy=3;
        gbc.gridx=0; gbc.weightx=0; panelForm.add(new JLabel("Saldo:"), gbc);
        gbc.gridx=1; gbc.weightx=1; panelForm.add(txtSaldo, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelBotones.setBackground(new Color(245, 255, 250));

        JButton btnGuardar    = boton("Guardar",    new Color(46, 204, 113));
        JButton btnActualizar = boton("Actualizar", new Color(52, 152, 219));
        JButton btnEliminar   = boton("Eliminar",   new Color(231, 76, 60));
        JButton btnLimpiar    = boton("Limpiar",    new Color(149, 165, 166));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm,    BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla
        String[] cols = {"ID/NIT", "Nombre", "Dirección", "Ciudad", "Teléfono", "Correo", "Saldo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) cargarEnFormulario();
        });

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void guardar() {
        if (!validar()) return;
        if (dao.buscarPorId(txtId.getText().trim()) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese ID/NIT.");
            return;
        }
        if (dao.guardar(leerFormulario())) {
            JOptionPane.showMessageDialog(this, "✅ Cliente guardado.");
            limpiar(); cargarTabla();
        }
    }

    private void actualizar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        if (!validar()) return;
        if (dao.actualizar(leerFormulario())) {
            JOptionPane.showMessageDialog(this, "✅ Cliente actualizado.");
            limpiar(); cargarTabla();
        }
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dao.eliminar(txtId.getText().trim());
            JOptionPane.showMessageDialog(this, "✅ Cliente eliminado.");
            limpiar(); cargarTabla();
        }
    }

    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtDireccion.setText("");
        txtCiudad.setText(""); txtTelefono.setText(""); txtCorreo.setText(""); txtSaldo.setText("0");
        tabla.clearSelection();
        txtId.setEditable(true);
    }

    private boolean validar() {
        if (txtId.getText().trim().isEmpty())     { JOptionPane.showMessageDialog(this, "El ID/NIT es obligatorio."); return false; }
        if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return false; }
        try { Double.parseDouble(txtSaldo.getText().trim()); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Saldo inválido."); return false; }
        return true;
    }

    private Cliente leerFormulario() {
        return new Cliente(
                txtId.getText().trim(),
                txtNombre.getText().trim(),
                txtDireccion.getText().trim(),
                txtCiudad.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                Double.parseDouble(txtSaldo.getText().trim())
        );
    }

    private void cargarEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        txtId.setText(modeloTabla.getValueAt(row,0).toString()); txtId.setEditable(false);
        txtNombre.setText(modeloTabla.getValueAt(row,1).toString());
        txtDireccion.setText(modeloTabla.getValueAt(row,2).toString());
        txtCiudad.setText(modeloTabla.getValueAt(row,3).toString());
        txtTelefono.setText(modeloTabla.getValueAt(row,4).toString());
        txtCorreo.setText(modeloTabla.getValueAt(row,5).toString());
        txtSaldo.setText(modeloTabla.getValueAt(row,6).toString());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente cl : dao.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                cl.getIdNit(), cl.getNombreCompleto(), cl.getDireccion(),
                cl.getCiudad(), cl.getTelefono(), cl.getCorreo(), cl.getSaldo()
            });
        }
    }

    private JTextField campo() {
        JTextField f = new JTextField(16);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void addPar(JPanel p, GridBagConstraints gbc, String l1, JComponent c1, String l2, JComponent c2) {
        gbc.gridx=0; gbc.weightx=0; p.add(new JLabel(l1), gbc);
        gbc.gridx=1; gbc.weightx=1; p.add(c1, gbc);
        gbc.gridx=2; gbc.weightx=0; p.add(new JLabel(l2), gbc);
        gbc.gridx=3; gbc.weightx=1; p.add(c2, gbc);
    }
}
