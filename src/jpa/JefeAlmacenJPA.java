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
 * JPA Class JefeAlmacenJPA
 */
@Entity
//@Table(name="teamWorkPlanner.jefeAlmacen")
public class JefeAlmacenJPA extends PersonalJPA {
	
	
	/**
	 * Class constructor methods
	 */
	public JefeAlmacenJPA() {
		super();
	}
	public JefeAlmacenJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta) {
		super(id, nif, nombre, apellidos, password, email, alta);
	}	
	/**
	 * Methods get/set persistent relationships
	 */

	
}
