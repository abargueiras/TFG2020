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
 * JPA Class PersonalAlmacenJPA
 */
@Entity
//@Table(name="teamWorkPlanner.personalAlmacen")
public abstract class PersonalAlmacenJPA extends PersonalJPA {
	private SeccionAlmacenJPA seccion;

	/**
	 * Class constructor methods
	 */
	public PersonalAlmacenJPA() {
		super();
	}
	
	public PersonalAlmacenJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta,SeccionAlmacenJPA seccion) {
		super(id, nif, nombre, apellidos, password, email, alta);
        this.seccion=seccion;
	}
	
	/**
	 * Métodos get/set relaciones persistentes
	 * El personal de almacén pertenece a una sección del almacén.
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
