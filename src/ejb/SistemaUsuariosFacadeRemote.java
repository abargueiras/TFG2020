/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.util.Collection;
import javax.ejb.Remote;
import jpa.Turno;
import jpa.CalendarioSeccionJPA;
import jpa.EncargadoJPA;
import jpa.JefeAlmacenJPA;
import jpa.JefeEquipoJPA;
import jpa.OperarioJPA;
import jpa.PersonalJPA;
import jpa.RrhhJPA;
import jpa.SeccionAlmacenJPA;

/**
 * EJB sesión interfaz remota
 */
@Remote
public interface SistemaUsuariosFacadeRemote {
	/**
	 * Remotely invoked method.
	 */
	 
	public boolean login(int id,String password);
	public void logout();
	public boolean cambioPassword (PersonalJPA pers, String newPassword);
    public String crearPersonal (int id, String nif ,String nombre, String apellidos, String password, String email, String nuevoRol , SeccionAlmacenJPA seccion, Turno turno);
	public boolean eliminarPersonal(int id);
	public boolean updatePersonal(int id, String nif, String nombre, String apellidos, String password, String email,
			String nuevoRol, SeccionAlmacenJPA seccion, Turno turno);   
	public int getPersonalNombr(String nomBuscado);
	public PersonalJPA getUsuario(int id);   
	public int nuevoIdPersonal();
	public String getTipoPersonal(int id);
	public Collection<PersonalJPA> getPersonalConNombre(String nomBuscado);
	public OperarioJPA getOperario(int id);
	public JefeEquipoJPA getJefeEquipo(int id);
	public EncargadoJPA getEncargado(int id);
	public JefeAlmacenJPA getJefeAlmacen(int id);
	public RrhhJPA getRrhh(int id);
	public Collection<OperarioJPA> getOperarios();
	public Collection<OperarioJPA> getOperarioConNombre(String nomBuscado);
	public Collection <OperarioJPA> getOperarios(CalendarioSeccionJPA sec);
	public Collection<OperarioJPA> getOperarioSeccion(SeccionAlmacenJPA dataSeccion);
	Collection<PersonalJPA> getPersonal();
    public boolean crearSeccion (String nombre, String descripcion,Turno turno);
    public boolean eliminarSeccion (int id);
    public SeccionAlmacenJPA getSeccion (int id);
    public int getSeccionNombr(String nomBuscado);
	public SeccionAlmacenJPA getSeccionPorNombre(String res);   
	public SeccionAlmacenJPA getSeccionEmpleado(int id);   
    public Collection<SeccionAlmacenJPA> getSecciones ();
	public boolean updateSeccion(int id, String nombre, String descripcion);
	public Turno getTurnoEmpleado(int id, String rol);
	public boolean existeSeccionTurno(String elegirSeccion, String elegirTurno);
	public boolean desasignarInstalacion(SeccionAlmacenJPA dataSeccion, int id);
	}


 