/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class JefeEquipoJPA
 */
@Entity
//@Table(name="teamWorkPlanner.jefeEquipo")
public class JefeEquipoJPA extends PersonalAlmacenJPA {
	private Turno turno;
	/**
	 * Class constructor methods
	 */
	public JefeEquipoJPA() {
		super();
	}
	
	public JefeEquipoJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta,Turno turno,SeccionAlmacenJPA seccion) {
		super(id, nif, nombre, apellidos, password, email, alta,seccion);
		this.turno=turno;
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


	
}
