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
import jpa.SeccionAlmacenJPA;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaSecciones")
@ViewScoped
public class ListaSeccionesMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	private Collection<SeccionAlmacenJPA> seccionesLista;
	private int screen = 0;
	protected Collection<SeccionAlmacenJPA> seccionesListaVista;
	protected int numeroSecciones = 0;
	private int idSeccionBuscada= -1;
	private String nomBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaSeccionesMBean() throws Exception
	{
		this.seccionesLista();
	}
	public String getNomBuscado() {
		return nomBuscado;
	}

	public void setNomBuscado(String nomBuscado) throws NamingException {
		this.nomBuscado = nomBuscado;
		int a=0;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if (nomBuscado!="") {
			a = sistemaUsuariosRemote.getSeccionNombr(nomBuscado);
			if (a<0) {
				idSeccionBuscada=-1;
				resultadoInc="Sección no encontrada";
				resultadoCor="";
				}
			}
		else {
			idSeccionBuscada=-1;
			resultadoInc="";
			resultadoCor="";
		}
		idSeccionBuscada=a;
	}
	public int getIdSeccionBuscada() {
		return idSeccionBuscada;
	}

	public void setIdSeccionBuscada(int nom) {
		this.idSeccionBuscada = nom;
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
	 * Metodo que retorna 9 instancias de sección por pantalla
	 * where the user is.
	 * @return Collection SeccionAlmacen
	 */
	public Collection<SeccionAlmacenJPA> getseccionesListaVista()
	{
		int n =0;
		seccionesListaVista = new ArrayList<SeccionAlmacenJPA>();
		for (Iterator<SeccionAlmacenJPA> iter2 = seccionesLista.iterator(); iter2.hasNext();)
		{
			SeccionAlmacenJPA cap = (SeccionAlmacenJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.seccionesListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroSecciones = n;
		return seccionesListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroSecciones()
	{ 
		return this.numeroSecciones;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < seccionesLista.size()))
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
	 * Method used for Facelet to call seccionesListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaSecciones() throws Exception
	{
		seccionesLista();
		return "listaSeccionesView";
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
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	public String eliminaSeccion(int id) throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		boolean res = sistemaUsuariosRemote.eliminarSeccion(id);
		return listaSecciones();
	}
	
	/**
	 * Method que toma la colección total de secciones de almacén
	 * @throws Exception
	 */

	private void seccionesLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if (idSeccionBuscada==-1) {
			seccionesLista = (Collection<SeccionAlmacenJPA>)sistemaUsuariosRemote.getSecciones();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			seccionesLista.clear();
			SeccionAlmacenJPA es =sistemaUsuariosRemote.getSeccion(idSeccionBuscada);
			if (es !=null) {
				seccionesLista.add(sistemaUsuariosRemote.getSeccion(idSeccionBuscada));
			    setResultadoCor("ID encontrada");
			    }
			else {setResultadoInc("ID no encontrada");}
		}
	}

	public void listaSeccionesFiltro() throws Exception {
		listaSecciones();
	}
	public void limpiaFiltroLista() throws Exception {
		setIdSeccionBuscada(-1);
		setNomBuscado("");
		listaSecciones();
	}

}
