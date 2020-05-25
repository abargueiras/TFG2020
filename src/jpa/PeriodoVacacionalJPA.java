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
 * JPA Class PeriodoVacacionalJPA
 */
@Entity
@Table(name="teamWorkPlanner.periodoVacacional")
public class PeriodoVacacionalJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private TipoVacaciones tipo;
	private CalendarioPersonalJPA calendarioPersonal;
	private Date primerDia;
	private Date ultimoDia;

	/**
	 * Class constructor methods
	 */
	public PeriodoVacacionalJPA() {
		super();
	}
	

	public PeriodoVacacionalJPA(int id, TipoVacaciones tipo, Date primerDia, Date ultimoDia) {
		super();
		this.id = id;
		this.tipo=tipo;
		this.primerDia=primerDia;
		this.ultimoDia=ultimoDia;
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


	public TipoVacaciones getTipo() {
		return tipo;
	}


	public void setTipo(TipoVacaciones tipo) {
		this.tipo = tipo;
	}


	public Date getPrimerDia() {
		return primerDia;
	}


	public void setPrimerDia(Date primerDia) {
		this.primerDia = primerDia;
	}

	public Date getUltimoDia() {
		return ultimoDia;
	}


	public void setUltimoDia(Date ultimoDia) {
		this.ultimoDia = ultimoDia;
	}

		
	/**
	 * Methods get/set persistent relationships
	 */
	@ManyToOne
	@JoinColumn (name="calendarioPersonal_id")
	public CalendarioPersonalJPA getCalendarioPersonal() {
		return calendarioPersonal;
	}
	public void setCalendarioPersonal(CalendarioPersonalJPA calendarioPersonal) {
		this.calendarioPersonal = calendarioPersonal;
	}

}
