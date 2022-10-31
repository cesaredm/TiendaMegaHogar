/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import view.PrincipalView;
import model.CreditosModel;
import java.util.Date;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CreditosController implements ActionListener, CaretListener, MouseListener, KeyListener {

	private static CreditosController instancia = null;
	CreditosModel creditosModel;
	PrincipalView menu;
	int filaseleccionada;

	private CreditosController(PrincipalView menu, CreditosModel creditosModel) {
		this.menu = menu;
		this.creditosModel = creditosModel;
		this.mostrar("");
		this.mostrarCreditosPendientes("");
		this.menu.btnActualizarCredito.setEnabled(false);
		this.menu.jcFechaCredito.setDate(new Date());
		this.menu.btnGuardarCredito.addActionListener(this);
		this.menu.btnActualizarCredito.addActionListener(this);
		this.menu.btnAddAvalCredito.addActionListener(this);
		this.menu.btnLimpiarCredito.addActionListener(this);
		this.menu.btnAddClienteCredito.addActionListener(this);
		this.menu.optEditarCredito.addActionListener(this);
		this.menu.optEliminarCredito.addActionListener(this);
		this.menu.optGenerarPago.addActionListener(this);
		this.menu.optHistorialCredito.addActionListener(this);
		this.menu.txtBuscarCredito.addCaretListener(this);
		this.menu.txtBuscarCreditoPendiente.addCaretListener(this);
		this.menu.txtBuscarAvalCredito.addCaretListener(this);
		this.menu.txtBuscarClienteCredito.addCaretListener(this);
		this.menu.tblAvalCredito.addMouseListener(this);
		this.menu.tblClientesCreditos.addMouseListener(this);
		this.menu.tblAvalCredito.addKeyListener(this);
		this.menu.tblClientesCreditos.addKeyListener(this);
	}

	public static void createInstanceController(PrincipalView menu, CreditosModel creditosModel) {
		if (instancia == null) {
			instancia = new CreditosController(menu, creditosModel);
		}
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.jcFechaCredito.setDate(new Date());
			this.menu.jsAvalCredito.setValue(0);
			this.menu.jsClienteCredito.setValue(0);
			this.menu.cmbEstadoCredito.setSelectedIndex(1);
			this.menu.txtClienteCredito.setText("");
			this.menu.txtAvalCredito.setText("");
			this.menu.btnGuardarCredito.setEnabled(true);
			this.menu.btnActualizarCredito.setEnabled(false);
		}
	}

	public void guardar() {
		this.creditosModel.setFecha(new java.sql.Timestamp(this.menu.jcFechaCredito.getDate().getTime()));
		this.creditosModel.setCliente((int) this.menu.jsClienteCredito.getValue());
		this.creditosModel.setAval((int) this.menu.jsAvalCredito.getValue());
		this.creditosModel.setEstado(this.menu.cmbEstadoCredito.getSelectedItem().toString());
		this.creditosModel.guardar();
		this.limpiar(this.creditosModel.validar);
		this.mostrar("");
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblCreditos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.creditosModel.setId(Integer.parseInt(this.menu.tblCreditos.getValueAt(filaseleccionada, 0).toString()));
			this.creditosModel.editar();
			this.creditosModel.nombreAval();
			if (this.creditosModel.getEstado().equals("Pendiente")) {
				this.menu.cmbEstadoCredito.setEnabled(false);
			}
			this.menu.jcFechaCredito.setDate(this.creditosModel.getFecha());
			this.menu.jsClienteCredito.setValue(this.creditosModel.getCliente());
			this.menu.jsAvalCredito.setValue(this.creditosModel.getAval());
			this.menu.cmbEstadoCredito.setSelectedItem(this.creditosModel.getEstado());
			this.menu.txtClienteCredito.setText(this.creditosModel.getNombreCliente());
			this.menu.txtAvalCredito.setText(this.creditosModel.getNombreAval());
			this.menu.btnGuardarCredito.setEnabled(false);
			this.menu.btnActualizarCredito.setEnabled(true);
		}
	}

	public void actualizar() {
		this.creditosModel.setFecha(new java.sql.Timestamp(this.menu.jcFechaCredito.getDate().getTime()));
		this.creditosModel.setCliente((int) this.menu.jsClienteCredito.getValue());
		this.creditosModel.setAval((int) this.menu.jsAvalCredito.getValue());
		this.creditosModel.setEstado(this.menu.cmbEstadoCredito.getSelectedItem().toString());
		this.creditosModel.actualizar();
		this.limpiar(this.creditosModel.validar);
		this.mostrar("");
		this.menu.btnGuardarCredito.setEnabled(false);
		this.menu.btnActualizarCredito.setEnabled(true);
	}

	public void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblCreditos);
		this.creditosModel.mostrar(value);
		this.menu.tblCreditos.setModel(this.creditosModel.tableModel);
	}

	public void mostrarCreditosPendientes(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblCreditosPendientes);
		this.creditosModel.creditosPendientes(value);
		this.menu.tblCreditosPendientes.setModel(this.creditosModel.tableModel);
	}

	public void eliminar() {
		this.filaseleccionada = this.menu.tblCreditos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.creditosModel.setId(Integer.parseInt(this.menu.tblCreditos.getValueAt(filaseleccionada, 0).toString()));
			this.creditosModel.isPendiente();
			this.mostrar("");
		}
	}

	public void showWindowAval() {
		this.menu.jdAvalCreditos.setSize(612, 345);
		this.menu.jdAvalCreditos.setLocationRelativeTo(null);
		this.menu.jdAvalCreditos.setVisible(true);
	}

	public void showWindowClientes() {
		this.menu.jdClienteCredito.setSize(614, 337);
		this.menu.jdClienteCredito.setLocationRelativeTo(null);
		this.menu.jdClienteCredito.setVisible(true);
	}

	public void avales(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblAvalCredito);
		this.creditosModel.getAvales(value);
		this.menu.tblAvalCredito.setModel(this.creditosModel.tableModel);
	}

	public void clientes(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblClientesCreditos);
		this.creditosModel.getClientes(value);
		this.menu.tblClientesCreditos.setModel(this.creditosModel.tableModel);
	}

	public void setAval() {
		this.filaseleccionada = this.menu.tblAvalCredito.getSelectedRow();
		this.menu.jsAvalCredito.setValue(Integer.parseInt(this.menu.tblAvalCredito.getValueAt(filaseleccionada, 0).toString()));
		this.menu.txtAvalCredito.setText((String) this.menu.tblAvalCredito.getValueAt(filaseleccionada, 1));
		this.menu.jdAvalCreditos.setVisible(false);
	}

	public void setCliente() {
		this.filaseleccionada = this.menu.tblClientesCreditos.getSelectedRow();
		this.menu.jsClienteCredito.setValue(Integer.parseInt(this.menu.tblClientesCreditos.getValueAt(filaseleccionada, 0).toString()));
		this.menu.txtClienteCredito.setText((String) this.menu.tblClientesCreditos.getValueAt(filaseleccionada, 1));
		this.menu.jdClienteCredito.setVisible(false);
	}

	public void generarPago() {
		this.filaseleccionada = this.menu.tblCreditosPendientes.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.menu.lblIdCreditoPago.setText(this.menu.tblCreditosPendientes.getValueAt(this.filaseleccionada, 0).toString());
			this.menu.txtNombrePago.setText(this.menu.tblCreditosPendientes.getValueAt(this.filaseleccionada, 1).toString());
			this.menu.txtNombrePago.setEnabled(false);
			this.menu.jdIngresarPago.setSize(871, 560);
			this.menu.jdIngresarPago.setLocationRelativeTo(null);
			this.menu.jdIngresarPago.setVisible(true);
			this.menu.jsMontoPago.requestFocus();
		}
	}

	public void historialCredito(){
		EstiloTablas.estilosCabeceras(this.menu.tblHistorialCredito);
		this.menu.jdHistorialCredito.setSize(1190, 550);
		this.menu.jdHistorialCredito.setLocationRelativeTo(null);
		this.menu.jdHistorialCredito.setVisible(true);
		this.filaseleccionada = this.menu.tblCreditosPendientes.getSelectedRow();
		if(this.filaseleccionada != -1){
			this.creditosModel.setId(Integer.parseInt(this.menu.tblCreditosPendientes.getValueAt(this.filaseleccionada, 0).toString()));
			this.creditosModel.getHistorialCredito();
			this.menu.tblHistorialCredito.setModel(this.creditosModel.tableModel);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarCredito": {
				this.guardar();
			}
			break;
			case "btnLimpiarCredito": {
				this.limpiar(true);
			}
			break;
			case "btnActualizarCredito": {
				this.actualizar();
			}
			break;
			case "optEditarCredito": {
				this.editar();
			}
			break;
			case "optEliminarCredito": {
				this.eliminar();
			}
			break;
			case "btnAddAvalCredito": {
				this.showWindowAval();
			}
			break;
			case "btnAddClienteCredito": {
				this.showWindowClientes();
			}
			break;
			case "optGenerarPago": {
				this.generarPago();
			}
			break;
			case "optHistorialCredito":{
				this.historialCredito();
			}break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarCredito) {
			this.mostrar(this.menu.txtBuscarCredito.getText());
		} else if (e.getSource() == this.menu.txtBuscarAvalCredito) {
			this.avales(this.menu.txtBuscarAvalCredito.getText());
		} else if (e.getSource() == this.menu.txtBuscarClienteCredito) {
			this.clientes(this.menu.txtBuscarClienteCredito.getText());
		} else if (e.getSource() == this.menu.txtBuscarCreditoPendiente) {
			this.mostrarCreditosPendientes(this.menu.txtBuscarCreditoPendiente.getText());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.tblAvalCredito) {
			if (e.getClickCount() == 2) {
				this.setAval();
			}
		} else if (e.getSource() == this.menu.tblClientesCreditos) {
			if (e.getClickCount() == 2) {
				this.setCliente();
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
		if (e.getSource() == this.menu.tblAvalCredito && e.getKeyCode() == e.VK_ENTER) {
			this.setAval();
		} else if (e.getSource() == this.menu.tblClientesCreditos && e.getKeyCode() == e.VK_ENTER) {
			this.setCliente();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
