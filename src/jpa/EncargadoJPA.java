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
 * JPA Class EncargadoJPA
 */
@Entity
//@Table(name="teamWorkPlanner.encargado")
public class EncargadoJPA extends PersonalAlmacenJPA {
	private SeccionAlmacenJPA seccion;
	
	/**
	 * Class constructor methods
	 */
	public EncargadoJPA() {
		super();
	}
	
	public EncargadoJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta, SeccionAlmacenJPA sec) {
		super(id, nif, nombre, apellidos, password, email, alta,sec);
	}
	

	
	/**
	 * Methods get/set persistent relationships
	 */

	
}