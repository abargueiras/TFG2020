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
 * JPA Class DiaFestivoJPA
 */
@Entity
@Table(name="teamWorkPlanner.diaFestivo")
public class DiaFestivoJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date id;
	private String festividad;
	private CalendarioEmpresaJPA calendarioEmpresa;

	/**
	 * Class constructor methods
	 */
	public DiaFestivoJPA() {
		super();
	}
	

	public DiaFestivoJPA(Date id, String festividad) {
		super();
		this.id = id;
		this.festividad=festividad;
	}


	/**
	 *  Methods get/set the fields of database
	 */
	@Id
	public Date getId() {
		return id;
	}


	public void setId(Date id) {
		this.id = id;
	}


	public String getFestividad() {
		return festividad;
	}


	public void setFestividad(String festividad) {
		this.festividad = festividad;
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
