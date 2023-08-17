/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HOLA
 */
public class CmbAvales {
	private int id;
	private String nombres, apellidos;

	public	CmbAvales(int id, String nombres, String apellidos){
		this.id = id;	
		this.nombres = nombres;
		this.apellidos = apellidos;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombres;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		final CmbAvales other = (CmbAvales) obj;
		if(this.id != other.id){
			return false;	
		}	
		return true;
	}
	
	@Override
	public String toString(){
		return this.nombres + ' ' + this.apellidos;
	}
}
