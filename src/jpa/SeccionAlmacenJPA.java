/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class SeccionAlmacenJPA
 */
@Entity
@Table(name="teamWorkPlanner.seccionAlmacen")
public class SeccionAlmacenJPA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String descripcion;
	private Turno turno;
	private Collection<OperarioJPA> operarios; 
	private Collection<JefeEquipoJPA> jefesEquipo; 
	private Collection<EncargadoJPA> encargados; 
	private Collection<CalendarioSeccionJPA> calendariosSeccion;
	private Collection<InstalacionJPA> instalaciones; 

	
	/**
	 * Class constructor methods
	 */
	public SeccionAlmacenJPA() {
		super();
	}
	
	public SeccionAlmacenJPA(int id,String nombre, String descripcion,Turno turno) {
		this.id = id;
		this.nombre=nombre;
		this.descripcion = descripcion;
		this.turno=turno;
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
		this.nombre=nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	/**
	 * Methods get/set persistent relationships
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="seccionAlmacen_id")
	public Collection<OperarioJPA> getOperarios() {
		return operarios;
	}	
	public void setOperarios(Collection<OperarioJPA> operarios) {
 		this.operarios = operarios; 
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="seccionAlmacen_id")
	public Collection<JefeEquipoJPA> getJefesEquipo() {
		return jefesEquipo;
	}
	public void setJefesEquipo(Collection<JefeEquipoJPA> jefesEquipo) {
 		this.jefesEquipo = jefesEquipo; 
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="seccionAlmacen_id")
	public Collection<EncargadoJPA> getEncargados() {
		return encargados;
	}	
	public void setEncargados(Collection<EncargadoJPA> encargados) {
 		this.encargados = encargados; 
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="seccionAlmacen_id")
	public Collection<CalendarioSeccionJPA> getCalendariosSeccion() {
		return calendariosSeccion;
	}	
	public void setCalendariosSeccion(Collection<CalendarioSeccionJPA> calendariosSeccion) {
 		this.calendariosSeccion = calendariosSeccion; 
	}	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="seccionAlmacen_id")
	public Collection<InstalacionJPA> getInstalaciones() {
		return instalaciones;
	}	
	public void setInstalaciones(Collection<InstalacionJPA> instalaciones) {
 		this.instalaciones = instalaciones; 
	}	

}
