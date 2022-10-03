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
public class PagosModel extends Conexion {

	private Timestamp fecha;
	private float monto;
	private int credito, id;
	public int banderin;
	private String message;
	public boolean validar;

	String consulta;
	public DefaultTableModel tableModel;
	Connection cn;
	ResultSet rs;
	Statement st;
	PreparedStatement pst;
	String[] data;

	public void PagosModel() {

	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public int getCredito() {
		return credito;
	}

	public void setCredito(int credito) {
		this.credito = credito;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void validar() {
		if (this.credito == 0) {
			this.validar = false;
			JOptionPane.showMessageDialog(null, "Seleccione un credito.");
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.consulta = "INSERT INTO pagos(fecha,monto,credito) VALUES(?,?,?)";
			this.message = "Pago registrado con exito.";
			this.cn = conexion();
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setTimestamp(1, fecha);
				this.pst.setFloat(2, monto);
				this.pst.setInt(3, credito);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, this.message);
				}
			} catch (SQLException err) {
				err.printStackTrace();
			}finally{
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(PagosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM pagos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1,this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {				
				this.fecha = this.rs.getTimestamp("fecha");
				this.credito = this.rs.getInt("credito");
				this.monto = this.rs.getFloat("monto");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PagosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.consulta = "UPDATE pagos SET fecha = ?, credito = ?, monto = ? WHERE id = ?";
			this.message = "Pago actualizado con exito.";
			this.cn = conexion();
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setTimestamp(1, fecha);
				this.pst.setInt(2, credito);
				this.pst.setFloat(3, monto);
				this.pst.setInt(4, id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, this.message);
				}
			} catch (SQLException err) {
				err.printStackTrace();
			}finally{
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(PagosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void eliminar() {
		this.cn = conexion();
		this.message = "Pago eliminado con exito.";
		this.consulta = "DELETE FROM pagos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1,this.id);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin > 0){
				JOptionPane.showMessageDialog(null, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PagosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public void mostrarPagos(String value) {
		this.cn = conexion();
		this.consulta = "SELECT p.id,c.nombres,c.apellidos,c.dni,p.fecha,p.monto FROM clientes AS c INNER JOIN creditos AS cr ON(c.id=cr.cliente)"
			+ " INNER JOIN pagos AS p ON(p.credito=cr.id) WHERE CONCAT(c.nombres,c.apellidos,p.fecha) LIKE ? ORDER BY c.nombres ASC";
		String[] title = {
			"ID",
			"CLIENTE",
			"DNI",
			"FECHA",
			"MONTO"
		};
		this.data = new String[5];
		this.tableModel = new DefaultTableModel(null, title) {
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
				this.data[0] = this.rs.getString("id");
				this.data[1] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.data[2] = this.rs.getString("dni");
				this.data[3] = this.rs.getString("fecha");
				this.data[4] = this.rs.getString("monto");
				this.tableModel.addRow(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PagosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
