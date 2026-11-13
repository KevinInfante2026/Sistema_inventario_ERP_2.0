package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Inventario - María Camila");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        construirUI();
    }

    private void construirUI() {
        // Panel principal con fondo degradado
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 60, 114),
                        getWidth(), getHeight(), new Color(42, 82, 152));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("SISTEMA DE INVENTARIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel lblSub = new JLabel("Gestión Empresarial", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(200, 220, 255));
        lblSub.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelTitulos = new JPanel(new BorderLayout());
        panelTitulos.setOpaque(false);
        panelTitulos.add(lblTitulo, BorderLayout.CENTER);
        panelTitulos.add(lblSub, BorderLayout.SOUTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 15, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));

        panelBotones.add(crearBoton("📦  INVENTARIO", new Color(52, 152, 219), e -> abrirInventario()));
        panelBotones.add(crearBoton("👥  CLIENTES", new Color(46, 204, 113), e -> abrirClientes()));
        panelBotones.add(crearBoton("👤  EMPLEADOS", new Color(155, 89, 182), e -> abrirEmpleados()));
        panelBotones.add(crearBoton("🧾  FACTURACIÓN", new Color(230, 126, 34), e -> abrirFacturacion()));
        panelBotones.add(crearBoton("📊  VENTAS DIARIAS", new Color(231, 76, 60), e -> abrirVentasDiarias()));
        panelBotones.add(crearBoton("💰  CUENTAS", new Color(26, 188, 156), e -> abrirCuentas()));

        panelFondo.add(panelTitulos, BorderLayout.NORTH);
        panelFondo.add(panelBotones, BorderLayout.CENTER);

        setContentPane(panelFondo);
    }

    private JButton crearBoton(String texto, Color color, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private void abrirInventario()     { new InventarioFrame().setVisible(true); }
    private void abrirClientes()       { new ClientesFrame().setVisible(true); }
    private void abrirEmpleados()      { new EmpleadosFrame().setVisible(true); }
    private void abrirFacturacion()    { new FacturacionFrame().setVisible(true); }
    private void abrirVentasDiarias()  { new VentasDiariasFrame().setVisible(true); }
    private void abrirCuentas()        { new CuentasFrame().setVisible(true); }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
