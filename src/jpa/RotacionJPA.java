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
 * JPA Class RotacionJPA
 */
@Entity
@Table(name="teamWorkPlanner.rotacion")
public class RotacionJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Turno turno;
	private Date fecha;
    private SeccionAlmacenJPA seccion;

	/**
	 * Class constructor methods
	 */
	public RotacionJPA() {
		super();
	}
	

	public RotacionJPA(int id, Turno turno, Date fecha) {
		super();
		this.id = id;
		this.turno=turno;
		this.fecha=fecha;
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


	public void setTurno(Turno turno) {
		this.turno = turno;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

}

