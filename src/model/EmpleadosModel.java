/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class EmpleadosModel extends Conexion {

	private String nombres,
		apellidos,
		telefono;
	private int id, banderin;
	public boolean validar;

	/*--------- VARIABLES DE CONEXION ----------*/
	PreparedStatement pst;
	Connection cn;
	ResultSet rs;
	String consulta;
	String[] datos;
	DefaultTableModel tableModel;

	public EmpleadosModel() {
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void validar() {
		if (this.nombres.equals("")) {
			this.validar = false;
			JOptionPane.showInternalMessageDialog(null, "Complete el campo nombres.", "Advertencia", JOptionPane.WARNING_MESSAGE);
		} else if (this.apellidos.equals("")) {
			this.validar = false;
			JOptionPane.showInternalMessageDialog(null, "Complete el campo apellidos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO empleados(nombres,apellidos,telefono) VALUES(?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, nombres);
				this.pst.setString(2, apellidos);
				this.pst.setString(3, telefono);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Empleado guardado con exito.");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar guardar el empleado.");
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(EmpleadosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM empleados WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombres = this.rs.getString("nombres");
				this.apellidos = this.rs.getString("apellidos");
				this.telefono = this.rs.getString("telefono");
			}
		} catch (Exception e) {
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EmpleadosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE empleados SET nombres=?,apellidos=?,telefono=? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombres);
				this.pst.setString(2, this.apellidos);
				this.pst.setString(3, this.telefono);
				this.pst.setInt(4, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "empleado actualizado con exito.");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar actualizar empleado.");
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(EmpleadosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	public void mostrar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT * FROM empleados WHERE CONCAT(nombres,apellidos) LIKE ?";
		String[] titulos = {"ID", "NOMBRES", "APELLIDOS", "TELEFONO"};
		this.datos = new String[4];
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
				this.datos[1] = this.rs.getString("nombres");
				this.datos[2] = this.rs.getString("apellidos");
				this.datos[3] = this.rs.getString("telefono");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EmpleadosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
