package controller;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import view.PrincipalView;
import model.FacturacionModel;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import model.CmbTipoVenta;
import model.Procedures;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class FacturacionController implements KeyListener, CaretListener, MouseListener, ActionListener, ItemListener {

	private static FacturacionController instancia = null;
	PrincipalView menu;
	FacturacionModel facturacionModel;
	CmbTipoVenta tiposVenta;
	DefaultTableModel modelo;
	String codigoBarra;
	DecimalFormat formato;
	int filaseleccionada, producto, filas;
	float cantidad;
	SpinnerNumberModel spinnerModel;
	JSpinner spinner;

	//variables de detalles
	float importe, precio;

	private FacturacionController(PrincipalView menu, FacturacionModel facturacionModel) {
		this.menu = menu;
		this.facturacionModel = facturacionModel;
		this.updateNumberComprobante();
		this.updateNumberDato();
		this.formato = new DecimalFormat("##,###,###,##0.00");
		start();
		this.createJSpinner();
		this.showTiposVentas();
		this.modelo = (DefaultTableModel) this.menu.tblFacturacion.getModel();
		this.menu.btnLimpiarFacturacion.addActionListener(this);
		this.menu.btnAgregarProductoFacturacion.addActionListener(this);
		this.menu.btnBuscarArticuloFacturacion.addActionListener(this);
		this.menu.btnEliminarArticuloFacturacion.addActionListener(this);
		this.menu.btnAgregarCreditoFacturacion.addActionListener(this);
		this.menu.btnGuardarFacturacion.addActionListener(this);
		this.menu.btnGuardarImprimirFacturacion.addActionListener(this);
		this.menu.txtCodigoBarraFacturacion.addKeyListener(this);
		this.menu.btnBuscarArticuloFacturacion.addKeyListener(this);
		this.menu.tblProductosFacturacion.addKeyListener(this);
		this.menu.tblCreditosFacturacion.addKeyListener(this);
		this.menu.txtBuscarProductoFacturacion.addCaretListener(this);
		this.menu.txtBuscarCreditoFacturacion.addCaretListener(this);
		this.menu.tblProductosFacturacion.addMouseListener(this);
		this.menu.tblCreditosFacturacion.addMouseListener(this);
		this.menu.cmbTipoComprobante.addItemListener(this);
	}

	public void createJSpinner() {
		this.spinnerModel = new SpinnerNumberModel();
		this.spinnerModel.setMinimum(0);
		this.spinnerModel.setStepSize(0.01);
		this.spinner = new JSpinner(spinnerModel);
	}

	public static void createInstanceController(PrincipalView menu, FacturacionModel facturacionModel) {
		if (instancia == null) {
			instancia = new FacturacionController(menu, facturacionModel);
		}
	}

	public void showTiposVentas() {
		this.facturacionModel.getTiposVenta();
		this.menu.cmbFormaPagoFacturacion.setModel(this.facturacionModel.comboModel);
	}

	public void start() {
		this.menu.lblEmpleadoFacturacion.setVisible(false);
		this.menu.lblIdCreditoFacturacion.setVisible(false);
		this.menu.btnGuardarFacturacion.setEnabled(false);
		this.menu.btnGuardarImprimirFacturacion.setEnabled(false);
		this.menu.jcFechaFactura.setDate(new Date());
		EstiloTablas.estilosCabeceras(this.menu.tblFacturacion);
		this.menu.txtCodigoBarraFacturacion.requestFocus();
	}

	public static String CleanChars(String cadena) {
		StringBuilder myString = new StringBuilder(cadena);
		char[] signos = {','};
		for (int i = 0; i < cadena.length(); i++) {
			char chr = cadena.charAt(i);

			for (char signo : signos) {
				if (chr == signo) {
					myString = myString.deleteCharAt(i);
				}
			}
		}

		return myString.toString();
	}

	public void setDatosSave() {
		this.tiposVenta = (CmbTipoVenta) this.menu.cmbFormaPagoFacturacion.getSelectedItem();
		this.menu.jcFechaFactura.setDate(new Date());
		this.facturacionModel.setFecha(
			new java.sql.Timestamp(this.menu.jcFechaFactura.getDate().getTime())
		);
		this.facturacionModel.setTipoVenta(this.tiposVenta.getId());
		this.facturacionModel.setEmpleado(MenuController.empleadoSistema);
		this.facturacionModel.setCredito(Integer.parseInt(this.menu.lblIdCreditoFacturacion.getText()));
		//this.facturacionModel.setTotal(Float.parseFloat(this.CleanChars(this.menu.lblTotalFactura.getText())));
		this.facturacionModel.setComprador(this.menu.txtCompradorFacturacion.getText());
		if (this.menu.cmbTipoComprobante.getSelectedItem().toString().equals("Factura")) {
			this.facturacionModel.setHelper(0);
		} else {
			this.facturacionModel.setHelper(1);
		}
		this.guardarDetalle();
	}

	public void guardarFactura() {
		if (this.menu.btnGuardarFacturacion.isEnabled()) {
			this.setDatosSave();
			this.facturacionModel.guardarFactura();
			//JOptionPane.showMessageDialog(null, this.facturacionModel.mensaje);
			Procedures.cambiarEstadoCredito(this.facturacionModel.getCredito());
			if (Procedures.response) {
				this.updateNumberComprobante();
				this.updateNumberDato();
				this.limpiar();
				this.mostrarProductosVender("");
			}
		} else {
			JOptionPane.showMessageDialog(null, "La factura esta vacia.");
		}

	}

	public void updateNumberFactura() {
		this.menu.txtNumeroFactura.setText(
			"" + this.facturacionModel.updateNumberFactura()
		);
	}

	public void updateNumberComprobante() {
		this.menu.txtNumeroFactura.setText(
			"" + this.facturacionModel.updateNumberComprobante()
		);
	}

	public void updateNumberDato() {
		this.menu.lblNumeroDatoGeneral.setText(
			"" + this.facturacionModel.updateNumberDato()
		);
	}

	public void guardarDetalle() {
		/* si la bandera es true guardara los detalles a una factura de lo contrario los guardara a un comprovante*/
		try {
			this.filas = this.menu.tblFacturacion.getRowCount();
			String[][] detallesList = new String[this.filas][4];
			if (this.filas > 0) {
				for (int i = 0; i < this.filas; i++) {
					this.producto = Integer.parseInt(this.menu.tblFacturacion.getValueAt(i, 0).toString());
					this.precio = Float.parseFloat(this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 4).toString()));
					this.cantidad = Float.parseFloat(this.menu.tblFacturacion.getValueAt(i, 2).toString());
					this.importe = Float.parseFloat(this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 5).toString()));
					//this.facturacionModel.setFactura(Integer.parseInt(this.menu.lblNumeroDatoGeneral.getText()));
					detallesList[i][0] = this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 0).toString());
					detallesList[i][1] = this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 4).toString());
					detallesList[i][2] = this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 2).toString());
					detallesList[i][3] = this.CleanChars(this.menu.tblFacturacion.getValueAt(i, 5).toString());
					/*this.facturacionModel.setProducto(producto);
					this.facturacionModel.setPrecio(precio);
					this.facturacionModel.setCantidad(cantidad);
					this.facturacionModel.setImporte(importe);
					this.facturacionModel.guardarDetalle();*/
				}
				this.facturacionModel.setDetallesList(detallesList);
				/*this.updateNumberComprobante();
				this.updateNumberDato();
				this.limpiar();*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void add(int id, float cantVender) {
		float cantidad;
		float importe;
		float precio;
		boolean isExiste = false; //para saber si el producto ya existe dentro de la factura 
		float inventario = Procedures.verificarInventario(id, ""); //Obtener el inventario actual de producto
		this.filas = this.menu.tblFacturacion.getRowCount(); //para ver si hay filas en la factura
		try {
			/* si hay mas de cero filas comenzamos la busqueda de lo contrario agregamos una fila nueva */
			if (this.filas > 0) {
				for (int i = 0; i < this.filas; i++) {
					this.producto = Integer.parseInt(this.menu.tblFacturacion.getValueAt(i, 0).toString());
					if (id == this.producto) {
						isExiste = true;
						cantidad = Float.parseFloat(this.menu.tblFacturacion.getValueAt(i, 2).toString());
						cantidad += cantVender;
						precio = Float.parseFloat(this.menu.tblFacturacion.getValueAt(i, 4).toString());
						importe = cantidad * precio;
						/* Verificamos que la cantidad a agrega no sobrepase el stock actual */
						if (inventario >= cantidad) {
							this.menu.tblFacturacion.setValueAt(this.formato.format(importe), i, 5);
							this.menu.tblFacturacion.setValueAt(this.formato.format(cantidad), i, 2);
							this.menu.txtBuscarProductoFacturacion.requestFocus();
							break; //detenemos el ciclo por que ya lo encontramos
						} else {
							JOptionPane.showMessageDialog(null, "Stock insuficiente para la venta");
							break; //detemos el ciclo por que ya lo encontramos pero no hay suficiente para la venta
						}
					}
				}
			}
			/* Agregamos una nueva fila y ademas validamos que la cantidad no sobrepase el stock actual */
			if (!isExiste) {
				if (inventario >= cantVender) {
					this.modelo.addRow(this.facturacionModel.getProducto);
					this.menu.txtBuscarProductoFacturacion.requestFocus();
				}else{
					JOptionPane.showMessageDialog(null, "Stock insuficiente para la venta");
				}
			}
			this.sumarImportes(); //sumamos los importes para el total de factura
			isExiste = false; 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarConCodigoBarra() {
		this.codigoBarra = this.menu.txtCodigoBarraFacturacion.getText();
		if (!this.codigoBarra.equals("")) {
			facturacionModel.getProductoVender(this.codigoBarra);
			this.add(Integer.parseInt(facturacionModel.getProducto[0]), 1);
		} else {
			JOptionPane.showMessageDialog(null, "Escriba un codigo de barras.");
		}

	}

	public void sumarImportes() {
		this.filas = this.menu.tblFacturacion.getRowCount();
		float suma = 0;
		for (int i = 0; i < filas; i++) {
			suma += Float.parseFloat(CleanChars(this.menu.tblFacturacion.getValueAt(i, 5).toString()));
		}
		this.menu.lblTotalFactura.setText(this.formato.format(suma));
	}

	public void agregarDesdeVentana() {
		this.filaseleccionada = this.menu.tblProductosFacturacion.getSelectedRow();
		try {
			if (this.filaseleccionada != -1) {
				int confir = JOptionPane.showConfirmDialog(null, this.spinner, "Cantidad:", JOptionPane.YES_OPTION);
				this.spinner.requestFocus();
				if (confir == JOptionPane.YES_OPTION) {
					this.cantidad = Float.parseFloat(this.spinner.getValue().toString());
					if (this.cantidad > 0) {
						this.producto = Integer.parseInt(
							this.menu.tblProductosFacturacion.getValueAt(this.filaseleccionada, 0).toString()
						);
						this.facturacionModel.getProductoVender(this.producto, cantidad);
						this.add(this.producto, this.cantidad);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mostrarProductosVender(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblProductosFacturacion);
		this.facturacionModel.getProductosVender(value);
		this.menu.tblProductosFacturacion.setModel(this.facturacionModel.tableModel);
	}

	public void windowProducts() {
		this.menu.jdProductosFacturacion.setSize(1234, 489);
		this.menu.jdProductosFacturacion.setLocationRelativeTo(null);
		this.menu.jdProductosFacturacion.setVisible(true);
	}

	public void windowCreditos() {
		this.menu.jdCreditoFacturacion.setSize(734, 324);
		this.menu.jdCreditoFacturacion.setLocationRelativeTo(null);
		this.menu.jdCreditoFacturacion.setVisible(true);
	}

	public void devolverAinventario() {
		this.filas = this.menu.tblFacturacion.getRowCount();
		this.modelo = (DefaultTableModel) this.menu.tblFacturacion.getModel();
		if (filas > 0) {
			try {
				for (int i = 0; i < filas; i++) {
					this.producto = Integer.parseInt(this.modelo.getValueAt(i, 0).toString());
					this.cantidad = Float.parseFloat(this.modelo.getValueAt(i, 2).toString());
					this.facturacionModel.agregarInventario(producto, cantidad);
				}
				this.mostrarProductosVender("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void limpiar() {
		this.filas = this.menu.tblFacturacion.getRowCount();
		this.modelo = (DefaultTableModel) this.menu.tblFacturacion.getModel();
		try {
			for (int i = 0; i < filas; i++) {
				this.modelo.removeRow(0);
			}
			this.menu.lblTotalFactura.setText("0.00");
			this.menu.lblIdCreditoFacturacion.setText("0");
			this.menu.txtClienteFacturacion.setText("");
			this.menu.txtCompradorFacturacion.setText("");
			this.menu.txtCodigoBarraFacturacion.setText("");
			this.menu.cmbFormaPagoFacturacion.setSelectedItem("");
			this.menu.cmbTipoComprobante.setSelectedItem("Comprobante");
			this.menu.cmbFormaPagoFacturacion.setSelectedItem(new CmbTipoVenta(1, ""));
			this.menu.btnGuardarFacturacion.setEnabled(false);
			this.menu.btnGuardarImprimirFacturacion.setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminarArticulo() {
		this.filaseleccionada = this.menu.tblFacturacion.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.producto = Integer.parseInt(this.menu.tblFacturacion.getValueAt(this.filaseleccionada, 0).toString());
			this.cantidad = Float.parseFloat(this.menu.tblFacturacion.getValueAt(this.filaseleccionada, 2).toString());
			//this.facturacionModel.agregarInventario(producto, cantidad);
			this.modelo = (DefaultTableModel) this.menu.tblFacturacion.getModel();
			this.modelo.removeRow(this.filaseleccionada);
			sumarImportes();
			this.mostrarProductosVender("");
		}
	}

	public void mostrarCreditos(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblCreditosFacturacion);
		this.facturacionModel.mostrarCreditos(value);
		this.menu.tblCreditosFacturacion.setModel(this.facturacionModel.tableModel);
	}

	public void addCredito() {
		this.filaseleccionada = this.menu.tblCreditosFacturacion.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.menu.lblIdCreditoFacturacion.setText(
				this.menu.tblCreditosFacturacion.getValueAt(this.filaseleccionada, 0).toString()
			);
			this.menu.txtClienteFacturacion.setText(
				this.menu.tblCreditosFacturacion.getValueAt(this.filaseleccionada, 1).toString()
			);
			this.menu.cmbFormaPagoFacturacion.setSelectedItem(new CmbTipoVenta(2, ""));
			this.menu.jdCreditoFacturacion.setVisible(false);
		}
	}

	public void habilitarBtnGuardar() {
		this.filas = this.menu.tblFacturacion.getRowCount();
		if (this.filas > 0) {
			this.menu.btnGuardarFacturacion.setEnabled(true);
			this.menu.btnGuardarImprimirFacturacion.setEnabled(true);
		} else {
			this.menu.btnGuardarFacturacion.setEnabled(false);
			this.menu.btnGuardarImprimirFacturacion.setEnabled(false);
		}
	}


	/*--------------------------------- EVENTOS ------------------------------- */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.menu.txtCodigoBarraFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.agregarConCodigoBarra();
			this.habilitarBtnGuardar();
		} else if (e.getSource() == this.menu.txtCodigoBarraFacturacion && e.getKeyCode() == e.VK_F7) {
			this.windowProducts();
		} else if (e.getSource() == this.menu.tblProductosFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.agregarDesdeVentana();
			this.habilitarBtnGuardar();
		} else if (e.getSource() == this.menu.tblCreditosFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.addCredito();
		} else if (e.getSource() == this.menu.txtCodigoBarraFacturacion && e.getKeyCode() == e.VK_F8) {
			this.guardarFactura();
		} else if (e.getSource() == this.menu.tblFacturacion && e.getKeyCode() == e.VK_F8) {
			this.guardarFactura();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProductoFacturacion) {
			this.mostrarProductosVender(this.menu.txtBuscarProductoFacturacion.getText());
		} else if (e.getSource() == this.menu.txtBuscarCreditoFacturacion) {
			this.mostrarCreditos(this.menu.txtBuscarCreditoFacturacion.getText());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.tblProductosFacturacion) {
			if (e.getClickCount() == 1) {
				this.agregarDesdeVentana();
				this.habilitarBtnGuardar();
			}
		} else if (e.getSource() == this.menu.tblCreditosFacturacion) {
			if (e.getClickCount() == 1) {
				this.addCredito();
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
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarFacturacion": {
				this.guardarFactura();
			}
			break;
			case "btnGuardarImprimirFacturacion": {

			}
			break;
			case "btnLimpiarFacturacion": {
				//this.devolverAinventario();
				this.limpiar();
				this.habilitarBtnGuardar();
			}
			break;
			case "btnAgregarProductoFacturacion": {
				this.agregarConCodigoBarra();
				this.habilitarBtnGuardar();
			}
			break;
			case "btnBuscarArticuloFacturacion": {
				this.windowProducts();
			}
			break;
			case "btnEliminarArticuloFacturacion": {
				this.eliminarArticulo();
				this.habilitarBtnGuardar();
			}
			break;
			case "btnAgregarCreditoFacturacion": {
				this.windowCreditos();
			}
			break;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.menu.cmbTipoComprobante && e.getStateChange() == ItemEvent.SELECTED) {
			Object item = e.getItem();
			if (item.toString().equals("Factura")) {
				this.updateNumberFactura();
			} else if (item.toString().equals("Comprobante")) {
				this.updateNumberComprobante();
			}
		}
	}

}
