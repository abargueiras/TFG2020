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
 * JPA Class DiaPersonalJPA
 */
@Entity
@Table(name="teamWorkPlanner.diaPersonal")
public class DiaPersonalJPA implements Serializable {

	private static final long serialVersionUID = 1L;
	private CalendarioPersonalJPA calendarioPersonal;
	private int id;
	private Date date;
	private String motivo;


	/**
	 * Class constructor methods
	 */
	public DiaPersonalJPA() {
		super();
	}
	

	public DiaPersonalJPA(int id,Date date, String motivo) {
		super();
		this.id = id;
		this.date=date;
		this.motivo=motivo;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getMotivo() {
		return motivo;
	}


	public void setMotivo(String motivo) {
		this.motivo = motivo;
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