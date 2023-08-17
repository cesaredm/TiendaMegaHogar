/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author HOLA
 */
public class PagosPedidosModel extends Conexion{
	private Timestamp fecha;
	private int id,pedido;
	public int idPagoInsertado;
	private float monto;

	String[] datos;
	public boolean validar =  true;
	String consulta;
	public int banderin;
	public DefaultTableModel tableModel;

	Connection cn;
	ResultSet rs;
	PreparedStatement pst;
	
	public PagosPedidosModel(){

	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPedido() {
		return pedido;
	}

	public void setPedido(int pedido) {
		this.pedido = pedido;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public void guardar(){
		this.cn = conexion();
		this.consulta = "INSERT INTO pagospedidos(fecha,pedido,monto) VALUES(?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta,Statement.RETURN_GENERATED_KEYS);
			this.pst.setTimestamp(1, this.fecha);
			this.pst.setInt(2,this.pedido);
			this.pst.setFloat(3, this.monto);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin>0){
				JOptionPane.showMessageDialog(null, "Pago realizado con exito.");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar guardar este pago. "+ e.toString());
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PagosPedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getPagos(int pedido){
		this.consulta = "SELECT *, DATE_FORMAT(fecha,'%d-%m-%Y, %r') AS f FROM pagospedidos WHERE pedido = ?";
		try {
			this.cn = conexion();
			this.pst = this.cn.prepareStatement(consulta);
			this.pst.setInt(1, pedido);
			this.rs = this.pst.executeQuery();
			String[] titles = {"Id","monto", "Fecha"};
			this.datos = new String[3];	
			this.tableModel = new DefaultTableModel(null, titles){
				@Override
				public boolean isCellEditable(int row, int col){
					return false;
				}
			};
			while (this.rs.next()) {				
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("monto");
				this.datos[2] = this.rs.getString("f");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PagosPedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	
	}
}
