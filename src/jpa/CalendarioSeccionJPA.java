/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */

package jpa;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class CalendarioSeccion
 */
@Entity
@Table(name="teamWorkPlanner.calendarioSeccion")
public class CalendarioSeccionJPA implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private Turno turno;
	private int operariosTurnoVacaciones;
    private SeccionAlmacenJPA seccion;
	private Collection<CalendarioPersonalJPA> calendariosPersonales;
	private CalendarioEmpresaJPA calendarioEmpresa;
	
	/**
	 * Class constructor methods
	 */
	public CalendarioSeccionJPA() {
		super();
	}
	

	public CalendarioSeccionJPA(int id,Turno turno, int operariosTurnoVacaciones) {
		super();
		this.id = id;
		this.turno=turno;
		this.operariosTurnoVacaciones=operariosTurnoVacaciones;
	}


	/**
	 *  Methods get/set the fields of database
	 */
	@Id
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public Turno getTurno() {
		return turno;
	}


	public void setTurno(Turno id) {
		this.turno = id;
	}
	
	public int getOperariosTurnoVacaciones() {
		return operariosTurnoVacaciones;
	}


	public void setOperariosTurnoVacaciones(int operariosTurnoVacaciones) {
		this.operariosTurnoVacaciones = operariosTurnoVacaciones;
	}




			
	/**
	 * Methods get/set persistent relationships
	 */
	@ManyToOne
	@JoinColumn (name="seccionAlmacen_id")
	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}
	public void setSeccion(SeccionAlmacenJPA seccion) {
		this.seccion = seccion;
	}

	@ManyToOne
	@JoinColumn (name="calendarioEmpresa_id")
	public CalendarioEmpresaJPA getCalendarioEmpresa() {
		return calendarioEmpresa;
	}
	public void setCalendarioEmpresa(CalendarioEmpresaJPA calendarioEmpresa) {
		this.calendarioEmpresa = calendarioEmpresa;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinColumn (name="calendarioSeccion_id")
	public Collection<CalendarioPersonalJPA> getCalendariosPersonales() {
		return calendariosPersonales;
	}	
	public void setCalendariosPersonales(Collection<CalendarioPersonalJPA> calendariosPersonales) {
 		this.calendariosPersonales = calendariosPersonales;
	}
}