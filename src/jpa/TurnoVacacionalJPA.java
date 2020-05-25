/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */

package jpa;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class TurnoVacacionalJPA
 */
@Entity
@Table(name="teamWorkPlanner.turnoVacacional")
public class TurnoVacacionalJPA implements Serializable {

	private static final long serialVersionUID = 1L;
	private CalendarioEmpresaJPA calendarioEmpresa;
	private Date fechaInicio;
	private Date fechaFin;
	private TipoVacaciones temporada;


	/**
	 * Class constructor methods
	 */
	public TurnoVacacionalJPA() {
		super();
	}	
	public TurnoVacacionalJPA(Date fechaI) {
		super();
		this.fechaInicio=fechaI;
	}
	public TurnoVacacionalJPA(Date fechaI,Date fechaF,TipoVacaciones tem) {
		super();
		this.fechaInicio=fechaI;
		this.fechaFin=fechaF;
		this.temporada=tem;
	}


	/**
	 *  Methods get/set the fields of database
	 */
	@Id
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public TipoVacaciones getTemporada() {
		return temporada;
	}

	public void setTemporada(TipoVacaciones temporada) {
		this.temporada = temporada;
	}
	/**
	 * Methods get/set persistent relationships
	 */
	
	@ManyToOne
	@JoinColumn (name="calendarioEmpresa_id")
	public CalendarioEmpresaJPA getCalendarioEmpresa() {
		return calendarioEmpresa;
	}
	public void setCalendarioEmpresa(CalendarioEmpresaJPA calendarioEmpresa) {
		this.calendarioEmpresa = calendarioEmpresa;
	}	
	
}