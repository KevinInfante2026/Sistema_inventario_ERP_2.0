package ui;

import dao.EmpleadoDAO;
import model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmpleadosFrame extends JFrame {

    private final EmpleadoDAO dao = new EmpleadoDAO();

    private JTextField txtCodigo, txtNombre, txtCargo, txtDocumento;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public EmpleadosFrame() {
        setTitle("Módulo de Empleados");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        panelForm.setBackground(new Color(250, 245, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtCodigo    = campo(); txtCodigo.setEditable(false); txtCodigo.setBackground(new Color(220,220,220));
        txtNombre    = campo();
        txtCargo     = campo();
        txtDocumento = campo();

        gbc.gridy=0; addPar(panelForm,gbc,"Código (auto):", txtCodigo,  "Nombre completo:", txtNombre);
        gbc.gridy=1; addPar(panelForm,gbc,"Cargo:",          txtCargo,  "Doc. Identidad:",  txtDocumento);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelBotones.setBackground(new Color(250, 245, 255));

        JButton btnGuardar    = boton("Guardar",    new Color(155, 89, 182));
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

        String[] cols = {"Código", "Nombre Completo", "Cargo", "Documento Identidad"};
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
        if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
        Empleado em = new Empleado(0, txtNombre.getText().trim(), txtCargo.getText().trim(), txtDocumento.getText().trim());
        if (dao.guardar(em)) { JOptionPane.showMessageDialog(this, "✅ Empleado guardado."); limpiar(); cargarTabla(); }
    }

    private void actualizar() {
        if (txtCodigo.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un empleado."); return; }
        if (txtNombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
        Empleado em = new Empleado(Integer.parseInt(txtCodigo.getText()),
                txtNombre.getText().trim(), txtCargo.getText().trim(), txtDocumento.getText().trim());
        if (dao.actualizar(em)) { JOptionPane.showMessageDialog(this, "✅ Empleado actualizado."); limpiar(); cargarTabla(); }
    }

    private void eliminar() {
        if (txtCodigo.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un empleado."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar empleado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dao.eliminar(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "✅ Eliminado."); limpiar(); cargarTabla();
        }
    }

    private void limpiar() {
        txtCodigo.setText(""); txtNombre.setText(""); txtCargo.setText(""); txtDocumento.setText("");
        tabla.clearSelection();
    }

    private void cargarEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        txtCodigo.setText(modeloTabla.getValueAt(row,0).toString());
        txtNombre.setText(modeloTabla.getValueAt(row,1).toString());
        txtCargo.setText(modeloTabla.getValueAt(row,2).toString());
        txtDocumento.setText(modeloTabla.getValueAt(row,3).toString());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Empleado em : dao.listarTodos()) {
            modeloTabla.addRow(new Object[]{ em.getCodigoEmpleado(), em.getNombreCompleto(), em.getCargo(), em.getDocumentoIdentidad() });
        }
    }

    private JTextField campo() {
        JTextField f = new JTextField(18); f.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return f;
    }
    private JButton boton(String t, Color c) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    private void addPar(JPanel p, GridBagConstraints gbc, String l1, JComponent c1, String l2, JComponent c2) {
        gbc.gridx=0; gbc.weightx=0; p.add(new JLabel(l1), gbc);
        gbc.gridx=1; gbc.weightx=1; p.add(c1, gbc);
        gbc.gridx=2; gbc.weightx=0; p.add(new JLabel(l2), gbc);
        gbc.gridx=3; gbc.weightx=1; p.add(c2, gbc);
    }
}
