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
public class ProveedoresModel extends Conexion {

	private int id, banderin;
	private String nombre, telefono, cuentaBancaria, vendedor, telefonoVendedor, message;
	String[] datos;

	private String consulta;
	public boolean validar;
	public DefaultTableModel tableModel;
	PreparedStatement pst;
	Connection cn;
	ResultSet rs;

	public ProveedoresModel() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(String cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getTelefonoVendedor() {
		return telefonoVendedor;
	}

	public void setTelefonoVendedor(String telefonoVendedor) {
		this.telefonoVendedor = telefonoVendedor;
	}

	public boolean isValidar() {
		return validar;
	}

	public void setValidar(boolean validar) {
		this.validar = validar;
	}

	private void validar() {
		if (this.nombre.equals("")) {
			this.validar = false;
			this.message = "Complete el nombre de proveedor.";
		} else if (this.telefono.equals("")) {
			this.validar = false;
			this.message = "Complete el numero de telefono.";
		} else if (this.vendedor.equals("")) {
			this.validar = false;
			this.message = "Complete el nombre de vendedor.";
		} else {
			this.validar = !this.telefonoVendedor.equals("");
		}
		JOptionPane.showMessageDialog(null, message);
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO proveedores(nombre,telefono,cuentaBancaria,vendedor,telefonoVendedor) VALUES(?,?,?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, nombre);
				this.pst.setString(2, telefono);
				this.pst.setString(3, cuentaBancaria);
				this.pst.setString(4, vendedor);
				this.pst.setString(5, telefonoVendedor);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Proveedor guardado con exito.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ProveedoresModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM proveedores WHERE id + ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombre = this.rs.getString("nombre");
				this.telefono = this.rs.getString("telefono");
				this.cuentaBancaria = this.rs.getString("cuentaBancaria");
				this.vendedor = this.rs.getString("vendedor");
				this.telefonoVendedor = this.rs.getString("telefonoVendedor");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProveedoresModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE proveedores SET nombre=?,telefono=?,cuentaBancaria=?,vendedor=?,telefonoVendedor=? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, nombre);
				this.pst.setString(2, telefono);
				this.pst.setString(3, cuentaBancaria);
				this.pst.setString(4, vendedor);
				this.pst.setString(5, telefonoVendedor);
				this.pst.setInt(6, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Proveedor guardado con exito.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ProveedoresModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void eliminar() {
		int confirmar = JOptionPane.showConfirmDialog(
			null,
			"Seguro que quieres eliminar de forma permanente este proveedor.?",
			"Advertencia",
			JOptionPane.YES_NO_OPTION
		);
		if (confirmar == JOptionPane.YES_OPTION) {
			this.cn = conexion();
			this.consulta = "DELETE FROM proveedores WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setInt(1, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito..");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void getProveedores(String value) {
		this.cn = conexion();
		this.consulta = "SELECT * FROM proveedores WHERE CONCAT(nombre,vendedor) LIKE ? ORDER BY id DESC";
		this.datos = new String[6];
		String[] titulos = {"ID", "NOMBRE", "TELEFONO", "CUENTA BANCARIA", "VENDEDOR", "TEL. VENDEDOR"};
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
				this.datos[2] = this.rs.getString("telefono");
				this.datos[3] = this.rs.getString("cuentaBancaria");
				this.datos[4] = this.rs.getString("vendedor");
				this.datos[5] = this.rs.getString("telefonoVendedor");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProveedoresModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
}
