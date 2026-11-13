package dao;

import model.DetalleFactura;
import model.Factura;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public int guardar(Factura f) {
        String sql = "INSERT INTO facturas (fecha, id_cajero, nombre_cajero, cliente_id_nit, forma_pago, " +
                "domicilio, descuento, total, dinero_recibido, vueltos, abono_saldo, fianza, saldo_nuevo) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getFecha());
            ps.setInt(2, f.getIdCajero());
            ps.setString(3, f.getNombreCajero());
            ps.setString(4, f.getClienteIdNit());
            ps.setString(5, f.getFormaPago());
            ps.setDouble(6, f.getDomicilio());
            ps.setDouble(7, f.getDescuento());
            ps.setDouble(8, f.getTotal());
            ps.setDouble(9, f.getDineroRecibido());
            ps.setDouble(10, f.getVueltos());
            ps.setDouble(11, f.getAbonoSaldo());
            ps.setDouble(12, f.getFianza());
            ps.setDouble(13, f.getSaldoNuevo());
            ps.executeUpdate();
            ResultSet gk = ps.getGeneratedKeys();
            if (gk.next()) {
                int id = gk.getInt(1);
                f.setIdFactura(id);
                guardarDetalles(c, id, f.getDetalles());
                return id;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    private void guardarDetalles(Connection c, int idFactura, List<DetalleFactura> detalles) throws SQLException {
        String sql = "INSERT INTO detalle_factura (id_factura, id_producto, descripcion_articulo, cantidad, valor_unitario, importe) VALUES (?,?,?,?,?,?)";
        for (DetalleFactura d : detalles) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, idFactura);
                ps.setInt(2, d.getIdProducto());
                ps.setString(3, d.getDescripcionArticulo());
                ps.setInt(4, d.getCantidad());
                ps.setDouble(5, d.getValorUnitario());
                ps.setDouble(6, d.getImporte());
                ps.executeUpdate();
            }
        }
    }

    public List<Factura> listarTodas() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY id_factura DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<DetalleFactura> obtenerDetalles(int idFactura) {
        List<DetalleFactura> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_factura WHERE id_factura = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetalleFactura d = new DetalleFactura();
                d.setIdDetalle(rs.getInt("id_detalle"));
                d.setIdFactura(rs.getInt("id_factura"));
                d.setIdProducto(rs.getInt("id_producto"));
                d.setDescripcionArticulo(rs.getString("descripcion_articulo"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setValorUnitario(rs.getDouble("valor_unitario"));
                d.setImporte(rs.getDouble("importe"));
                lista.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Factura mapear(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setIdFactura(rs.getInt("id_factura"));
        f.setFecha(rs.getString("fecha"));
        f.setIdCajero(rs.getInt("id_cajero"));
        f.setNombreCajero(rs.getString("nombre_cajero"));
        f.setClienteIdNit(rs.getString("cliente_id_nit"));
        f.setFormaPago(rs.getString("forma_pago"));
        f.setDomicilio(rs.getDouble("domicilio"));
        f.setDescuento(rs.getDouble("descuento"));
        f.setTotal(rs.getDouble("total"));
        f.setDineroRecibido(rs.getDouble("dinero_recibido"));
        f.setVueltos(rs.getDouble("vueltos"));
        f.setAbonoSaldo(rs.getDouble("abono_saldo"));
        f.setFianza(rs.getDouble("fianza"));
        f.setSaldoNuevo(rs.getDouble("saldo_nuevo"));
        return f;
    }
}
