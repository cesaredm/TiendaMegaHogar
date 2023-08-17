package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import model.PagosPedidosModel;
import model.PedidosModel;
import view.PrincipalView;

/**
 *
 * @author Cesar Diaz
 */
public class PedidosController implements ActionListener, CaretListener, MouseListener, KeyListener {

	private static PedidosController instancia = null;
	PrincipalView menu;
	PedidosModel pedidosModel;
	PagosPedidosModel pagosPedidosModel;
	int filaseleccionada;
	float cantidad, precio, importe, total;
	int proveedor;
	Date fecha;
	DecimalFormat formato;
	String[] titulos = {"ID", "DESCRIPCION", "PRECIO", "CANTIDAD", "IMPORTE"};
	DefaultTableModel modelo;

	private PedidosController(PrincipalView menu, PedidosModel pedidosModel) {
		this.menu = menu;
		this.pedidosModel = pedidosModel;
		this.pagosPedidosModel = new PagosPedidosModel();
		EstiloTablas.estilosCabeceras(this.menu.tblItemsPedidos);
		EstiloTablas.estilosCabeceras(this.menu.tblPagosPedidos);
		this.formato = new DecimalFormat("##,###,###,###,##0.00");
		this.modelo = new DefaultTableModel(null, this.titulos);
		this.getPedidos("");
		//actionListener
		this.menu.btnAgregarProductoPedido.addActionListener(this);
		this.menu.btnProveedorPedido.addActionListener(this);
		this.menu.btnAgregarProductoApedido.addActionListener(this);
		this.menu.btnGuardarPedido.addActionListener(this);
		this.menu.btnEliminarArtPedido.addActionListener(this);
		this.menu.btnLimpiarPedido.addActionListener(this);
		this.menu.optListarDetallesPedidos.addActionListener(this);
		//caretListener
		this.menu.txtBuscarProveedorPedido.addCaretListener(this);
		this.menu.txtBuscarProductoPedido.addCaretListener(this);
		this.menu.txtBuscarPedido.addCaretListener(this);
		//MouseListener
		this.menu.tblProveedoresPedidos.addMouseListener(this);
		this.menu.tblBuscarProductoPedido.addMouseListener(this);
		this.menu.tblPedidos.addMouseListener(this);
		//KeyListener
		this.menu.tblBuscarProductoPedido.addKeyListener(this);

	}

	public static void createInstanciaController(PrincipalView menu, PedidosModel pedidosModel) {
		if (instancia == null) {
			instancia = new PedidosController(menu, pedidosModel);
		}
	}

