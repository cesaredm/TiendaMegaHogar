/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import model.ReportesModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ReportesController implements ActionListener, MouseListener {

	private static ReportesController instancia = null;
	ReportesModel reportesModel;
	PrincipalView menu;
	java.sql.Date fechaIncio, fechaFinal;
	DefaultTableModel tableModel;
	String[] titulos = {"CUENTA", "MONTO"};
	String[][] data;
	DecimalFormat formato;
	boolean bandera;//para saber si se esta devolviendo un comprobante o desde una facura

	private ReportesController(PrincipalView menu, ReportesModel reportesModel) {
		this.reportesModel = reportesModel;
		this.menu = menu;
		this.formato = new DecimalFormat("###,###,###,###,###,#00.00");
		this.menu.jcFechaIncioReporte.setDate(new Date());
		this.menu.jcFechaFinalReporte.setDate(new Date());
		this.menu.btnActualizarReporteDiario.addActionListener(this);
		this.menu.btnDevolverFactura.addActionListener(this);
		this.menu.btnMostrarFacturasEmitidas.addActionListener(this);
		this.menu.tblReporteFacturas.addMouseListener(this);
		this.menu.tblReportesComprobantes.addMouseListener(this);
	}

	static public void createInstanceController(PrincipalView menu, ReportesModel reportesModel) {
		if (instancia == null) {
			instancia = new ReportesController(menu, reportesModel);
		}
	}

	public void generarReporte() {
		EstiloTablas.estilosCabeceras(this.menu.tblReporteDiario);
		this.fechaIncio = new java.sql.Date(this.menu.jcFechaIncioReporte.getDate().getTime());
		this.fechaFinal = new java.sql.Date(this.menu.jcFechaFinalReporte.getDate().getTime());
		this.reportesModel.setFechaInicio(fechaIncio);
		this.reportesModel.setFechaFinal(fechaFinal);
		this.reportesModel.generarReporteDiario();
		String[][] data = {
			{"Apertura de caja", this.formato.format(this.reportesModel.getAperturaCaja())},
			{"Ventas de contado", this.formato.format(this.reportesModel.getVentasDiariasContado())},
			{"Ventas de credito", this.formato.format(this.reportesModel.getVentasDiariasCredito())},
			{"Pagos", this.formato.format(this.reportesModel.getPagosDiarios())},
			{"Ingresos de efectivo", this.formato.format(this.reportesModel.getIngresoDiarioEfectivo())},
			{"Egresos de efectivo", this.formato.format(this.reportesModel.getSalidaDiarioEfectivo())}
		};
		this.tableModel = new DefaultTableModel(data, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.menu.tblReporteDiario.setModel(this.tableModel);
	}

	public void mostrarFacturasEmitidas() {
		EstiloTablas.estilosCabeceras(this.menu.tblReporteFacturas);
		this.fechaIncio = new java.sql.Date(this.menu.jcFechaIncioReporte.getDate().getTime());
		this.reportesModel.setFechaInicio(fechaIncio);
		this.reportesModel.getFacturas();
		this.menu.tblReporteFacturas.setModel(this.reportesModel.tableModel);

	}

	public void mostrarComprobantesEmitidas() {
		EstiloTablas.estilosCabeceras(this.menu.tblReportesComprobantes);
		this.fechaIncio = new java.sql.Date(this.menu.jcFechaIncioReporte.getDate().getTime());
		this.reportesModel.setFechaInicio(fechaIncio);
		this.reportesModel.getComprobantes();
		this.menu.tblReportesComprobantes.setModel(this.reportesModel.tableModel);

	}

	public void devolverDesdeFactura() {
		float cant = 0,
			importe = 0,
			totalDev = 0;
		int detalle = 0,
			filaseleccionada = this.menu.tblDetalles.getSelectedRow();
		if (filaseleccionada != -1) {
			SpinnerNumberModel modelSpinerDev = new SpinnerNumberModel();
			modelSpinerDev.setMinimum(0);
			modelSpinerDev.setValue(0);
			modelSpinerDev.setStepSize(0.01);
			JSpinner spinerDev = new JSpinner(modelSpinerDev);
			int confirmacion = JOptionPane.showConfirmDialog(null, spinerDev, "Cantidad", JOptionPane.OK_OPTION);
			if (confirmacion == JOptionPane.OK_OPTION) {
				cant = Float.parseFloat(spinerDev.getValue().toString());
				detalle = Integer.parseInt(this.menu.tblDetalles.getValueAt(filaseleccionada, 0).toString());
				importe = Float.parseFloat(this.menu.tblDetalles.getValueAt(filaseleccionada, 3).toString()) * cant;
				this.reportesModel.devolver(detalle, importe, cant);
				this.reportesModel.getDetallesFacturas(this.reportesModel.factura, bandera ? "factura" : "comprobante");
				this.menu.tblDetalles.setModel(this.reportesModel.tableModel);
				if (bandera) {
					this.mostrarFacturasEmitidas();
				} else {
					this.mostrarComprobantesEmitidas();
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnActualizarReporteDiario": {
				this.generarReporte();
			}
			break;
			case "btnMostrarFacturasEmitidas": {
				this.mostrarFacturasEmitidas();
				this.mostrarComprobantesEmitidas();
			}
			break;
			case "btnDevolverFactura": {
				this.devolverDesdeFactura();
			}
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.tblReporteFacturas) {
			if (e.getClickCount() == 2) {
				int filaseleccionada = this.menu.tblReporteFacturas.getSelectedRow();
				int id = Integer.parseInt(this.menu.tblReporteFacturas.getValueAt(filaseleccionada, 0).toString());
				this.reportesModel.factura = id;
				this.reportesModel.getDetallesFacturas(id, "factura");
				this.menu.tblDetalles.setModel(this.reportesModel.tableModel);
				this.menu.jdDetalles.setSize(1165, 433);
				this.menu.jdDetalles.setLocationRelativeTo(null);
				this.menu.jdDetalles.setVisible(true);
				this.bandera = true;
			}

		}
		if (e.getSource() == this.menu.tblReportesComprobantes) {
			if (e.getClickCount() == 2) {
				int filaseleccionada = this.menu.tblReportesComprobantes.getSelectedRow();
				int id = Integer.parseInt(this.menu.tblReportesComprobantes.getValueAt(filaseleccionada, 0).toString());
				this.reportesModel.factura = id;
				this.reportesModel.getDetallesFacturas(id, "comprobante");
				this.menu.tblDetalles.setModel(this.reportesModel.tableModel);
				this.menu.jdDetalles.setSize(1165, 433);
				this.menu.jdDetalles.setLocationRelativeTo(null);
				this.menu.jdDetalles.setVisible(true);
				this.bandera = false;
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
