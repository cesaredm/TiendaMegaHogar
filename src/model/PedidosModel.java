package model;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author CESAR DIAZ 
 */
public class PedidosModel extends Conexion{
	Connection cn;
	PreparedStatement pst;
	ResultSet rs;
	Statement st;	
	public DefaultTableModel tableModel;
	String consulta;
	String[] datos;
	
	public PedidosModel(){

	}

	public void getProveedores(String value){
		this.cn = conexion();
		this.consulta = "SELECT * FROM proveedores WHERE nombre LIKE ?";
		String[] titulos = {"ID","PROVEEDOR","VENDEDOR"};
		this.datos = new String[3];
		this.tableModel = new DefaultTableModel(null,titulos){
			@Override 
			public boolean isCellEditable(int row, int col){
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
			JOptionPane.showMessageDialog(null,e);
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
