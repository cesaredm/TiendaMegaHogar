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
	public boolean validar;
	public int idInsertado;

	//atributos pedido
	private int id,proveedor;
	private Timestamp fecha;
	private float total;
	private String estado;
	//atributos de detalle de pedido
	private int pedido,producto,detalle;
	private float precio,cantidad,importe;
	
	public PedidosModel(){

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

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
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

	public void guardarPedido(){
		this.cn = conexion();
		this.consulta = "INSERT INTO pedidos(proveedor,fecha,total,estado) VALUES(?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta,Statement.RETURN_GENERATED_KEYS);
			this.pst.setInt(1,this.proveedor);
			this.pst.setTimestamp(2, this.fecha);
			this.pst.setFloat(3, this.total);
			this.pst.setString(4, this.estado);
			if(this.pst.executeUpdate() > 0){
				this.rs = this.pst.getGeneratedKeys();
				if(this.rs.next()){
					this.idInsertado = this.rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(PedidosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void guardarDetallePedido(){
		this.cn = conexion();
		this.consulta = "INSERT INTO productoProveedor(pedido,producto,precio,cantidad,importe) VALUES(?,?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1,this.idInsertado);
			this.pst.setInt(2,this.producto);
			this.pst.setFloat(3,this.precio);
			this.pst.setFloat(4,this.cantidad);
			this.pst.setFloat(5,this.importe);
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
	
	public void getProductos(String value){
		this.cn = conexion();
		this.consulta = "SELECT p.*,m.nombre AS nombreMarca FROM productos AS p INNER JOIN marca AS m ON(p.marca=m.id) WHERE p.descripcion LIKE ?";
		this.datos = new String[4];
		String[] titulos = {"ID","DESCRIPCION","PRECIO COSTO","MARCA"};
		this.tableModel = new DefaultTableModel(null,titulos){
			@Override
			public boolean isCellEditable(int row,int col){
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1,"%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {				
				this.datos[0]=this.rs.getString("id");
				this.datos[1]=this.rs.getString("descripcion");
				this.datos[2]=this.rs.getString("precioCosto");
				this.datos[3]=this.rs.getString("nombreMarca");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
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
