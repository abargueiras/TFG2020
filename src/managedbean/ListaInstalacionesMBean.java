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
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaInstalaciones")
@ViewScoped
public class ListaInstalacionesMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaInstalacionesFacadeRemote sistInstalacionesRemote;
	private Collection<InstalacionJPA> instalacionesLista;
	private int screen = 0;
	protected Collection<InstalacionJPA> instalacionesListaVista;
	protected int numeroInstalaciones = 0;
	private int idInstalacionBuscada= -1;
	private String nomBuscado="";
	private String nomSeccion="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaInstalacionesMBean() throws Exception
	{
		this.instalacionesLista();
	}
	public String getNomBuscado() {
		return nomBuscado;
	}

	public void setNomBuscado(String nomBuscado) throws NamingException {
		this.nomBuscado = nomBuscado;
	}
	public int getIdInstalacionBuscada() {
		return idInstalacionBuscada;
	}

	public void setIdInstalacionBuscada(int nom) {
		this.idInstalacionBuscada = nom;
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
	 * Metodo que retorna 9 instalaciones por pantalla 
	 * @return Collection instalaciones
	 */
	
	public Collection<InstalacionJPA> getInstalacionesListaVista()
	{
		int n =0;
		instalacionesListaVista = new ArrayList<InstalacionJPA>();
		for (Iterator<InstalacionJPA> iter2 = instalacionesLista.iterator(); iter2.hasNext();)
		{
			InstalacionJPA cap = (InstalacionJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.instalacionesListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroInstalaciones = n;
		return instalacionesListaVista;
	}
	
	/**
	 * Returns the total number of instances of Instalacion
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
		if (((screen+1)*9 < instalacionesLista.size()))
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
	 * Method used for Facelet to call instalacionesListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaInstalaciones() throws Exception
	{
		instalacionesLista();
		return "listaInstalacionesView";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearInstalacion()
	{
		return "crearInstalacionView";
	}
	
	/**
	 * Method used for Facelet to call UpdateCAPView Facelet
	 * @return Facelet name
	 */
	public String setModificaInstalacion()
	{
		return "modificarInstalacionView";
	}
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	public String eliminaSeccion(int id) throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		boolean res = sistInstalacionesRemote.eliminarInstalacion(id);
		return listaInstalaciones();
	}
	
	/**
	 * Metodo que toma la coleccion completa de instalaciones a mostrar 
	 * method instalacionesLista
	 * @throws Exception
	 */

	private void instalacionesLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		if (nomBuscado=="") {
			instalacionesLista = (Collection<InstalacionJPA>)sistInstalacionesRemote.getInstalaciones();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			instalacionesLista.clear();
			InstalacionJPA ins =(sistInstalacionesRemote.getInstalacionNombre(nomBuscado));
			if (ins !=null) {
				instalacionesLista.add(ins);
			    setResultadoCor("Instalación encontrada");
			    }
			else {setResultadoInc("Instalación no encontrada");}
		}
	}

	public void listaInstalacionesFiltro() throws Exception {
		listaInstalaciones();
	}
	public void limpiaFiltroLista() throws Exception {
		setIdInstalacionBuscada(-1);
		setNomBuscado("");
		listaInstalaciones();
	}

}
