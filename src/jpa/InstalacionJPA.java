/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class InstalacionJPA
 */
@Entity
@Table(name="teamWorkPlanner.instalacion")
public class InstalacionJPA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String descripcion;
	private Collection<PuestoJPA> puestos; 
    private SeccionAlmacenJPA seccion;
	
	/**
	 * Class constructor methods
	 */
	public InstalacionJPA() {
		super();
	}
	
	public InstalacionJPA(int id, String nombre, String descripcion) {
		this.id = id;
		this.nombre=nombre;
		this.descripcion = descripcion;
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(unique = true)
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
	
	
	/**
	 * Methods get/set persistent relationships
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinColumn (name="instalacion_id")
	public Collection<PuestoJPA> getPuestos() {
		return puestos;
	}	
	public void setPuestos(Collection<PuestoJPA> puestos) {
 		this.puestos = puestos; 
	}
	
	@ManyToOne
	@JoinColumn (name="seccionAlmacen_id")
	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}
	public void setSeccion(SeccionAlmacenJPA seccion) {
		this.seccion = seccion;
	}
}
