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
import view.PrincipalView;

/**
 *
 * @author HOLA
 */
public class PagosPedidosController implements ActionListener,CaretListener{
	private static PagosPedidosController instancia = null;
	PrincipalView menu;
	PagosPedidosModel pagosModel;
	Date fecha;
	private PagosPedidosController(PrincipalView menu,PagosPedidosModel pagosModel){
		this.menu = menu;
		this.pagosModel = pagosModel;
	}

	public static void createInstnciaController(PrincipalView menu,PagosPedidosModel pagosModel){
		if(instancia == null)	
			instancia = new PagosPedidosController(menu, pagosModel);
	}

	public void setData(){
		this.fecha = (this.menu.jcFechaPagoPedido.getDate() == null) ? new Date() : this.menu.jcFechaPagoPedido.getDate();
		this.pagosModel.setFecha(new java.sql.Timestamp(this.fecha.getTime()));
		this.pagosModel.setPedido(Integer.parseInt(this.menu.lblIdPedido.getText()));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void caretUpdate(CaretEvent e) {
	}
	
}
