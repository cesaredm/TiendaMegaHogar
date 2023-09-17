/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import model.CmbMarcas;
import model.ProductosModel;
import model.KardexModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProductosController implements ActionListener, CaretListener {

	private static ProductosController instancia = null;
	PrincipalView menu;
	ProductosModel productosModel;
	KardexModel kardex;
	DefaultComboBoxModel comboModel;
	CmbMarcas marcas;
	int filaseleccionada, id;
	float cantidadAgregar;
	JSpinner spiner;
	SpinnerNumberModel spinnerModel;
	DecimalFormat numberFormat;

	private ProductosController(PrincipalView menu, ProductosModel productosModel, KardexModel kardex) {
		this.menu = menu;
		this.productosModel = productosModel;
		this.kardex = kardex;
		this.numberFormat = new DecimalFormat("#,###,###,###,#00.0");
		this.menu.btnActualizarProducto.setEnabled(false);
		this.mostrar("");
		this.menu.jifMarca.setVisible(false);
		this.menu.btnAgregarMarca.addActionListener(this);
		this.menu.btnGuardarProducto.addActionListener(this);
		this.menu.btnLimpiarProdducto.addActionListener(this);
		this.menu.btnGuardarMovimientoKardex.addActionListener(this);
		this.menu.optEditarProducto.addActionListener(this);
		this.menu.optEliminarProducto.addActionListener(this);
		this.menu.btnActualizarProducto.addActionListener(this);
		this.menu.txtBuscarProducto.addCaretListener(this);
		this.menu.optAgregarProducto.addActionListener(this);
		this.menu.optKardexProducto.addActionListener(this);
		/* crear spinner para agregar producto a inventario */
		this.spinnerModel = new SpinnerNumberModel();
		this.spinnerModel.setValue(0.0);
		this.spinnerModel.setMinimum(0.0);
		this.spinnerModel.setStepSize(0.01);
		this.spiner = new JSpinner(spinnerModel);
	}

	public static void createInstanceController(PrincipalView menu, ProductosModel productosModel, KardexModel kardex) {
		if (instancia == null) {
			instancia = new ProductosController(menu, productosModel, kardex);
		}
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.txtDescripcionProducto.setText("");
			this.menu.txtCodBarraProducto.setText("");
			this.menu.txtModeloProducto.setText("");
			this.menu.cmbMarcasProducto.setSelectedItem(new CmbMarcas(1, null));
			this.menu.jsPrecioCostoProducto.setValue(0.00);
			this.menu.jsPrecioVentaProducto.setValue(0.00);
			this.menu.jsCantidadProducto.setValue(0.00);
			this.menu.btnActualizarProducto.setEnabled(false);
			this.menu.btnGuardarProducto.setEnabled(true);
			this.menu.jsCantidadProducto.setEnabled(true);
		}
	}

	public void guardar() {
		this.marcas = (CmbMarcas) this.menu.cmbMarcasProducto.getSelectedItem();
		this.productosModel.setDescripcion(this.menu.txtDescripcionProducto.getText());
		this.productosModel.setCodigoBarra(this.menu.txtCodBarraProducto.getText());
		this.productosModel.setModelo(this.menu.txtModeloProducto.getText());
		this.productosModel.setMarca(this.marcas.getId());
		this.productosModel.setPrecioCosto((float) this.menu.jsPrecioCostoProducto.getValue());
		this.productosModel.setPrecioVenta((float) this.menu.jsPrecioVentaProducto.getValue());
		this.productosModel.setStock((float) this.menu.jsCantidadProducto.getValue());
		this.productosModel.guardar();
		this.limpiar(this.productosModel.validar);
		this.mostrar("");
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblProductos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.productosModel.setId(Integer.parseInt(this.menu.tblProductos.getValueAt(this.filaseleccionada, 0).toString()));
			this.productosModel.editar();
			this.menu.txtDescripcionProducto.setText(this.productosModel.getDescripcion());
			this.menu.txtCodBarraProducto.setText(this.productosModel.getCodigoBarra());
			this.menu.txtModeloProducto.setText(this.productosModel.getModelo());
			this.menu.jsPrecioCostoProducto.setValue(this.productosModel.getPrecioCosto());
			this.menu.jsPrecioVentaProducto.setValue(this.productosModel.getPrecioVenta());
			this.menu.cmbMarcasProducto.setSelectedItem(new CmbMarcas(this.productosModel.getMarca(), null));
			this.menu.jsCantidadProducto.setValue(this.productosModel.getStock());
			this.menu.btnGuardarProducto.setEnabled(false);
			this.menu.btnActualizarProducto.setEnabled(true);
			this.menu.jsCantidadProducto.setEnabled(false);
		}
	}

	public void actualizar() {
		this.marcas = (CmbMarcas) this.menu.cmbMarcasProducto.getSelectedItem();
		this.productosModel.setDescripcion(this.menu.txtDescripcionProducto.getText());
		this.productosModel.setCodigoBarra(this.menu.txtCodBarraProducto.getText());
		this.productosModel.setModelo(this.menu.txtModeloProducto.getText());
		this.productosModel.setMarca(this.marcas.getId());
		this.productosModel.setPrecioCosto((float) this.menu.jsPrecioCostoProducto.getValue());
		this.productosModel.setPrecioVenta((float) this.menu.jsPrecioVentaProducto.getValue());
		this.productosModel.setStock((float) this.menu.jsCantidadProducto.getValue());
		this.productosModel.actualizar();
		this.limpiar(this.productosModel.validar);
		this.mostrar("");
	}

	public void eliminar() {
		this.filaseleccionada = this.menu.tblProductos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			int conf = JOptionPane.showConfirmDialog(
				null,
				"Seguro quieres eliminar este producto",
				"Advertencia",
				JOptionPane.YES_NO_CANCEL_OPTION
			);
			if (conf == JOptionPane.YES_OPTION) {
				this.productosModel.setId(Integer.parseInt(this.menu.tblProductos.getValueAt(this.filaseleccionada, 0).toString()));
				this.productosModel.eliminar();
			}
		}
	}

	public void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblProductos);
		this.productosModel.mostrar(value);
		this.menu.tblProductos.setModel(this.productosModel.tableModelProductos);
	}

	public void limpiarFormKardex(){
		this.menu.txtAreaNotaMovimientoKardex.setText("");
		this.menu.jsCantidadMovimientoKardex.setValue(0);
	}

	public void crearMovimientoKardex() {
		this.kardex.setTipoMovimiento(this.menu.cmbTipoMovimientoKardex.getSelectedItem().toString());
		this.kardex.setCantidad(Float.parseFloat(this.menu.jsCantidadMovimientoKardex.getValue().toString()));
		this.kardex.setNota(this.menu.txtAreaNotaMovimientoKardex.getText());
		this.kardex.setProducto(this.id);
		this.kardex.guardar();
		this.menu.jdMovKardex.setVisible(false);
		this.mostrar("");
		this.limpiarFormKardex();
	}

	public void agregarProductoInventario() {
		this.filaseleccionada = this.menu.tblProductos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.id = Integer.parseInt(this.menu.tblProductos.getValueAt(this.filaseleccionada, 0).toString());
			this.menu.jdMovKardex.setSize(400, 340);
			this.menu.jdMovKardex.setLocationRelativeTo(null);
			this.menu.jdMovKardex.setVisible(true);
		}
	}

	/*public void guardarKardex(boolean confirmacion, int producto, float cantidad, String accion) {
		if (confirmacion) {
			long fecha = new Date().getTime();
			this.kardex.setProducto(producto);
			this.kardex.setCantidad(cantidad);
			this.kardex.setFecha(new java.sql.Timestamp(fecha));
			this.kardex.setAccion(accion);
			this.kardex.setEmpleado(MenuController.empleadoSistema);
			this.kardex.guardar();
		}
	}*/

	public void mostrarKardex() {
		this.filaseleccionada = this.menu.tblProductos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			/* Estilo de cabeceras de las tablas */
			EstiloTablas.estilosCabeceras(this.menu.tblSalidasKardex);
			EstiloTablas.estilosCabeceras(this.menu.tblOtrosMovimientosKardex);
			/* Id de producto */ 
			this.id = Integer.parseInt(this.menu.tblProductos.getValueAt(this.filaseleccionada, 0).toString());
			/* Obtengo el inventario actual desde la tabla prodcuto */
			this.menu.lblInventarioActual.setText((String) this.menu.tblProductos.getValueAt(this.filaseleccionada, 7));
			this.menu.lblNombreProductoKardex.setText((String) this.menu.tblProductos.getValueAt(this.filaseleccionada, 2));
			this.kardex.getSumaMovimientosKardex(this.id);
			/* obtengo lista de salidas */
			this.kardex.salidas(this.id);
			this.menu.tblSalidasKardex.setModel(this.kardex.tableModel);
			this.menu.lblEntradasKardex.setText(this.numberFormat.format(this.kardex.getEntradas()));
			this.menu.lblSalidasKardex.setText(this.numberFormat.format(this.kardex.getSalidas()));
			//this.menu.lblKardexInicial.setText(this.numberFormat.format(this.kardex.getInventarioInicial()));
			/* obtengo lista de entradas */
			this.kardex.getEntradas(this.id);
			this.menu.tblOtrosMovimientosKardex.setModel(this.kardex.tableModel);
			this.menu.jdKardex.setSize(1108, 473);
			this.menu.jdKardex.setLocationRelativeTo(null);
			this.menu.jdKardex.setVisible(true);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnAgregarMarca": {
				this.menu.jifMarca.setVisible(true);
			}
			break;
			case "btnGuardarProducto": {
				this.guardar();
			}
			break;
			case "btnGuardarMovimientoKardex":{
				crearMovimientoKardex();
			}
			break;
			case "btnLimpiarProducto": {
				this.limpiar(true);
			}
			break;
			case "optEditarProducto": {
				this.editar();
			}
			break;
			case "btnActualizarProducto": {
				this.actualizar();
			}
			break;
			case "optEliminarProducto": {
				this.eliminar();
			}
			break;
			case "optKardexProducto": {
				this.mostrarKardex();
			}
			break;
			case "optAgregarProducto": {
				this.agregarProductoInventario();
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProducto) {
			this.mostrar(this.menu.txtBuscarProducto.getText());
		}
	}

}
