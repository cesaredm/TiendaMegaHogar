/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.MenuController;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class KardexModel extends Conexion {

	private int producto, empleado;
	private float cantidad, inventarioInicial, salidas, entradas;
	private String nota, consulta;
	private String tipoMovimiento;

	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	private Connection cn;
	String[] datos;

	public DefaultTableModel tableModel;

	public KardexModel() {

	}

	public int getProducto() {
		return producto;
	}

	public void setProducto(int producto) {
		this.producto = producto;
	}

	public int getEmpleado() {
		return empleado;
	}

	public void setEmpleado(int empleado) {
		this.empleado = empleado;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public void setTipoMovimiento(String tipoMovimietoK) {
		this.tipoMovimiento = tipoMovimietoK;
	}

	public void setFecha(String nota) {
		this.nota= nota;
	}

	public float getInventarioInicial() {
		return inventarioInicial;
	}

	public void setInventarioInicial(float inventarioInicial) {
		this.inventarioInicial = inventarioInicial;
	}

	public float getEntradas() {
		return entradas;
	}

	public void setEntradas(float entradas) {
		this.entradas = entradas;
	}

	public float getSalidas() {
		return salidas;
	}

	public void setSalidas(float salidas) {
		this.salidas = salidas;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
	

	public void guardar() {
		this.cn = conexion();
		this.consulta = "call crearMovimientoKardex(?,?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.producto);
			this.pst.setFloat(2, this.cantidad);
			this.pst.setString(3, this.tipoMovimiento);
			this.pst.setString(4, this.nota);
			this.pst.setInt(5, MenuController.empleadoSistema);
			this.rs = this.pst.executeQuery();
			if (this.rs.next()) {
				if (this.rs.getString(1).equals("exito")) {
					JOptionPane.showMessageDialog(null, "Movimiento creado con exito.");
				}else{
					JOptionPane.showMessageDialog(null, "Oops. ocurrio un error al intentar crear el movimiento");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(KardexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public int getLastProducto() {
		int id = 0;
		this.cn = conexion();
		this.consulta = "SELECT MAX(id) AS id FROM productos";
		try {
			this.st = this.cn.createStatement();
			this.rs = this.st.executeQuery(this.consulta);
			while (this.rs.next()) {
				id = this.rs.getInt("id");
			}
		} catch (Exception e) {
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(KardexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return id;
	}

	public void getSumaMovimientosKardex(int producto) {
		this.cn = conexion();
		this.consulta = "CALL kardex(?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, producto);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.entradas = this.rs.getFloat("entradas");
				this.salidas = this.rs.getFloat("salidas");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(KardexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void salidas(int producto) {
		this.cn = conexion();
		this.consulta = "SELECT k.cantidad,k.tipoMovimiento,DATE_FORMAT(k.fecha,'%d-%M-%y %r') AS fecha,k.nota, e.nombres,e.apellidos"
			+ " FROM kardex AS k INNER JOIN empleados AS e ON(k.empleado = e.id) WHERE k.producto = ? AND k.tipoMovimiento = ? ";
		this.datos = new String[5];
		String[] titulos = {"CANTIDAD","TIPO MOV.", "NOTA", "FECHA","EMPLEADO"};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1,producto);
			this.pst.setString(2, "Salida");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("cantidad");
				this.datos[1] = this.rs.getString("tipoMovimiento");
				this.datos[2] = this.rs.getString("nota");
				this.datos[3] = this.rs.getString("fecha");
				this.datos[4] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(KardexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getEntradas(int producto) {
		this.cn = conexion();
		this.consulta = "SELECT k.cantidad,k.tipoMovimiento,DATE_FORMAT(k.fecha,'%d-%M-%y %r') AS fecha,k.nota, e.nombres, e.apellidos "
			+ "FROM kardex AS k INNER JOIN empleados e ON(k.empleado=e.id) WHERE k.producto = ? AND k.tipoMovimiento = ?";
		this.datos = new String[5];
		String[] titulos = {"CANTIDAD", "TIPO MOV.", "NOTA", "FECHA", "EMPLEADO"};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.st = this.cn.createStatement();
			this.st.execute("SET lc_time_names = 'es_ES'");
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, producto);
			this.pst.setString(2, "Entrada");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("cantidad");
				this.datos[1] = this.rs.getString("tipoMovimiento");
				this.datos[2] = this.rs.getString("nota");
				this.datos[3] = this.rs.getString("fecha");
				this.datos[4] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(KardexModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
