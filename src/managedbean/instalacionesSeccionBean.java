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
import jpa.OperarioJPA;
import jpa.PuestoJPA;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;
import ejb.SistemaInstalacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "instalacionesSeccion")
@ViewScoped
public class instalacionesSeccionBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	private SistemaInstalacionesFacadeRemote sistInstalacionesRemote;
	protected SeccionAlmacenJPA dataSeccion;
	private Collection<InstalacionJPA> instalacionesLista= new ArrayList <InstalacionJPA>();
	private Collection<InstalacionJPA> instalacionesListaVista= new ArrayList <InstalacionJPA>();
	protected String id = "";
	protected String nombre= "";
	protected String numOperarios= "0";
	protected String numPuestos= "0";
	protected int numeroInstalaciones = 0;
	protected String descripcion = "";
	protected Turno turno = Turno.Mañana;
	private int screen = 0;
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public instalacionesSeccionBean() throws Exception 
	{
		try {
			sistInstalacionesRemote.getInstalaciones(dataSeccion);
		}
		catch(Exception e) {
			setResultadoInc("No hay instalaciones asociadas");
			return;
		}
		instalacionesLista();
		Iterator <InstalacionJPA> insts= instalacionesLista.iterator();
		InstalacionJPA inst=null;
		while (insts.hasNext()) {
			inst=insts.next();
			try {
				Collection <PuestoJPA> puestos= sistInstalacionesRemote.getPuestos(inst);
				Iterator <PuestoJPA> iter2= puestos.iterator();
				PuestoJPA pos=null;
				numPuestos="0";
				while (iter2.hasNext()) {
					pos=iter2.next();
					numPuestos= String.valueOf(Integer.valueOf(numPuestos)+Integer.valueOf(pos.getNumOperarios()));
					}
			}catch (Exception e) {
				setResultadoInc("No hay instalaciones asociadas");
				return;
			}
		}
	}

	public String getId() {
		return this.id;
		}
	
	public void setId(String id) throws Exception {
		try {
			Integer.parseInt(id);
			this.id = id;
		}catch(Exception e) {}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		setDataSeccion(sistUsuariosRemote.getSeccion(Integer.valueOf(getId())));
		Collection <OperarioJPA> ops=sistUsuariosRemote.getOperarioSeccion(dataSeccion);
		setNumOperarios(String.valueOf(ops.size()));

	}
	
	public String getNombre() {
		return this.nombre;
		}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNumOperarios() {
		return this.numOperarios;
		}
	
	public void setNumOperarios(String num) {
		try {
			Integer.valueOf(num);
		    this.numOperarios = num;
		}catch(Exception e){}
	    }
	public String getNumPuestos() {
		return this.numPuestos;
		}
	
	public void setNumPuestos(String num) {
		try {
			Integer.valueOf(num);
		    this.numPuestos = num;
		}catch(Exception e){}
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
	public void setDataSeccion(SeccionAlmacenJPA sec) throws Exception
	{	
		dataSeccion = sec;
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
	 * Metodo que retorna 12 instancias de la coleccion de instalaciones 
	 * @return Collection Instalaciones
	 */
	public Collection<InstalacionJPA> getinstalacionesListaVista()
	{
		int n =0;
		instalacionesListaVista = new ArrayList<InstalacionJPA>();
		for (Iterator<InstalacionJPA> iter2 = instalacionesLista.iterator(); iter2.hasNext();)
		{
			InstalacionJPA ins = (InstalacionJPA) iter2.next();
			if (n >= screen*12 && n< (screen*12+12))
			{				
				this.instalacionesListaVista.add(ins);
			}
			n +=1;
		}
		this.numeroInstalaciones = n;
		return instalacionesListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroInstalaciones()
	{ 
		return this.numeroInstalaciones;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*12 < instalacionesLista.size()))
		{
			screen +=1;
		}
	}
	public void previousScreen()
	{
		if ((screen > 0))
		{
			screen -=1;
		}
	}
	
	
	
	/**
	 * Method used for Facelet to be able to do the submit
	 * @return Facelet name
	 * @throws Exception
	 */
	public void verSeccion() throws Exception
	{

        }

	public String eliminarInstalacion(int id) throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		boolean res = sistUsuariosRemote.desasignarInstalacion(dataSeccion,id);
		return listaInstalaciones();
	}
	
	/**
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */

	private void instalacionesLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		try {
			sistInstalacionesRemote.getInstalaciones(dataSeccion);
		}
		catch(Exception e) {
			setResultadoInc("No hay instalaciones asociadas");
			return;
		}
		instalacionesLista = (Collection<InstalacionJPA>)sistInstalacionesRemote.getInstalaciones(dataSeccion);
	}
	/**
	 * Method used for Facelet to call seccionesListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaInstalaciones() throws Exception
	{
		instalacionesLista();
		return "verInstalacionesSeccionView";
	}

	/*
	 * SelectManyList instalaciones
	 */
	private List<String>instalacionesV;

	//getter and setter methods...

	public List<String> getInstalacionesV() {
		return instalacionesV;
	}
	public void setInstalacionesV(List<String> a) {
		this.instalacionesV=a;
	}
	//Generated by Object array
	public static class InstalacionV{
		public String instalacionLabel;
		public String instalacionValue;

		public InstalacionV(String instalacionLabel, String instalacionValue){
			this.instalacionLabel = instalacionLabel;
			this.instalacionValue = instalacionValue;
		}

		public String getInstalacionLabel(){
			return instalacionLabel;
		}

		public String getInstalacionValue(){
			return instalacionValue;
		}

	}

	public InstalacionV[] instalacionesVList;

	public InstalacionV[] getinstalacionesVValue() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		Collection <InstalacionJPA> a=sistInstalacionesRemote.getInstalaciones();
		instalacionesVList = new InstalacionV[a.size()];
		Iterator <InstalacionJPA> its= a.iterator();
		int i=0;
		while (its.hasNext()) {
			InstalacionJPA it=its.next();
			instalacionesVList[i] = new InstalacionV(it.getNombre(),String.valueOf(it.getId()));
			i++;
		}
		return instalacionesVList;
	}
	public void asignarInstalaciones() throws Exception {
		resultadoInc = "";
		resultadoCor = "";
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		Iterator <String> a= instalacionesV.iterator();
		instalacionesLista.clear();
		while(a.hasNext()) {
			InstalacionJPA b=sistInstalacionesRemote.getInstalacion(Integer.valueOf(a.next()));
			instalacionesLista.add(b);
		}
		sistInstalacionesRemote.modificarInstalaciones(dataSeccion,instalacionesLista);
		Iterator <InstalacionJPA> insts= instalacionesLista.iterator();
		InstalacionJPA inst=null;
		numPuestos="0";
		while (insts.hasNext()) {
			inst=insts.next();
			try {
				Collection <PuestoJPA> puestos= sistInstalacionesRemote.getPuestos(inst);
				Iterator <PuestoJPA> iter2= puestos.iterator();
				PuestoJPA pos=null;				
				while (iter2.hasNext()) {
					pos=iter2.next();
					numPuestos= String.valueOf(Integer.valueOf(numPuestos)+Integer.valueOf(pos.getNumOperarios()));
					}
			}catch (Exception e) {
			}
		}
	}

}
