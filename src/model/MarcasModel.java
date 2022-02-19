package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class MarcasModel extends Conexion {

	private int id, banderin;
	private String nombre, consulta;
	public boolean validar;

	String[] datos;
	public DefaultTableModel tableModel;
	public DefaultComboBoxModel comboModel;
	private Connection cn;
	private ResultSet rs;
	private PreparedStatement pst;
	private Statement st;

	public MarcasModel() {

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

	public void validar() {
		if (this.nombre.equals("")) {
			this.validar = false;
			JOptionPane.showMessageDialog(null, "Complete el campo nombre.");
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO marca(nombre) VALUES(?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombre);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Marca guardada con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar guardar marca -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(MarcasModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM marca WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombre = this.rs.getString("nombre");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar editar marca.");
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(MarcasModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE marca SET nombre = ? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombre);
				this.pst.setInt(2, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Marca actualizada con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar actualizar marca -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(MarcasModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void mostar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT * FROM marca WHERE nombre LIKE ? ORDER BY id DESC LIMIT 15";
		String[] titulos = {"ID", "NOMBRE"};
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
				this.datos[1] = this.rs.getString("nombre");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(MarcasModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public void getMarcas() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM marca ORDER BY nombre ASC";
		this.comboModel = new DefaultComboBoxModel();
		try {
			this.st = this.cn.createStatement();
			this.rs = this.st.executeQuery(this.consulta);
			while (this.rs.next()) {
				this.comboModel.addElement(new CmbMarcas(this.rs.getInt("id"), this.rs.getString("nombre")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
