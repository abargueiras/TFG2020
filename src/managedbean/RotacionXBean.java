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
import jpa.PosicionRotacionJPA;
import jpa.RotacionJPA;
import ejb.SistemaRotacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "rotacionX")
@ViewScoped
public class RotacionXBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	private SistemaRotacionesFacadeRemote sistemaRotacionesRemote;
	private Collection<PosicionRotacionJPA> rotacionLista=new ArrayList<PosicionRotacionJPA> ();
	private int screen = 0;
	protected Collection<PosicionRotacionJPA> rotacionListaVista;
	protected RotacionJPA rotacion;
	protected int numPosicion = 0;
	protected String numRotacion = "0";
	private int id;
    private int numPuestos=0;  
    private int numOperariosPuestos=0;   
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public RotacionXBean() throws Exception
	{
		
	}
	
	public void verLista() throws NamingException {
		getNumRotacion();
		this.rotacionLista();
	}
	
	public String getNumRotacion() {
		return numRotacion;
	}

	public void setNumRotacion(String num) throws NamingException {
		try {
			setId(Integer.valueOf(num));
		}catch(Exception e) {numRotacion = "0"; return;}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
        setRotacion(sistemaRotacionesRemote.getRotacionNum(Integer.valueOf(num)));	
		this.numRotacion = num;
	}
	
	public String getResultadoInc() {
		return resultadoInc;
	}

	public void setResultadoInc(String resultadoInc) {
		this.resultadoInc = resultadoInc;
	}

	public String getResultadoCor() {
		return resultadoCor;
	}

	public void setResultadoCor(String resultadoCor) {
		this.resultadoCor = resultadoCor;
	}
	
	/**
	 * Method that returns an instance Collection of 10 or less SeccionAlmacen according screen 
	 * where the user is.
	 * @return Collection SeccionAlmacen
	 */
	public Collection<PosicionRotacionJPA> getRotacionListaVista()
	{
		int n =0;
		rotacionListaVista = new ArrayList<PosicionRotacionJPA>();
		for (Iterator<PosicionRotacionJPA> iter2 = rotacionLista.iterator(); iter2.hasNext();)
		{
			PosicionRotacionJPA cap = (PosicionRotacionJPA) iter2.next();
			if (n >= screen*25 && n< (screen*25+25))
			{				
				this.rotacionListaVista.add(cap);
			}
			n +=1;
		}
		this.numPosicion = n;
		return rotacionListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroSecciones()
	{ 
		return this.numPosicion;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*25 < rotacionLista.size()))
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
	 * Method used for Facelet to call rotacionListaVista Facelet
	 * @return Facelet name
	 * @throws NamingException 
	 * @throws Exception
	 */
	
	
	public String listaRotacion() throws Exception
	{
		rotacionLista();
		return "rotacionListaVista";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearSeccion()
	{
		return "crearSeccionView";
	}
	
	/**
	 * Method used for Facelet to call UpdateCAPView Facelet
	 * @return Facelet name
	 */
	public String setModificaSeccion()
	{
		return "modificarSeccionView";
	}
	
	
	/**
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws NamingException 
	 * @throws Exception
	 */
	
	private void rotacionLista() throws NamingException 
	{		
		try {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
        rotacionLista.addAll(sistemaRotacionesRemote.getRotacion(getId()));
		}catch (Exception e) {
			resultadoInc="Error";
				
		}
		resultadoCor="Info rotación";
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) throws NamingException {	
		this.id = id;
	}

	public RotacionJPA getRotacion() {
		return rotacion;
	}

	public void setRotacion(RotacionJPA rotacion) {
		this.rotacion = rotacion;
	}

	public int getNumOperariosPuestos() {
		return numOperariosPuestos;
	}

	public void setNumOperariosPuestos(int numOperarios) {
		this.numOperariosPuestos = numOperarios;
	}
	
	public int getNumPuestos() {
		return numPuestos;
	}

	public void setNumPuestos(int numPuestos) {
		this.numPuestos = numPuestos;
	}
}