	public void getProveedores(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblProveedoresPedidos);
		this.pedidosModel.getProveedores(value);
		this.menu.tblProveedoresPedidos.setModel(this.pedidosModel.tableModel);
	}

	public void getProductos(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblBuscarProductoPedido);
		this.pedidosModel.getProductos(value);
		this.menu.tblBuscarProductoPedido.setModel(this.pedidosModel.tableModel);
	}

	public void getDetallesPedido(){
		this.filaseleccionada = this.menu.tblPedidos.getSelectedRow();
		EstiloTablas.estilosCabeceras(this.menu.tblDetallesPedidos);
		if (this.filaseleccionada > -1) {
			int pedido = Integer.parseInt(this.menu.tblPedidos.getValueAt(this.filaseleccionada, 0).toString());
			this.pedidosModel.getDetallesPedido(pedido);
			this.menu.tblDetallesPedidos.setModel(this.pedidosModel.tableModel);
			this.menu.jdDetallesPedidos.setSize(937, 311);
			this.menu.jdDetallesPedidos.setLocationRelativeTo(null);
			this.menu.jdDetallesPedidos.setVisible(true);
		}
	}

	public void seleccionProducto() {
		this.filaseleccionada = this.menu.tblBuscarProductoPedido.getSelectedRow();
		this.menu.txtIdProductoPediddo.setText(this.menu.tblBuscarProductoPedido.getValueAt(this.filaseleccionada, 0).toString());
		this.menu.txtDescripcionProductoPedido.setText(this.menu.tblBuscarProductoPedido.getValueAt(this.filaseleccionada, 1).toString());
		this.menu.jsPrecioProductoPedidos.setValue(Float.parseFloat(FacturacionController.CleanChars(this.menu.tblBuscarProductoPedido.getValueAt(this.filaseleccionada, 2).toString())));
		this.menu.jsCantidadProductoPedidos.setValue(0.00);
	}

	public void agregarItemPedido() {
		if (Float.parseFloat(this.menu.jsCantidadProductoPedidos.getValue().toString()) > 0) {
			this.cantidad = Float.parseFloat(this.menu.jsCantidadProductoPedidos.getValue().toString());
			this.precio = Float.parseFloat(this.menu.jsPrecioProductoPedidos.getValue().toString());
			this.importe = cantidad * precio;
			this.total += this.importe;
			String[] data = {
				this.menu.txtIdProductoPediddo.getText(),
				this.menu.txtDescripcionProductoPedido.getText(),
				this.formato.format(this.precio),
				this.formato.format(this.cantidad),
				this.formato.format(importe)
			};
			this.modelo.addRow(data);
			this.menu.lblTotalPedido.setText(this.formato.format(this.total));
			this.menu.tblItemsPedidos.setModel(this.modelo);
		} else {
			JOptionPane.showMessageDialog(null, "Ingrese una cantidad mayor a 0", "Advertencia", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void eliminarArtPedido() {
		this.filaseleccionada = this.menu.tblItemsPedidos.getSelectedRow();
		DefaultTableModel modelo = (DefaultTableModel) this.menu.tblItemsPedidos.getModel();
		if (this.filaseleccionada != -1) {
			this.total -= Float.parseFloat(FacturacionController.CleanChars(modelo.getValueAt(this.filaseleccionada, 4).toString()));
			modelo.removeRow(filaseleccionada);
			this.menu.tblItemsPedidos.setModel(modelo);
			this.menu.lblTotalPedido.setText(this.formato.format(this.total));
		}
	}

	public void limpiarTablaPedido() {
		try {
			int filas = this.menu.tblItemsPedidos.getRowCount();
			DefaultTableModel modelo = (DefaultTableModel) this.menu.tblItemsPedidos.getModel();
			for (int i = 0; i < filas; i++) {
				this.modelo.removeRow(0);
			}
			this.total = 0;
			this.menu.lblTotalPedido.setText("0.00");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setDatos(){
		this.proveedor = (this.menu.lblIdProveedor.getText().equals("id")) ? 0 : Integer.parseInt(this.menu.lblIdProveedor.getText());
		this.fecha = (this.menu.jcFechaPedido.getDate() == null) ? new Date() : this.menu.jcFechaPedido.getDate();
	}

	public void guardarPedido() {
		try {
			this.setDatos();
			int filas = this.menu.tblItemsPedidos.getRowCount();
			if (filas > 0) {
				this.pedidosModel.setProveedor(this.proveedor);
				this.pedidosModel.setFecha(new java.sql.Timestamp(this.fecha.getTime()));
				this.pedidosModel.setEstado(this.menu.cmbEstadoPedido.getSelectedItem().toString());
				this.pedidosModel.guardarPedido();
				if (pedidosModel.idInsertado > 0) {
					this.guardarDetallePedido();
					this.limpiarTablaPedido();
				}
			}else{
				JOptionPane.showMessageDialog(null, "El pedido esta vacio.!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void guardarDetallePedido() {
		int filas = this.menu.tblItemsPedidos.getRowCount();
		for (int i = 0; i < filas; i++) {
			this.pedidosModel.setProducto(Integer.parseInt(this.menu.tblItemsPedidos.getValueAt(i, 0).toString()));
			this.pedidosModel.setPrecio(Float.parseFloat(FacturacionController.CleanChars(this.menu.tblItemsPedidos.getValueAt(i, 2).toString())));
			this.pedidosModel.setCantidad(Float.parseFloat(FacturacionController.CleanChars(this.menu.tblItemsPedidos.getValueAt(i, 3).toString())));
			this.pedidosModel.setImporte(Float.parseFloat(FacturacionController.CleanChars(this.menu.tblItemsPedidos.getValueAt(i, 4).toString())));
			this.pedidosModel.guardarDetallePedido();
		}
		JOptionPane.showMessageDialog(null, "Pedido Guardado con exito.");
	}

	public void getPedidos(String value){
		EstiloTablas.estilosCabeceras(this.menu.tblPedidos);
		this.pedidosModel.getPedidos(value);
		this.menu.tblPedidos.setModel(this.pedidosModel.tableModel);
	}

	public void getPagosPedido(int pedido){
		EstiloTablas.estilosCabeceras(this.menu.tblPagosPedidos);
		this.pagosPedidosModel.getPagos(pedido);
		this.menu.tblPagosPedidos.setModel(this.pagosPedidosModel.tableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnProveedorPedido": {
				this.menu.jdProveedorPedido.setSize(676, 272);
				this.menu.jdProveedorPedido.setLocationRelativeTo(null);
				this.menu.jdProveedorPedido.setVisible(true);
			}
			break;
			case "btnAgregarProductoApedido": {
				this.agregarItemPedido();
			}
			break;
			case "btnEliminarArtPedido": {
				this.eliminarArtPedido();
			}
			break;
			case "btnLimpiarPedido": {
				this.limpiarTablaPedido();
			}
			break;
			case "btnGuardarPedido": {
				this.guardarPedido();
				this.getPedidos("");
			}
			break;
			case "optListarDetallesPedidos":{
				this.getDetallesPedido();
			}break;
			default:
				throw new AssertionError();
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProveedorPedido) {
			this.getProveedores(this.menu.txtBuscarProveedorPedido.getText());
		}
		if (e.getSource() == this.menu.txtBuscarProductoPedido) {
			this.getProductos(this.menu.txtBuscarProductoPedido.getText());
		}
		if (e.getSource() == this.menu.txtBuscarPedido) {
			this.getPedidos(this.menu.txtBuscarPedido.getText());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(this.menu.tblProveedoresPedidos)) {
			if (e.getClickCount() == 2) {
				this.filaseleccionada = this.menu.tblProveedoresPedidos.getSelectedRow();
				this.menu.lblNombreProveedor.setText(this.menu.tblProveedoresPedidos.getValueAt(this.filaseleccionada, 1).toString());
				this.menu.lblIdProveedor.setText(this.menu.tblProveedoresPedidos.getValueAt(this.filaseleccionada, 0).toString());
				this.menu.jdProveedorPedido.setVisible(false);
			}
		}
		if (e.getSource().equals(this.menu.tblBuscarProductoPedido)) {
			if (e.getClickCount() == 1) {
				this.seleccionProducto();
			}
		}
		if(e.getSource().equals(this.menu.tblPedidos)){
			if (e.getClickCount() == 2) {
				this.filaseleccionada = this.menu.tblPedidos.getSelectedRow();
				this.pedidosModel.getPagosPorPedido(
					Integer.parseInt(this.menu.tblPedidos.getValueAt(this.filaseleccionada, 0).toString()),""
				);
				this.menu.tblPagosPedidos.setModel(this.pedidosModel.tableModel);
				this.menu.lblIdPedido.setText(this.menu.tblPedidos.getValueAt(this.filaseleccionada, 0).toString());
			}
	
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.menu.tblBuscarProductoPedido && e.getKeyCode() == e.VK_ENTER) {
			this.seleccionProducto();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
