package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.util.Date;
import javax.swing.JOptionPane;
import model.PagosModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class PagosController implements ActionListener, CaretListener {

	private static PagosController instancia = null;
	PrincipalView menu;
	PagosModel pagosModel;
	private int credito;

	private PagosController(PrincipalView menu, PagosModel pagosModel) {
		this.menu = menu;
		this.pagosModel = pagosModel;
		this.menu.jcFechaPago.setDate(new Date());
		this.menu.btnActualizarPago.setEnabled(false);
		this.pagosModel.mostrarPagos("");
		this.menu.btnNuevoPago.addActionListener(this);
		this.menu.btnGuardarPago.addActionListener(this);
		this.menu.btnActualizarPago.addActionListener(this);
		this.menu.txtBuscarPago.addCaretListener(this);
		this.menu.optEditarPago.addActionListener(this);
		this.menu.optEliminarPago.addActionListener(this);
	}

	public static void createInstanciaController(PrincipalView menu, PagosModel pagosModel) {
		if (instancia == null) {
			instancia = new PagosController(menu, pagosModel);
		}
	}

	public void limpiar(boolean validar) {
		if (validar) {
			this.menu.jcFechaPago.setDate(new Date());
			this.menu.lblIdCreditoPago.setText("");
			this.menu.jsMontoPago.setValue(0.0);
			this.menu.btnActualizarPago.setEnabled(false);
			this.menu.btnGuardarPago.setEnabled(true);
		}

	}

	public void guardar() {
		this.credito = (this.menu.lblIdCreditoPago.getText().equals("")) ? 0 : Integer.parseInt(this.menu.lblIdCreditoPago.getText());
		this.pagosModel.setFecha(new java.sql.Timestamp(this.menu.jcFechaPago.getDate().getTime()));
		this.pagosModel.setCredito(this.credito);
		this.pagosModel.setMonto(Float.parseFloat(this.menu.jsMontoPago.getValue().toString()));
		this.pagosModel.guardar();
		this.limpiar(this.pagosModel.validar);
		if (this.pagosModel.validar) {
			this.mostrarPagos("");
		}
	}

	public void mostrarPagos(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblPagos);
		this.pagosModel.mostrarPagos(value);
		this.menu.tblPagos.setModel(this.pagosModel.tableModel);
	}

	public void editar() {
		this.pagosModel.editar();
		this.menu.jcFechaPago.setDate(this.pagosModel.getFecha());
		this.menu.lblIdCreditoPago.setText(String.valueOf(this.pagosModel.getCredito()));
		this.menu.jsMontoPago.setValue(this.pagosModel.getMonto());
		this.menu.btnGuardarPago.setEnabled(false);
		this.menu.btnActualizarPago.setEnabled(true);
		this.menu.jdIngresarPago.setSize(275, 260);
		this.menu.jdIngresarPago.setVisible(true);
		this.menu.jdIngresarPago.setLocationRelativeTo(null);
	}

	public void eliminar() {
		int confirmacion = JOptionPane.showConfirmDialog(
			null,
			"Seguro que quieres eliminar este pago de forma permanente.?",
			"Advertencia.",
			JOptionPane.YES_NO_OPTION
		);
		if (confirmacion == JOptionPane.YES_OPTION) {
			this.pagosModel.eliminar();
			this.mostrarPagos("");
		}
	}

	//funcion para deterniar la accion de editar o eliminar
	public void ed(String accion) {
		int filaseleccionada = this.menu.tblPagos.getSelectedRow();
		if (filaseleccionada != -1) {
			this.pagosModel.setId(Integer.parseInt(this.menu.tblPagos.getValueAt(filaseleccionada, 0).toString()));
			if (accion.equals("editar")) {
				this.editar();
			} else {
				this.eliminar();
			}
		}
	}

	public void actualizar() {
		this.credito = (this.menu.lblIdCreditoPago.getText().equals("")) ? 0 : Integer.parseInt(this.menu.lblIdCreditoPago.getText());
		this.pagosModel.setFecha(new java.sql.Timestamp(this.menu.jcFechaPago.getDate().getTime()));
		this.pagosModel.setCredito(this.credito);
		this.pagosModel.setMonto(Float.parseFloat(this.menu.jsMontoPago.getValue().toString()));
		this.pagosModel.actualizar();
		this.limpiar(this.pagosModel.validar);
		if (this.pagosModel.validar) {
			this.mostrarPagos("");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnNuevoPago": {
				this.limpiar(true);
			}
			break;
			case "btnGuardarPago": {
				this.guardar();
			}
			break;
			case "btnActualizarPago": {
				this.actualizar();
			}
			break;
			case "optEditarPago": {
				this.ed("editar");
			}
			break;
			case "optEliminarPago": {
				this.ed("eliminar");
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarPago) {
			this.mostrarPagos(this.menu.txtBuscarPago.getText());
		}
	}
}
