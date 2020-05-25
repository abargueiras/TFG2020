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
import jpa.PersonalJPA;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaPersonal")
@ViewScoped
public class ListaPersonalAlmacenMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	private Collection<PersonalJPA> personalLista;
	private int screen = 0;
	protected Collection<PersonalJPA> personalListaVista;
	protected int numeroPersonal = 0;
	private String nomBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaPersonalAlmacenMBean() throws Exception
	{
		this.personalLista();
	}
	public String getNomBuscado() {
		return nomBuscado;
	}

	public void setNomBuscado(String nomBuscado) throws NamingException {
		this.nomBuscado = nomBuscado;
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
	 * Metodo que retorna 9 miembros del personal por pantalla 
	 * @return Collection personal
	 */
	public Collection<PersonalJPA> getpersonalListaVista()
	{
		int n =0;
		personalListaVista = new ArrayList<PersonalJPA>();
		for (Iterator<PersonalJPA> iter2 = personalLista.iterator(); iter2.hasNext();)
		{
			PersonalJPA cap = (PersonalJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.personalListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroPersonal = n;
		return personalListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroSecciones()
	{ 
		return this.numeroPersonal;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < personalLista.size()))
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
	 * Method used for Facelet to call personalListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaPersonal() throws Exception
	{
		personalLista();
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
		return listaPersonal();
	}
	
	/**
	 * Metodo que toma todas las instancias de personal a mostrar 
	 * @throws Exception
	 */

	private void personalLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if (nomBuscado=="") {
			personalLista = (Collection<PersonalJPA>)sistemaUsuariosRemote.getPersonal();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			personalLista.clear();
			Collection<PersonalJPA> es =sistemaUsuariosRemote.getPersonalConNombre(nomBuscado);
			if (es !=null) {
				personalLista=es;
			    setResultadoCor("Personal filtrado");
			    }
			else {setResultadoInc("No existe personal con este nombre");}
		}
	}

	public void listaPersonalFiltro() throws Exception {
		listaPersonal();
	}
	public void limpiaFiltroLista() throws Exception {
		setNomBuscado("");
		setResultadoCor("");
		setResultadoInc("");
		listaPersonal();
	}

}
