/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class OperarioJPA
 */
@Entity
//@Table(name="teamWorkPlanner.operario")
public class OperarioJPA extends PersonalAlmacenJPA {
	
	private Turno turno;
	private Collection<LimitacionJPA> limitaciones;
	private Collection<CalendarioPersonalJPA> calendarios;
	
	/**
	 * Método constructor de la subclase
	 */
	public OperarioJPA() {
		super();
	}
	
	public OperarioJPA(Turno turno) {
		this.turno=turno;
	}
	
	public OperarioJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta,Turno turno,SeccionAlmacenJPA seccion) {
		super(id, nif, nombre, apellidos, password, email, alta,seccion);
		this.turno=turno;
	}
	
	/**
	 *  Métodos get/set atributos subclase
	 *  Turno: turno de trabajo de un operario (Clase turno de trabajo: mañana,tarde,noche)
	 */
	
	public Turno getTurno() {
		return turno;
	}
	
	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	
	/**
	 * Métodos get/set relaciones persistentes
	 * Un operario pertenece a una sección del almacén.
	 * Un operario puede estar limitado y no poder trabajar en determinados puestos en instalaciones diferentes.
	 */
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="operario_id")
	public Collection<LimitacionJPA> getLimitaciones() {
		return limitaciones;
	}	
	public void setLimitaciones(Collection<LimitacionJPA> limitaciones) {
 		this.limitaciones = limitaciones; 
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="operario_id")
	public Collection<CalendarioPersonalJPA> getCalendarios() {
		return calendarios;
	}	
	public void setCalendarios(Collection<CalendarioPersonalJPA> calendarios) {
 		this.calendarios = calendarios; 
	}

}
