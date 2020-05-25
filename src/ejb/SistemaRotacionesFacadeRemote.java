/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.util.Collection;
import javax.ejb.Remote;
import jpa.OperarioJPA;
import jpa.PosicionRotacionJPA;
import jpa.SeccionAlmacenJPA;
import jpa.PuestoJPA;
import jpa.RotacionJPA;
import jpa.Turno;

/**
 * EJB sesión interfaz remota
 */
@Remote
public interface SistemaRotacionesFacadeRemote {
	  /**
	   * Remotely invoked method.
	   */	  
	  public RotacionJPA crearRotacion(Turno turno, java.sql.Date valueOf, SeccionAlmacenJPA seccion);
	  public boolean eliminarRotacion(RotacionJPA rotacion);	  
	  public Collection<OperarioJPA> asignarPosicion(RotacionJPA rotacion, PuestoJPA p, Collection<OperarioJPA> operarios, java.sql.Date f);
	  public Collection<PosicionRotacionJPA> getRotacion(int id);
	  public Collection<RotacionJPA> getRotacionesSeccion(SeccionAlmacenJPA seccion);
	  public Collection<RotacionJPA> getRotacionesOperario(OperarioJPA operario);
	  public RotacionJPA getRotacion(RotacionJPA rotacion);
	  public RotacionJPA getRotacionNum(int numeroRotacion);
	  public Collection<RotacionJPA> getRotaciones();
	  public Collection<PosicionRotacionJPA> getPosicionesOperario(OperarioJPA operario);
	  public PosicionRotacionJPA getPosicionOperario(OperarioJPA operario, int i);
}
