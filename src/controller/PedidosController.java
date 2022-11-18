/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.PedidosModel;
import view.PrincipalView;

/**
 *
 * @author HOLA
 */
public class PedidosController implements ActionListener,CaretListener,MouseListener{
	private static PedidosController instancia = null;
	PrincipalView menu;
	PedidosModel pedidosModel;
	int filaseleccionada;
	private PedidosController(PrincipalView menu, PedidosModel pedidosModel){
		this.menu = menu;
		this.pedidosModel = pedidosModel;
		//actionListener
		this.menu.btnAgregarProductoPedido.addActionListener(this);
		this.menu.btnProveedorPedido.addActionListener(this);
		//caretListener
		this.menu.txtBuscarProveedorPedido.addCaretListener(this);
		//MouseListener
		this.menu.tblProveedoresPedidos.addMouseListener(this);
		
	}

	public static void createInstanciaController(PrincipalView menu, PedidosModel pedidosModel){
		if(instancia == null)
			instancia = new PedidosController(menu, pedidosModel);
	}

	public void getProveedores(String value){
		EstiloTablas.estilosCabeceras(this.menu.tblProveedoresPedidos);
		this.pedidosModel.getProveedores(value);
		this.menu.tblProveedoresPedidos.setModel(this.pedidosModel.tableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnProveedorPedido":{
				this.menu.jdProveedorPedido.setSize(676, 272);
				this.menu.jdProveedorPedido.setLocationRelativeTo(null);
				this.menu.jdProveedorPedido.setVisible(true);
			}break;
			case "":{

			}break;
			default:
				throw new AssertionError();
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == this.menu.txtBuscarProveedorPedido){
			this.getProveedores(this.menu.txtBuscarProveedorPedido.getText());
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


}
