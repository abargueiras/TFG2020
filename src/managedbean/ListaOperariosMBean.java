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
import jpa.OperarioJPA;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaOperarios")
@ViewScoped
public class ListaOperariosMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	private Collection<OperarioJPA> operariosLista;
	private int screen = 0;
	protected Collection<OperarioJPA> operariosListaVista;
	protected int numeroPersonal = 0;
	private String nomBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaOperariosMBean() throws Exception
	{
		this.operariosLista();
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
	 * Metodo que retorna 9 operarios por pantalla 
	 * @return Collection operarios
	 */
	public Collection<OperarioJPA> getoperariosListaVista()
	{
		int n =0;
		operariosListaVista = new ArrayList<OperarioJPA>();
		for (Iterator<OperarioJPA> iter2 = operariosLista.iterator(); iter2.hasNext();)
		{
			OperarioJPA cap = (OperarioJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.operariosListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroPersonal = n;
		return operariosListaVista;
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
		if (((screen+1)*9 < operariosLista.size()))
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
	 * Method used for Facelet to call operariosListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaPersonal() throws Exception
	{
		operariosLista();
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
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void operariosLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if (nomBuscado=="") {
			operariosLista = (Collection<OperarioJPA>)sistemaUsuariosRemote.getOperarios();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			operariosLista.clear();
			Collection<OperarioJPA> es =sistemaUsuariosRemote.getOperarioConNombre(nomBuscado);
			if (es !=null) {
				operariosLista=es;
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
