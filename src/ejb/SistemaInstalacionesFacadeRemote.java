/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.util.Collection;
import javax.ejb.Remote;
import jpa.InstalacionJPA;
import jpa.PuestoJPA;
import jpa.SeccionAlmacenJPA;

/**
 * EJB sesión interfaz remota
 */
@Remote
public interface SistemaInstalacionesFacadeRemote {

	  /**
	   * Remotely invoked method.
	   */

	boolean crearInstalacion(String nombre, String descripcion);
	boolean eliminarInstalacion(InstalacionJPA dataInstalacion);
	boolean eliminarInstalacion(int id);
	boolean updateInstalacion(int id, String nombre, String descripcion);	
	void modificarInstalaciones(SeccionAlmacenJPA dataSeccion, Collection<InstalacionJPA> instalacionesLista);
	InstalacionJPA getInstalacion(int id);
	Collection<InstalacionJPA> getInstalaciones();
	InstalacionJPA getInstalacionNombre(String nomBuscado);
	Collection<InstalacionJPA> getInstalaciones(SeccionAlmacenJPA dataSeccion);
	Collection<PuestoJPA> getPuestos(InstalacionJPA inst);
	boolean crearPuesto(String nombre, String descripcion, int numOperarios, boolean requerido, InstalacionJPA instalacion);
	boolean eliminarPuesto(PuestoJPA pos);
	boolean eliminarPuesto(int id);	
	boolean updatePuesto(int id, String nombre, String descripcion, int numOperarios, boolean requerido, InstalacionJPA instalacionJPA);
	PuestoJPA getPuesto(int id);
	Collection<PuestoJPA> getPosiciones();  
	Collection<PuestoJPA> getPosicionNombre(String nomBuscado);


}
