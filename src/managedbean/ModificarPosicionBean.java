/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import jpa.InstalacionJPA;
import jpa.PersonalJPA;
import jpa.PuestoJPA;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;
import ejb.SistemaInstalacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "modPosicion")
@ViewScoped
public class ModificarPosicionBean implements Serializable{
	
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
	private String id="";
	private String nombre="";
	private String descripcion="";
	private int numOperarios=0;
	private boolean requerido=false;
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public ModificarPosicionBean() throws Exception 
	{
		cargarInstalaciones();
	}

	public String getId() throws Exception {
		return this.id;
		}
	
	public void setId(String id) throws Exception {
		this.id = id;
		String a="";
		try {
		a=a+"   ";
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		a=a+"  ID:"+id;
		posicion=sistInstalacionRemote.getPuesto(Integer.valueOf(id));
		a=a+" Posicion:"+posicion.getNombre();
		setNombre(posicion.getNombre());
		setDescripcion(posicion.getDescripcion());
		setNumOperarios(posicion.getNumOperarios());
		setRequerido(posicion.getRequerido());
		setDataInstalacion(posicion.getInstalacion());
		instalacion=dataInstalacion;
		setInstalacionId(posicion.getNombre());
		
		}catch(Exception e) {resultadoInc = ""+a;}
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
	}
	public PuestoJPA getDataPuesto()
	{
		return dataPuesto;
	}	
	public void setDataPuesto() throws Exception
	{	
		dataPuesto = new PuestoJPA(Integer.valueOf(id),nombre,descripcion,numOperarios,requerido );
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

	public void verPuesto() throws Exception
	{
		try {

		}catch (Exception e) {}
        }

	
	public void modificarPuesto() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		res= sistInstalacionRemote.updatePuesto(Integer.valueOf(id),nombre,descripcion,numOperarios,requerido ,instalacion);
		if(res) {
    		resultadoCor = " Puesto modificado id"+id+" nombre:"+nombre+" descr:"+descripcion+" num:"+numOperarios+" req:"+requerido +" inst:"+instalacion.getNombre();
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
