package ui;

import dao.VentaDiariaDAO;
import model.VentaDiaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasDiariasFrame extends JFrame {

    private final VentaDiariaDAO dao = new VentaDiariaDAO();
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtFechaBuscar;
    private JLabel lblTotalVentas;

    public VentasDiariasFrame() {
        setTitle("Ventas Diarias");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construirUI();
        cargarTodos();
    }

    private void construirUI() {
        setLayout(new BorderLayout(5, 5));

        // Barra de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtro por Fecha"));
        txtFechaBuscar = new JTextField(12);
        txtFechaBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFechaBuscar.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        JButton btnFiltrar = boton("🔍 Filtrar", new Color(52, 152, 219));
        JButton btnTodos   = boton("📋 Ver Todos", new Color(46, 204, 113));

        panelFiltro.add(new JLabel("Fecha (yyyy-MM-dd):"));
        panelFiltro.add(txtFechaBuscar);
        panelFiltro.add(btnFiltrar);
        panelFiltro.add(btnTodos);

        // Totales
        lblTotalVentas = new JLabel("  Total vendido: $ 0.00");
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalVentas.setForeground(new Color(0, 128, 0));
        panelFiltro.add(Box.createHorizontalStrut(30));
        panelFiltro.add(lblTotalVentas);

        // Tabla
        String[] cols = {"ID", "Fecha", "ID Producto", "Nombre Producto", "Cantidad Vendida", "Precio Unitario", "Total"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(23);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(panelFiltro, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnFiltrar.addActionListener(e -> {
            String fecha = txtFechaBuscar.getText().trim();
            if (fecha.isEmpty()) { cargarTodos(); return; }
            cargar(dao.listarPorFecha(fecha));
        });
        btnTodos.addActionListener(e -> cargarTodos());
    }

    private void cargarTodos() { cargar(dao.listarTodos()); }

    private void cargar(List<VentaDiaria> lista) {
        modeloTabla.setRowCount(0);
        double total = 0;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es","CO"));
        for (VentaDiaria v : lista) {
            double subtotal = v.getCantidadVendida() * v.getPrecioUnitario();
            total += subtotal;
            modeloTabla.addRow(new Object[]{
                v.getId(), v.getFecha(), v.getIdProducto(), v.getNombre(),
                v.getCantidadVendida(), v.getPrecioUnitario(), subtotal
            });
        }
        lblTotalVentas.setText("  Total vendido: " + nf.format(total));
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
}
