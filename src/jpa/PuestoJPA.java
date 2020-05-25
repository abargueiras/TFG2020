/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class PuestoJPA
 */
@Entity
@Table(name="teamWorkPlanner.puesto")
public class PuestoJPA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String descripcion;
	private int numOperarios;
	private InstalacionJPA instalacion; 
	private boolean requerido;
	/**
	 * Class constructor methods
	 */
	public PuestoJPA() {
		super();
	}
	
	public PuestoJPA(int id, String nombre, String descripcion, int numOperarios, boolean requerido) {
		this.id = id;
		this.nombre=nombre;
		this.descripcion = descripcion;
		this.numOperarios=numOperarios;
		this.requerido=requerido;
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getNumOperarios() {
		return numOperarios;
	}

	public void setNumOperarios(int numOperarios) {
		this.numOperarios = numOperarios;
	}
	public boolean getRequerido() {
		return requerido;
	}

	public void setRequerido(boolean esPreciso) {
		this.requerido = esPreciso;
	}
	/**
	 * Methods get/set persistent relationships
	 */
	@ManyToOne
	@JoinColumn (name="instalacion_id")
	public InstalacionJPA getInstalacion() {
		return instalacion;
	}
	public void setInstalacion(InstalacionJPA instalacion) {
		this.instalacion = instalacion;
	}
}
