/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ReportesModel extends Conexion {

	private float ventasDiariasContado,
		ventasDiariasCredito,
		ingresoDiarioEfectivo,
		salidaDiarioEfectivo,
		pagosDiarios,
		aperturaCaja;
	private Date fechaInicio, fechaFinal;
	public DefaultTableModel tableModel;

	PreparedStatement pst;
	ResultSet rs;
	Connection cn;
	String consulta;

	public float getVentasDiariasContado() {
		return ventasDiariasContado;
	}

	public void setVentasDiariasContado(float ventasDiariasContado) {
		this.ventasDiariasContado = ventasDiariasContado;
	}

	public float getVentasDiariasCredito() {
		return ventasDiariasCredito;
	}

	public void setVentasDiariasCredito(float ventasDiariasCredito) {
		this.ventasDiariasCredito = ventasDiariasCredito;
	}

	public float getIngresoDiarioEfectivo() {
		return ingresoDiarioEfectivo;
	}

	public void setIngresoDiarioEfectivo(float ingresoDiarioEfectivo) {
		this.ingresoDiarioEfectivo = ingresoDiarioEfectivo;
	}

	public float getSalidaDiarioEfectivo() {
		return salidaDiarioEfectivo;
	}

	public void setSalidaDiarioEfectivo(float salidaDiarioEfectivo) {
		this.salidaDiarioEfectivo = salidaDiarioEfectivo;
	}

	public float getPagosDiarios() {
		return pagosDiarios;
	}

	public void setPagosDiarios(float pagosDiarios) {
		this.pagosDiarios = pagosDiarios;
	}

	public float getAperturaCaja() {
		return aperturaCaja;
	}

	public void setAperturaCaja(float aperturaCaja) {
		this.aperturaCaja = aperturaCaja;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public void generarReporteDiario() {
		this.consulta = "CALL reportes(?,?)";
		this.cn = conexion();
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setDate(1, this.fechaInicio);
			this.pst.setDate(2, this.fechaFinal);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.ventasDiariasContado = this.rs.getFloat("ventasContado");
				this.ventasDiariasCredito = this.rs.getFloat("ventasCredito");
				this.pagosDiarios = this.rs.getFloat("pagos");
				this.salidaDiarioEfectivo = this.rs.getFloat("salidaEfectivo");
				this.ingresoDiarioEfectivo = this.rs.getFloat("entradaEfectivo");
				this.aperturaCaja = this.rs.getFloat("aperturaCaja");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ReportesModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getFacturas() {
		this.consulta = "select f.id AS numeroFactura,dg.id as Ndato,dg.fecha,dg.comprador,tv.tipoventa,(select sum(d.importe) from detalles as d where dg.id=d.datos) as total from datosGenerales as dg inner join factura AS f on(dg.id=f.datos) inner join tipoVenta AS tv on(dg.tipoventa=tv.id) WHERE DATE(dg.fecha) = ?";
		this.cn = conexion();
		String[] titulos = {"N. FACTURA", "FECHA", "COMPRADOR", "TIPO VENTA", "TOTAL"};
		String[] data = new String[5];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setDate(1, this.fechaInicio);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				data[0] = this.rs.getString("numeroFactura");
				data[1] = this.rs.getString("fecha");
				data[2] = this.rs.getString("comprador");
				data[3] = this.rs.getString("tipoventa");
				data[4] = this.rs.getString("total");
				this.tableModel.addRow(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ReportesModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getComprobantes() {
		this.consulta = "select c.id AS numeroComprobante,dg.id as Ndato,dg.fecha,dg.comprador,tv.tipoventa,(select sum(d.importe) from detalles as d where dg.id=d.datos) as total from datosGenerales as dg inner join comprobante AS c on(dg.id=c.datos) inner join tipoVenta AS tv on(dg.tipoventa=tv.id) WHERE DATE(dg.fecha) = ?";
		this.cn = conexion();
		String[] titulos = {"N. FACTURA", "FECHA", "COMPRADOR", "TIPO VENTA", "TOTAL"};
		String[] data = new String[5];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setDate(1, this.fechaInicio);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				data[0] = this.rs.getString("numeroComprobante");
				data[1] = this.rs.getString("fecha");
				data[2] = this.rs.getString("comprador");
				data[3] = this.rs.getString("tipoventa");
				data[4] = this.rs.getString("total");
				this.tableModel.addRow(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ReportesModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
