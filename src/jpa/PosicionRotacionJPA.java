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
 * JPA Class PuestoJPA
 */
@Entity
@Table(name="teamWorkPlanner.posicion")
public class PosicionRotacionJPA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private Date fecha;
	private OperarioJPA operario;
	private PuestoJPA puesto; 
	private RotacionJPA rotacion;
	/**
	 * Class constructor methods
	 */
	public PosicionRotacionJPA() {
		super();
	}
	
	public PosicionRotacionJPA(int id,Date fecha, OperarioJPA operario,PuestoJPA puesto) {
		this.id = id;
		this.setFecha(fecha);
		this.setOperario(operario);
		this.setPuesto(puesto);
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	@JoinColumn (name="operario_id")
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

	@ManyToOne
	@JoinColumn (name="rotacion_id")
	public RotacionJPA getRotacion() {
		return rotacion;
	}
	public void setRotacion(RotacionJPA rotacion) {
		this.rotacion = rotacion;
	}

}
