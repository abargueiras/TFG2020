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
import jpa.InstalacionJPA;
import jpa.PuestoJPA;
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "instalacion")
@ViewScoped
public class instalacionBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaInstalacionesFacadeRemote sistInstalacionRemote;
	protected InstalacionJPA dataInstalacion;
	private int id=0;
	private String nombre="";
	private String descripcion="";
	private Collection<PuestoJPA> puestos; 
    private InstalacionJPA instalacion;
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public instalacionBean() throws Exception 
	{
		setDataInstalacion();
	}

	public int getId() throws Exception {
		setDataInstalacion();
		return this.id;
		}
	
	public void setId(int id) throws Exception {
		this.id = id;
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
	
	public Collection<PuestoJPA> getPuestos() {
		return puestos;
	}

	public void setPuestos(Collection<PuestoJPA> puestos) {
		this.puestos = puestos;
	}

	public InstalacionJPA getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(InstalacionJPA inst) throws Exception {	
		this.instalacion = inst;
		setId(instalacion.getId());
		setNombre(instalacion.getNombre());
		setDescripcion(instalacion.getDescripcion());
	}
	public InstalacionJPA getDataInstalacion()
	{
		return dataInstalacion;
	}	
	public void setDataInstalacion() throws Exception
	{	
		dataInstalacion = new InstalacionJPA(id,nombre,descripcion );
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
	public void crearInstalacion() throws Exception
	{		    
		    boolean res=false;
        	setDataInstalacion();
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
    		res= sistInstalacionRemote.crearInstalacion(dataInstalacion.getNombre(), dataInstalacion.getDescripcion());
	        	if(res) {
	        		resultadoCor = " Sección creada ";
	        		resultadoInc = "";	        		
	        		}
        		else {
        			resultadoInc = " La sección existe ";
        			resultadoCor = "";
        			}
	        	}
	public void verInstalacion() throws Exception
	{
		try {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		setInstalacion(sistInstalacionRemote.getInstalacion(getId()));
		setId(instalacion.getId());
		setNombre(instalacion.getNombre());
		setDescripcion(instalacion.getDescripcion());
		}catch (Exception e) {}
        }

	public void eliminarInstalacion() throws Exception
	{
		String a=" ";
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		setDataInstalacion();
		a=a+ "  dataIns:" + dataInstalacion.getNombre();
		a=a+ "  Ins:"+instalacion.getNombre();
		a=a + "   nombre:"+ nombre;
		
		res= sistInstalacionRemote.eliminarInstalacion(getId());
		if(res) {
    		resultadoCor = " Sección eliminada ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " Sección con personal asignado -> ¡ERROR AL ELIMINAR! "+a;
			resultadoCor = "";
			}
    	setDataInstalacion();
        }
	
	public void modificarInstalacion() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		res= sistInstalacionRemote.updateInstalacion(id, instalacion.getNombre(), instalacion.getDescripcion());
		if(res) {
    		resultadoCor = " Instalación modificada ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " Error de modificacion ";
			resultadoCor = "";
			}
    	setDataInstalacion();
	}


	
	
}
