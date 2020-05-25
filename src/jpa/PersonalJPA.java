/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package jpa;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

/**
 * JPA Class DoctorJPA
 */
@Entity
@Table(name="teamWorkPlanner.personal")
public class PersonalJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String nif;
	private String nombre;
	private String apellidos;
	private String password;
	private String email;
	private Date fechaAlta;
	private Date fechaBaja;
	/**
	 * Class constructor methods
	 */
	public PersonalJPA() {
		super();
	}
	public PersonalJPA(int id, String nif, String nombre, String apellidos, String password, String email, Date alta) {		
		this.id = id;
		this.nif=nif;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.password = password;
		this.email = email;
		this.fechaAlta=alta;
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
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getnombre() {
		return nombre;
	}
	public void setnombre(String nombre) {
		this.nombre = nombre;
	}
	public String getapellidos() {
		return apellidos;
	}
	public void setapellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fecha) {
		this.fechaAlta = fecha;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fecha) {
		this.fechaBaja = fecha;
	}

	/**
	 * Methods get/set persistent relationships
	 */
}
