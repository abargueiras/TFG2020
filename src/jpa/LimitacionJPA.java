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
 * JPA Class LimitacionJPA
 */
@Entity
@Table(name="teamWorkPlanner.limitacion")
public class LimitacionJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private PuestoJPA puesto;
	private String motivo;
	private OperarioJPA operario;

	
	/**
	 * Class constructor methods
	 */
	public LimitacionJPA() {
		super();
	}
	

	public LimitacionJPA(int id, PuestoJPA puesto, String motivo) {
		super();
		this.id = id;
		this.puesto=puesto;
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
    @JoinColumn(name = "operario_id")
	public OperarioJPA getOperario() {
		return operario;
	}
	public void setOperario(OperarioJPA operario) {
		this.operario = operario;
	}
	
	
	@ManyToOne
	@JoinColumn (name="puesto_id")
	public PuestoJPA getPuesto() {
		return puesto;
	}	
	public void setPuesto(PuestoJPA puesto) {
 		this.puesto = puesto; 
	}
	
}
