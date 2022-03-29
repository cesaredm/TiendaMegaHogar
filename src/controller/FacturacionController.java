package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import view.PrincipalView;
import model.FacturacionModel;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import model.Procedures;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class FacturacionController implements KeyListener, CaretListener, MouseListener, ActionListener, WindowListener {

	private static FacturacionController instancia = null;
	PrincipalView menu;
	FacturacionModel facturacionModel;
	DefaultTableModel modelo;
	String codigoBarra;
	DecimalFormat formato;
	int filaseleccionada, producto, filas;
	float cantidad;
	SpinnerNumberModel spinnerModel;
	JSpinner spinner;

	private FacturacionController(PrincipalView menu, FacturacionModel facturacionModel) {
		this.menu = menu;
		this.facturacionModel = facturacionModel;
		this.formato = new DecimalFormat("##,###,###,##0.00");
		start();
		this.createJSpinner();
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
		this.menu.addWindowListener(this);
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

	public void start() {
		this.menu.lblEmpleadoFacturacion.setVisible(false);
		this.menu.lblIdCreditoFacturacion.setVisible(false);
		this.menu.btnGuardarFacturacion.setEnabled(false);
		this.menu.btnGuardarImprimirFacturacion.setEnabled(false);
		this.menu.jcFechaFactura.setDate(new Date());
		EstiloTablas.estilosCabeceras(this.menu.tblFacturacion);
		this.menu.txtCodigoBarraFacturacion.requestFocus();
	}

	public String CleanChars(String cadena) {
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

	public void guardar() {

	}

	public void add(int id, float cantVender) {
		float cantidad;
		float importe;
		float precio;
		this.filas = this.menu.tblFacturacion.getRowCount();
		try {
			if (this.filas > 0) {
				for (int i = 0; i < this.filas; i++) {
					this.producto = Integer.parseInt(this.menu.tblFacturacion.getValueAt(i, 0).toString());
					if (id == this.producto) {
						cantidad = Float.parseFloat(this.menu.tblFacturacion.getValueAt(i, 2).toString());
						cantidad += cantVender;
						precio = Float.parseFloat(this.menu.tblFacturacion.getValueAt(i, 4).toString());
						importe = cantidad * precio;
						this.menu.tblFacturacion.setValueAt(this.formato.format(importe), i, 5);
						this.menu.tblFacturacion.setValueAt(this.formato.format(cantidad), i, 2);
						this.mostrarProductosVender("");
						this.menu.txtBuscarProductoFacturacion.requestFocus();
					} else {
						this.modelo.addRow(this.facturacionModel.getProducto);
						this.mostrarProductosVender("");
						this.menu.txtBuscarProductoFacturacion.requestFocus();
					}
				}
			} else {
				this.modelo.addRow(this.facturacionModel.getProducto);
				this.mostrarProductosVender("");
				this.menu.txtBuscarProductoFacturacion.requestFocus();
			}
			sumarImportes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarConCodigoBarra() {
		this.codigoBarra = this.menu.txtCodigoBarraFacturacion.getText();
		if (!this.codigoBarra.equals("")) {
			facturacionModel.getProductoVender(this.codigoBarra);
			if (Procedures.response) {
				this.add(Integer.parseInt(facturacionModel.getProducto[0]), 1);
			}
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
					this.producto = Integer.parseInt(
						this.menu.tblProductosFacturacion.getValueAt(this.filaseleccionada, 0).toString()
					);
					this.cantidad = Float.parseFloat(this.spinner.getValue().toString());
					this.facturacionModel.getProductoVender(this.producto, cantidad);
					if (Procedures.response) {
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
			this.menu.txtClienteFacturacion.setText("");
			this.menu.txtCompradorFacturacion.setText("");
			this.menu.cmbFormaPagoFacturacion.setSelectedItem("");
			this.menu.cmbTipoComprobante.setSelectedIndex(1);
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
			this.facturacionModel.agregarInventario(producto, cantidad);
			this.modelo = (DefaultTableModel) this.menu.tblFacturacion.getModel();
			this.modelo.removeRow(this.filaseleccionada);
			sumarImportes();
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
			this.menu.jdCreditoFacturacion.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.menu.txtCodigoBarraFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.agregarConCodigoBarra();
		} else if (e.getSource() == this.menu.txtCodigoBarraFacturacion && e.getKeyCode() == e.VK_F7) {
			this.windowProducts();
		} else if (e.getSource() == this.menu.tblProductosFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.agregarDesdeVentana();
		} else if (e.getSource() == this.menu.tblCreditosFacturacion && e.getKeyCode() == e.VK_ENTER) {
			this.addCredito();
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

			}
			break;
			case "btnGuardarImprimirFacturacion": {

			}
			break;
			case "btnLimpiarFacturacion": {
				devolverAinventario();
				limpiar();
			}
			break;
			case "btnAgregarProductoFacturacion": {
				agregarConCodigoBarra();
			}
			break;
			case "btnBuscarArticuloFacturacion": {
				windowProducts();
			}
			break;
			case "btnEliminarArticuloFacturacion": {
				eliminarArticulo();
			}
			break;
			case "btnAgregarCreditoFacturacion": {
				windowCreditos();
			}
			break;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		devolverAinventario();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
