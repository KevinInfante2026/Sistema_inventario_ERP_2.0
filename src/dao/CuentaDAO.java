package dao;

import model.Cuenta;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public boolean guardar(Cuenta cu) {
        String sql = "INSERT INTO cuentas (fecha, cliente_id, metodo_pago, total_venta, fianza, empleado, tipo) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cu.getFecha());
            ps.setString(2, cu.getClienteId());
            ps.setString(3, cu.getMetodoPago());
            ps.setDouble(4, cu.getTotalVenta());
            ps.setDouble(5, cu.getFianza());
            ps.setString(6, cu.getEmpleado());
            ps.setString(7, cu.getTipo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Cuenta> listarTodas() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM cuentas ORDER BY fecha DESC, id DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public double totalVentas() {
        String sql = "SELECT COALESCE(SUM(total_venta),0) FROM cuentas WHERE tipo='INGRESO'";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public double totalDeuda() {
        String sql = "SELECT COALESCE(SUM(total_venta),0) FROM cuentas WHERE tipo='EGRESO'";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private Cuenta mapear(ResultSet rs) throws SQLException {
        Cuenta cu = new Cuenta();
        cu.setId(rs.getInt("id"));
        cu.setFecha(rs.getString("fecha"));
        cu.setClienteId(rs.getString("cliente_id"));
        cu.setMetodoPago(rs.getString("metodo_pago"));
        cu.setTotalVenta(rs.getDouble("total_venta"));
        cu.setFianza(rs.getDouble("fianza"));
        cu.setEmpleado(rs.getString("empleado"));
        cu.setTipo(rs.getString("tipo"));
        return cu;
    }
}
