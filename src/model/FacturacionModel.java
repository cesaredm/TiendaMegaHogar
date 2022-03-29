/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class FacturacionModel extends Conexion {

	/* ---------- FACTURA ----------- */
	private Timestamp fecha;
	private int tipoVenta, empleado, credito;
	private float total;
	private String comprador;

	/* ---------- DETALLES ---------- */
	private int factura, comprobante, producto;
	private float precio, cantidad, importe;
	public String[] getProducto;

	public boolean validar;
	public DefaultTableModel tableModel;
	public DefaultComboBoxModel comboModel;

	private String[] datos;
	private String consulta;
	private int banderin;
	private Connection cn;
	private PreparedStatement pst;
	private Statement st;
	private ResultSet rs;
	private DecimalFormat formato;

	public FacturacionModel() {
		this.formato = new DecimalFormat("##,###,###,##0.00");
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public int getTipoVenta() {
		return tipoVenta;
	}

	public void setTipoVenta(int tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	public int getEmpleado() {
		return empleado;
	}

	public void setEmpleado(int empleado) {
		this.empleado = empleado;
	}

	public int getCredito() {
		return credito;
	}

	public void setCredito(int credito) {
		this.credito = credito;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getComprador() {
		return comprador;
	}

	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public int getFactura() {
		return factura;
	}

	public void setFactura(int factura) {
		this.factura = factura;
	}

	public int getComprobante() {
		return comprobante;
	}

	public void setComprobante(int comprobante) {
		this.comprobante = comprobante;
	}

	public int getProducto() {
		return producto;
	}

	public void setProducto(int producto) {
		this.producto = producto;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public void guardarFactura() {
	}

	public void getProductoVender(String codigo) {
		if (Procedures.venderCodigoBarra(codigo, 1)) {
			this.cn = conexion();
			this.consulta = "SELECT * FROM productos AS p"
				+ " WHERE p.codigoBarra = ?";

			this.getProducto = new String[6];
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, codigo);
				this.rs = this.pst.executeQuery();
				while (this.rs.next()) {
					this.getProducto[0] = this.rs.getString("id");
					this.getProducto[1] = this.rs.getString("codigoBarra");
					this.getProducto[2] = "1";
					this.getProducto[3] = this.rs.getString("descripcion");
					this.getProducto[4] = this.rs.getString("precioVenta");
					this.getProducto[5] = this.rs.getString("precioVenta");
				}
			} catch (Exception e) {
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(FacturacionModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} else {
		}

	}

	public void getProductoVender(int producto, float cantidad) {
		if (Procedures.venderId(producto, cantidad)) {
			this.cn = conexion();
			this.consulta = "SELECT * FROM productos AS p"
				+ " WHERE p.id = ?";
			this.getProducto = new String[6];
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setInt(1, producto);
				this.rs = this.pst.executeQuery();
				while (this.rs.next()) {
					this.importe = cantidad * this.rs.getFloat("precioVenta");
					this.getProducto[0] = this.rs.getString("id");
					this.getProducto[1] = this.rs.getString("codigoBarra");
					this.getProducto[2] = this.formato.format(cantidad);
					this.getProducto[3] = this.rs.getString("descripcion");
					this.getProducto[4] = this.rs.getString("precioVenta");
					this.getProducto[5] = this.formato.format(this.importe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.importe = 0;
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(FacturacionModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} else {
		}
	}

	public void getProductosVender(String value) {
		this.cn = conexion();
		this.consulta = "SELECT p.id,descripcion,precioVenta,stock,m.nombre FROM productos AS p INNER JOIN marca AS m ON(p.marca=m.id)"
			+ " WHERE CONCAT(p.descripcion,p.codigoBarra,m.nombre) LIKE ? LIMIT 30";
		String[] titulos = {"ID", "DESCRIPCION", "MARCA", "PRECIO VENTA", "INVENTARIO"};
		this.datos = new String[5];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1) {
					return true;
				}
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
				this.datos[2] = this.rs.getString("nombre");
				this.datos[3] = this.rs.getString("precioVenta");
				this.datos[4] = this.rs.getString("stock");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(FacturacionModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrarCreditos(String value) {
		this.cn = conexion();
		this.consulta = "SELECT cr.id, c.nombres,c.apellidos FROM creditos AS cr INNER JOIN clientes AS c ON(cr.cliente=c.id)"
			+ " WHERE CONCAT(c.nombres,c.apellidos,cr.id) LIKE ? ORDER BY id DESC LIMIT 30";
		String[] titulos = {"ID CREDITO", "CLIENTE"};
		this.datos = new String[2];
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
				this.datos[1] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(FacturacionModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void agregarInventario(int producto, float cantidad) {
		Procedures.agregarInventario(producto, cantidad);
	}
}
