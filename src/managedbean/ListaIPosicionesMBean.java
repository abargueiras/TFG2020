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
import jpa.PuestoJPA;
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaPosiciones")
@ViewScoped
public class ListaIPosicionesMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaInstalacionesFacadeRemote sistInstalacionesRemote;
	private Collection<PuestoJPA> posicionesLista;
	private int screen = 0;
	protected Collection<PuestoJPA> posicionesListaVista;
	protected int numeroPosiciones = 0;
	private int idPosicionBuscada= -1;
	private String nomBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaIPosicionesMBean() throws Exception
	{
		this.posicionesLista();
	}
	public String getNomBuscado() {
		return nomBuscado;
	}

	public void setNomBuscado(String nomBuscado) throws NamingException {
		this.nomBuscado = nomBuscado;
	}
	public int getidPosicionBuscada() {
		return idPosicionBuscada;
	}

	public void setidPosicionBuscada(int nom) {
		this.idPosicionBuscada = nom;
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
	 * Metodo que retorna 9 instalaciones
	 */
	public Collection<PuestoJPA> getposicionesListaVista()
	{
		int n =0;
		posicionesListaVista = new ArrayList<PuestoJPA>();
		for (Iterator<PuestoJPA> iter2 = posicionesLista.iterator(); iter2.hasNext();)
		{
			PuestoJPA cap = (PuestoJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.posicionesListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroPosiciones = n;
		return posicionesListaVista;
	}
	
	/**
	 * Returns the total number of instances of Instalacion
	 * @return CAPs number
	 */
	public int getnumeroPosiciones()
	{ 
		return this.numeroPosiciones;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < posicionesLista.size()))
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
	 * Method used for Facelet to call posicionesListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaPosiciones() throws Exception
	{
		posicionesLista();
		return "listaPosicionesView";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearInstalacion()
	{
		return "crearPosicionView";
	}
	
	/**
	 * Method used for Facelet to call UpdateCAPView Facelet
	 * @return Facelet name
	 */
	public String setModificaPosicion()
	{
		return "modificarPosicionView";
	}
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	public String eliminaPosicion(int id) throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		boolean res = sistInstalacionesRemote.eliminarPuesto(id);
		return listaPosiciones();
	}
	
	/**
	 * Method that takes a collection of instances of Instalacion calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void posicionesLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		if (nomBuscado=="") {
			posicionesLista = (Collection<PuestoJPA>)sistInstalacionesRemote.getPosiciones();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			posicionesLista.clear();
			Collection<PuestoJPA> es =(sistInstalacionesRemote.getPosicionNombre(nomBuscado));
			if (es !=null) {
				posicionesLista=es;
			    setResultadoCor("Posición encontrada");
			    }
			else {setResultadoInc("Posición no encontrada");}
		}
	}

	public void listaPosicionesFiltro() throws Exception {
		listaPosiciones();
	}
	public void limpiaFiltroLista() throws Exception {
		setidPosicionBuscada(-1);
		setNomBuscado("");
		listaPosiciones();
	}

}
