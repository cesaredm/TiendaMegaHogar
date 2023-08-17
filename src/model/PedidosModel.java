package model;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ
 */
public class PedidosModel extends Conexion {

	Connection cn;
	PreparedStatement pst;
	ResultSet rs;
	Statement st;
	public DefaultTableModel tableModel;
	String consulta;
	String[] datos;
	public boolean validar = true;
	public int idInsertado;
	DecimalFormat formato;

	//atributos pedido
	private int id, proveedor;
	private Timestamp fecha;
	private String estado;
	//atributos de detalle de pedido
	private int pedido, producto, detalle;
	private float precio, cantidad, importe;

	public PedidosModel() {
		this.formato = new DecimalFormat("###,###,###,##0.00");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProveedor() {
		return proveedor;
	}

	public void setProveedor(int proveedor) {
		this.proveedor = proveedor;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getPedido() {
		return pedido;
	}

	public void setPedido(int pedido) {
		this.pedido = pedido;
	}

	public int getProducto() {
		return producto;
	}

	public void setProducto(int producto) {
		this.producto = producto;
	}

	public int getDetalle() {
		return detalle;
	}

	public void setDetalle(int detalle) {
		this.detalle = detalle;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public void validarPedido() {
		if (this.proveedor == 0) {
			this.validar = false;
			JOptionPane.showMessageDialog(null, "Seleccione un proveedor.");
		} else {
			this.validar = true;
		}
	}

	public void guardarPedido() {
		this.validarPedido();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO pedidos(proveedor,fecha,estado) VALUES(?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta, Statement.RETURN_GENERATED_KEYS);
				this.pst.setInt(1, this.proveedor);
				this.pst.setTimestamp(2, this.fecha);
				this.pst.setString(3, this.estado);
				if (this.pst.executeUpdate() > 0) {
					this.rs = this.pst.getGeneratedKeys();
					if (this.rs.next()) {
						this.idInsertado = this.rs.getInt(1);
					}
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void guardarDetallePedido() {
		this.cn = conexion();
		this.consulta = "INSERT INTO productoProveedor(pedido,producto,precio,cantidad,importe) VALUES(?,?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.idInsertado);
			this.pst.setInt(2, this.producto);
			this.pst.setFloat(3, this.precio);
			this.pst.setFloat(4, this.cantidad);
			this.pst.setFloat(5, this.importe);
			this.pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getDetallesPedido(int id){
		this.consulta = "SELECT pp.id, pp.pedido, pp.producto, pr.descripcion, pp.cantidad,pp.precio, pp.importe FROM productoproveedor AS pp"
			+ " INNER JOIN productos AS pr ON(pp.producto=pr.id) WHERE pp.pedido = ?";
		this.cn = conexion();
		String[] titulos = {"N. Detalle", "DESCRIPCION", "CANTIDAD", "PRECIO", "IMPORTE"};
		this.datos = new String[5];
		this.tableModel = new DefaultTableModel(null, titulos){
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {				
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("descripcion");
				this.datos[2] = this.rs.getString("cantidad");
				this.datos[3] = this.rs.getString("precio");
				this.datos[4] = this.rs.getString("importe");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getProveedores(String value) {
		this.cn = conexion();
		this.consulta = "SELECT * FROM proveedores WHERE nombre LIKE ?";
		String[] titulos = {"ID", "PROVEEDOR", "VENDEDOR"};
		this.datos = new String[3];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombre");
				this.datos[2] = this.rs.getString("vendedor");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getProductos(String value) {
		this.cn = conexion();
		this.consulta = "SELECT p.*,m.nombre AS nombreMarca FROM productos AS p INNER JOIN marca AS m ON(p.marca=m.id) WHERE p.descripcion LIKE ?";
		this.datos = new String[4];
		String[] titulos = {"ID", "DESCRIPCION", "PRECIO COSTO", "MARCA"};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("descripcion");
				this.datos[2] = this.formato.format(this.rs.getFloat("precioCosto"));
				this.datos[3] = this.rs.getString("nombreMarca");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getPedidos(String value) {
		this.cn = conexion();
		this.consulta = "SELECT pt.*, (total - (SELECT IFNULL(SUM(monto),0) FROM pagospedidos WHERE pedido=pt.id)) AS saldo FROM"
			+ " pedidostienda AS pt WHERE CONCAT(pt.id, pt.nombre) LIKE ? ORDER BY nombre DESC";
		this.datos = new String[4];
		String[] titulos = {
			"ID",
			"PROVEEDOR",
			"TOTAL",
			"ESTADO"
		};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%"+value+"%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombre");
				this.datos[2] = this.formato.format(this.rs.getFloat("saldo"));
				this.datos[3] = this.rs.getString("estado");
				this.tableModel.addRow(this.datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getPagosPorPedido(int id, String value) {
		this.cn = conexion();
		if (id == 0) {
			this.consulta = "SELECT pp.*, DATE_FORMAT(pp.fecha,'%d-%m-%Y, %r') AS f,pr.nombre FROM pagospedidos AS pp INNER JOIN pedidos AS p ON(pp.pedido=p.id) INNER JOIN proveedores AS pr ON(p.proveedor=pr.id) WHERE CONCAT(pr.nombre, DATE(pp.fecha)) LIKE ?";
		} else if(id>0) {
			this.consulta = "SELECT pp.*, DATE_FORMAT(pp.fecha,'%d-%m-%Y, %r') AS f,pr.nombre FROM pagospedidos AS pp INNER JOIN pedidos AS p ON(pp.pedido=p.id) INNER JOIN proveedores AS pr ON(p.proveedor=pr.id) WHERE p.id = ?";
		}

		this.datos = new String[4];
		String[] titulos = {
			"ID",
			"FECHA",
			"MONTO",
			"PROVEEDOR"
		};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			if (id > 0) {
				this.pst.setInt(1, id);
			} else {
				this.pst.setString(1, "%"+value+"%");
			} 
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("f");
				this.datos[2] = this.rs.getString("monto");
				this.datos[3] = this.rs.getString("nombre");
				this.tableModel.addRow(this.datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
