/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.PagosPedidosModel;
import model.PedidosModel;
import view.PrincipalView;

/**
 *
 * @author HOLA
 */
public class PagosPedidosController implements ActionListener, CaretListener {

	private static PagosPedidosController instancia = null;
	PrincipalView menu;
	PagosPedidosModel pagosModel;
	PedidosModel pedidosModel;
	Date fecha;

	private PagosPedidosController(PrincipalView menu, PagosPedidosModel pagosModel) {
		this.menu = menu;
		this.pagosModel = pagosModel;
		this.pedidosModel = new PedidosModel();
		this.menu.btnGuardarPagoPedido.addActionListener(this);
		this.menu.txtBuscarPagoPedido.addCaretListener(this);
	}

	public static void createInstnciaController(PrincipalView menu, PagosPedidosModel pagosModel) {
		if (instancia == null) {
			instancia = new PagosPedidosController(menu, pagosModel);
		}
	}

	public void setData() {
		this.fecha = (this.menu.jcFechaPagoPedido.getDate() == null) ? new Date() : this.menu.jcFechaPagoPedido.getDate();
		this.pagosModel.setFecha(new java.sql.Timestamp(this.fecha.getTime()));
		this.pagosModel.setPedido(Integer.parseInt(this.menu.lblIdPedido.getText()));
		this.pagosModel.setMonto(Float.parseFloat(this.menu.jsMontoPagoPedido.getValue().toString()));
	}

	public void getPagos(int pedido, String value){
		this.pedidosModel.getPagosPorPedido(pedido,value);
		this.menu.tblPagosPedidos.setModel(this.pedidosModel.tableModel);
	}

	public void getPedidos(String value){
		EstiloTablas.estilosCabeceras(this.menu.tblPedidos);
		this.pedidosModel.getPedidos(value);
		this.menu.tblPedidos.setModel(this.pedidosModel.tableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarPagoPedido": {
				this.setData();
				this.pagosModel.guardar();
				this.getPagos(this.pagosModel.getPedido(),"");
				this.getPedidos("");
			}
			break;
			default:
				throw new AssertionError();
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarPagoPedido) {
			getPagos(0, this.menu.txtBuscarPagoPedido.getText());
		}
	}

}
