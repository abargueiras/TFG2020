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
 * JPA Class CalendarioPersonal
 */
@Entity
@Table(name="teamWorkPlanner.calendarioPersonal")
public class CalendarioPersonalJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private OperarioJPA operario;	
	private CalendarioSeccionJPA calendarioSeccion;
	private Collection<DiaPersonalJPA> diasPersonales;
	private Collection<PeriodoVacacionalJPA> periodoVacacional;

	/**
	 * Class constructor methods
	 */
	public CalendarioPersonalJPA() {
		super();
	}
	

	public CalendarioPersonalJPA(int id) {
		super();
		this.id = id;
	}
	
	public CalendarioPersonalJPA(int id,OperarioJPA op,CalendarioSeccionJPA cal) {
		super();
		this.id = id;
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

			
	/**
	 * Methods get/set persistent relationships
	 */
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn (name="operario_id")
	public OperarioJPA getOperario() {
		return operario;
	}
	public void setOperario(OperarioJPA operario) {
		this.operario = operario;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="calendarioPersonal_id")
	public Collection<DiaPersonalJPA> getDiasPersonales() {
		return diasPersonales;
	}	
	public void setDiasPersonales(Collection<DiaPersonalJPA> diasPersonales) {
 		this.diasPersonales = diasPersonales; 
	}
	
	@ManyToOne
	@JoinColumn (name="calendarioSeccion_id")
	public CalendarioSeccionJPA getCalendarioSeccion() {
		return calendarioSeccion;
	}
	public void setCalendarioSeccion(CalendarioSeccionJPA calendarioSeccion) {
		this.calendarioSeccion = calendarioSeccion;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn (name="calendarioPersonal_id")
	public Collection<PeriodoVacacionalJPA> getPeriodoVacacional() {
		return periodoVacacional;
	}	
	public void setPeriodoVacacional(Collection<PeriodoVacacionalJPA> periodoVacacional) {
 		this.periodoVacacional = periodoVacacional;
	}
}
