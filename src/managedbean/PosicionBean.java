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
import javax.naming.NamingException;
import jpa.InstalacionJPA;
import jpa.PuestoJPA;
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "posicion")
@ViewScoped
public class PosicionBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaInstalacionesFacadeRemote sistInstalacionRemote;
	private PuestoJPA dataPuesto; 
	private PuestoJPA posicion;
	protected InstalacionJPA dataInstalacion;
	protected InstalacionJPA instalacion;
	private String instalacionId="";
	private List<InstalacionJPA> instalaciones;
	public boolean seleccionadaInstalacion=false;
	private int id=0;
	private String nombre="";
	private String descripcion="";
	private int numOperarios=0;
	private boolean requerido=false;
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public PosicionBean() throws Exception 
	{
		cargarInstalaciones();
	}

	public int getId() throws Exception {
		setDataPuesto();
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
	public int getNumOperarios() {
		return numOperarios;
	}

	public void setNumOperarios(int numOperarios) {
		this.numOperarios = numOperarios;
	}

	public boolean isRequerido() {
		return requerido;
	}

	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}
	public PuestoJPA getPosicion() {
		return posicion;
	}

	public void setPosicion(PuestoJPA inst) throws Exception {	
		this.posicion = inst;
		setId(posicion.getId());
		setNombre(posicion.getNombre());
		setDescripcion(posicion.getDescripcion());
		setNumOperarios(posicion.getNumOperarios());
		setRequerido(posicion.getRequerido());
	}
	public PuestoJPA getDataPuesto()
	{
		return dataPuesto;
	}	
	public void setDataPuesto() throws Exception
	{	
		dataPuesto = new PuestoJPA(id,nombre,descripcion,numOperarios,requerido );
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
	public void crearPuesto() throws Exception
	{	
		resultadoCor = "";
		resultadoInc = "";	 
		
		if((nombre=="")||(descripcion=="")||(numOperarios<0)||(instalacion==null)) {
			resultadoInc = " Rellene todos los campos ";
			resultadoCor = "";
			return;
		}
		    boolean res=false;
        	setDataPuesto();
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
    		res= sistInstalacionRemote.crearPuesto(nombre, descripcion,numOperarios,requerido,instalacion);
	        	if(res) {
	        		resultadoCor = " Puesto creado ";
	        		resultadoInc = "";	        		
	        		}
        		else {
        			resultadoInc = " La posicion existe ";
        			resultadoCor = "";
        			}
	        	}
	public void verPuesto() throws Exception
	{
		try {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		setPosicion(sistInstalacionRemote.getPuesto(getId()));
		setId(posicion.getId());
		setNombre(posicion.getNombre());
		setDescripcion(posicion.getDescripcion());
		setNumOperarios(posicion.getNumOperarios());
		setRequerido(posicion.getRequerido());
		}catch (Exception e) {}
        }

	public void eliminarPuesto() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		setDataPuesto();		
		res= sistInstalacionRemote.eliminarPuesto(getId());
		if(res==true) {
    		resultadoCor = " Puesto eliminado ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " ¡ERROR AL ELIMINAR! ";
			resultadoCor = "";
			}
    	setDataPuesto();
        }
	
	public void modificarPuesto() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		res= sistInstalacionRemote.updatePuesto(id, posicion.getNombre(), posicion.getDescripcion(),posicion.getNumOperarios(), posicion.getRequerido(), posicion.getInstalacion());
		if(res) {
    		resultadoCor = " Puesto modificado ";
    		resultadoInc = "";	        		
    		}
		else {
			resultadoInc = " Error de modificacion ";
			resultadoCor = "";
			}
    	setDataPuesto();
	}

	public InstalacionJPA getDataInstalacion() {
		return dataInstalacion;
	}

	public void setDataInstalacion(InstalacionJPA dataInst) {
		this.dataInstalacion = dataInst;
	}
	public String getInstalacionId() {
		return instalacionId;
	}

	public void setInstalacionId(String nombre) throws NamingException {
		try {
			Integer.parseInt(nombre);
		}catch (Exception e) {
			this.instalacionId = "";
			return;
		}
		if((nombre=="")||(nombre==null)) {
			this.instalacionId = "";
			return;
		}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		this.instalacion=sistInstalacionRemote.getInstalacion(Integer.parseInt(nombre));
		this.instalacionId = nombre;
	}
	
	public void nombreInstalacion() {
		this.instalacionId =this.instalacion.getNombre();
		seleccionadaInstalacion=true;
	}
	public List<InstalacionJPA> getInstalaciones() throws NamingException {
		return instalaciones;
	}
	public void setInstalaciones(List<InstalacionJPA> insts) {
		this.instalaciones = insts;
	}
    
	public void cargarInstalaciones() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		Collection<InstalacionJPA> es =sistInstalacionRemote.getInstalaciones();
		instalaciones= new ArrayList<InstalacionJPA>(es);
	}
	
}
