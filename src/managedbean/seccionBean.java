/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "seccion")
@ViewScoped
public class seccionBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	protected SeccionAlmacenJPA dataSeccion;
	protected int id = 0;
	protected String nombre= "";
	protected String descripcion = "";
	protected Turno turno = Turno.Mañana;
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public seccionBean() throws Exception 
	{
		setDataSeccion();
	}

	public int getId() {
		return this.id;
		}
	
	public void setId(int id) throws Exception {
		this.id = id;
		setDataSeccion();
	}
	
	public String getNombre() {
		return this.nombre;
		}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return this.descripcion;
		}
	
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	
	public Turno getTurno() {
		return this.turno;
		}
	
	public void setTurno (Turno turno) {
		this.turno = turno;
	}
	
	public SeccionAlmacenJPA getDataSeccion()
	{
		return dataSeccion;
	}	
	public void setDataSeccion() throws Exception
	{	
		dataSeccion = new SeccionAlmacenJPA( );
	}
	
	public String getResultadoInc() {
		return resultadoInc;
	}

	public void setResultadoInc(String resultado) {
		this.resultadoInc = resultado;
	}
	public String getResultadoCor() {
		return resultadoCor;
	}

	public void setResultadoCor(String resultado) {
		this.resultadoCor = resultado;
	}
	
	/**
	 * Method used for Facelet to be able to do the submit
	 * @return Facelet name
	 * @throws Exception
	 */
	public void crearSeccion() throws Exception
	{		    
		    boolean res=false;
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		res= sistUsuariosRemote.crearSeccion(dataSeccion.getNombre(), dataSeccion.getDescripcion(),dataSeccion.getTurno());
	        	if(res) {
	        		resultadoCor = " Sección creada ";
	        		resultadoInc = "";	        		
	        		}
        		else {
        			resultadoInc = " La sección existe ";
        			resultadoCor = "";
        			}
	        	setDataSeccion();
	        	}
	public void verSeccion() throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataSeccion= sistUsuariosRemote.getSeccion(id);
        }

	public void eliminarSeccion() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		res= sistUsuariosRemote.eliminarSeccion(id);
		if(res) {
    		resultadoCor = " Sección eliminada ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " Sección con personal asignado -> ¡ERROR AL ELIMINAR! ";
			resultadoCor = "";
			}
    	setDataSeccion();
        }
	
	public void modificarSeccion() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		res= sistUsuariosRemote.updateSeccion(id, dataSeccion.getNombre(), dataSeccion.getDescripcion());
		if(res) {
    		resultadoCor = " Sección modificada ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " Error de modificacion ";
			resultadoCor = "";
			}
    	setDataSeccion();
	}
	
	
}
