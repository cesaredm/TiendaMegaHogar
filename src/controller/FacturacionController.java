package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class FacturacionController implements KeyListener, CaretListener, MouseListener {

	private static FacturacionController instancia = null;
	PrincipalView menu;
	FacturacionModel facturacionModel;
	DefaultTableModel modelo;
	String codigoBarra;
	DecimalFormat formato;
	int filaseleccionada;
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
		this.menu.txtCodigoBarraFacturacion.addKeyListener(this);
		this.menu.btnBuscarArticuloFacturacion.addKeyListener(this);
		this.menu.tblProductosFacturacion.addKeyListener(this);
		this.menu.txtBuscarProductoFacturacion.addCaretListener(this);
		this.menu.tblProductosFacturacion.addMouseListener(this);
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

	public String CleanChars(String cadena){
		StringBuilder myString = new StringBuilder(cadena);
		char[] signos = {','};
		for(int i=0; i<cadena.length();i++){
			char chr = cadena.charAt(i);

			for (char signo : signos) {
				if(chr == signo){
					myString = myString.deleteCharAt(i);
				}	
			}
		}

		return myString.toString();		
	}

	public void guardar() {

	}

	public void agregarConCodigoBarra() {
		this.codigoBarra = this.menu.txtCodigoBarraFacturacion.getText();
		if (!this.codigoBarra.equals("")) {
			facturacionModel.getProductoVender(this.codigoBarra);
			if (Procedures.response) {
				this.mostrarProductosVender("");
				this.modelo.addRow(this.facturacionModel.getProducto);
				this.sumarImportes();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Escriba un codigo de barras.");
		}

	}

	public void sumarImportes() {
		int filas = this.menu.tblFacturacion.getRowCount();
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
					int producto = Integer.parseInt(
						this.menu.tblProductosFacturacion.getValueAt(this.filaseleccionada, 0).toString()
					);
					this.cantidad = Float.parseFloat(this.spinner.getValue().toString());
					this.facturacionModel.getProductoVender(producto, cantidad);
					if (Procedures.response) {
						this.modelo.addRow(this.facturacionModel.getProducto);
						this.sumarImportes();
						this.mostrarProductosVender("");
						this.menu.txtBuscarProductoFacturacion.requestFocus();
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
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProductoFacturacion) {
			this.mostrarProductosVender(this.menu.txtBuscarProductoFacturacion.getText());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.tblProductosFacturacion) {
			if (e.getClickCount() == 1) {
				this.agregarDesdeVentana();
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
