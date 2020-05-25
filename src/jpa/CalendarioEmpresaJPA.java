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
 * JPA Class CalendarioEmpresa
 */
@Entity
@Table(name="teamWorkPlanner.calendarioEmpresa")
public class CalendarioEmpresaJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Date limitePropuestaVacaciones;
	private boolean propuestaCerrada;
	private int numDiasPersonales;
	private Collection<CalendarioSeccionJPA> calendariosSecciones;
	private Collection<DiaFestivoJPA> diasFestivos;
	private Collection<TurnoVacacionalJPA> turnosVacacionales;
	/**
	 * Class constructor methods
	 */
	public CalendarioEmpresaJPA() {
		super();
	}
	public CalendarioEmpresaJPA(int id) {
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

	public Date getlimitePropuestaVacaciones() {
		return limitePropuestaVacaciones;
	}

	public void setlimitePropuestaVacaciones(Date limitePropuestaVacaciones) {
		this.limitePropuestaVacaciones = limitePropuestaVacaciones;
	}
	public boolean isPropuestaCerrada() {
		return propuestaCerrada;
	}
	public void setPropuestaCerrada(boolean propuestaCerrada) {
		this.propuestaCerrada = propuestaCerrada;
	}		
	/**
	 * Methods get/set persistent relationships
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="calendarioEmpresa_id")
	public Collection<CalendarioSeccionJPA> getCalendariosSecciones() {
		return calendariosSecciones;
	}	
	public void setCalendariosSecciones(Collection<CalendarioSeccionJPA> calendariosSecciones) {
 		this.calendariosSecciones = calendariosSecciones;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="calendarioEmpresa_id")
	public Collection<DiaFestivoJPA> getDiasFestivos() {
		return diasFestivos;
	}	
	public void setDiasFestivos(Collection<DiaFestivoJPA> diasFestivos) {
 		this.diasFestivos = diasFestivos;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn (name="calendarioEmpresa_id")
	public Collection<TurnoVacacionalJPA> getTurnosVacacionales() {
		return turnosVacacionales;
	}	
	public void setTurnosVacacionales(Collection<TurnoVacacionalJPA> turnosVacacionales) {
 		this.turnosVacacionales = turnosVacacionales;
	}
	public int getNumDiasPersonales() {
		return numDiasPersonales;
	}
	public void setNumDiasPersonales(int numDiasPersonales) {
		this.numDiasPersonales = numDiasPersonales;
	}

}
