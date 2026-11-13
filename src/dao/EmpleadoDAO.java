package dao;

import model.Empleado;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY nombre_completo";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Empleado buscarPorId(int codigo) {
        String sql = "SELECT * FROM empleados WHERE codigo_empleado = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean guardar(Empleado e) {
        String sql = "INSERT INTO empleados (nombre_completo, cargo, documento_identidad) VALUES (?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNombreCompleto());
            ps.setString(2, e.getCargo());
            ps.setString(3, e.getDocumentoIdentidad());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean actualizar(Empleado emp) {
        String sql = "UPDATE empleados SET nombre_completo=?, cargo=?, documento_identidad=? WHERE codigo_empleado=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, emp.getNombreCompleto());
            ps.setString(2, emp.getCargo());
            ps.setString(3, emp.getDocumentoIdentidad());
            ps.setInt(4, emp.getCodigoEmpleado());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean eliminar(int codigo) {
        String sql = "DELETE FROM empleados WHERE codigo_empleado = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Empleado mapear(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("codigo_empleado"),
                rs.getString("nombre_completo"),
                rs.getString("cargo"),
                rs.getString("documento_identidad")
        );
    }
}
