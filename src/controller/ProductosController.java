/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.CmbMarcas;
import model.ProductosModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProductosController implements ActionListener, CaretListener{

	private static ProductosController instancia = null;
	PrincipalView menu;
	ProductosModel productosModel;
	DefaultComboBoxModel comboModel;
	CmbMarcas marcas;
	int filaseleccionada;

	private ProductosController(PrincipalView menu, ProductosModel productosModel) {
		this.menu = menu;
		this.productosModel = productosModel;
		this.menu.btnActualizarProducto.setEnabled(false);
		this.mostrarMarcas();
		this.mostrar("");
		this.menu.jifMarca.setVisible(false);
		this.menu.btnAgregarMarca.addActionListener(this);
		this.menu.btnGuardarProducto.addActionListener(this);
		this.menu.btnLimpiarProdducto.addActionListener(this);
		this.menu.optEditarProducto.addActionListener(this);
		this.menu.optEliminarProducto.addActionListener(this);
		this.menu.btnActualizarProducto.addActionListener(this);
		this.menu.txtBuscarProducto.addCaretListener(this);
	}

	public static ProductosController getInstancia(PrincipalView menu, ProductosModel productosModel) {
		if (instancia == null) {
			instancia = new ProductosController(menu, productosModel);
		}
		return instancia;
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

	public void mostrarMarcas() {
		this.productosModel.getMarcas();
		this.menu.cmbMarcasProducto.setModel(this.productosModel.comboModel);
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
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProducto) {
			this.mostrar(this.menu.txtBuscarProducto.getText());
		}
	}

}
