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
import jpa.DiaFestivoJPA;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaFestivosEmpresa")
@ViewScoped
public class ListaFestivosEmpresaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistemaCalendariosRemote;
	private Date diaFestivo = null;
	private Collection<DiaFestivoJPA> festivosEmpresaLista;
	private int screen = 0;
	protected Collection<DiaFestivoJPA> festivosEmpresaListaVista;
	protected int numeroFestivo = 0;
	private String festivoBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaFestivosEmpresaBean() throws Exception
	{
		this.festivosEmpresaLista();
	}
	public String getFestivoBuscado() {
		return festivoBuscado;
	}

	public void setFestivoBuscado(String festivoBuscado) throws NamingException {
		this.festivoBuscado = festivoBuscado;
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
	public Date getDiaFestivo() {
		return diaFestivo;
	}
	public void setDiaFestivo(Date diaFestivo) {
		this.diaFestivo = diaFestivo;
	}
	/**
	 * Metodo que muestra un máximo de 9 dias festivos por pantalla 
	 * where the user is.
	 * @return Collection DiaFestivo
	 */
	public Collection<DiaFestivoJPA> getFestivosEmpresaListaVista()
	{
		int n =0;
		festivosEmpresaListaVista = new ArrayList<DiaFestivoJPA>();
		for (Iterator<DiaFestivoJPA> iter2 = festivosEmpresaLista.iterator(); iter2.hasNext();)
		{
			DiaFestivoJPA cap = (DiaFestivoJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.festivosEmpresaListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroFestivo = n;
		return festivosEmpresaListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroFestivo()
	{ 
		return this.numeroFestivo;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < festivosEmpresaLista.size()))
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
	 * Method used for Facelet to call festivosEmpresaListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaFestivos() throws Exception
	{
		festivosEmpresaLista();
		return "listaSeccionesView";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearDiaFestivoEmpresa()
	{
		return "crearDiaFestivoView";
	}
	
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	public String eliminaDiaFestivoEmpresa(int id) throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		boolean res = sistemaCalendariosRemote.eliminarDiaFestivoEmpresa(id);
		return listaFestivos();
	}
	
	/**
	 * Metodo que toma las colección de dias festivos de la empresa 
	 * @throws Exception
	 */

	private void festivosEmpresaLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		if (festivoBuscado=="") {
			festivosEmpresaLista = (Collection<DiaFestivoJPA>)sistemaCalendariosRemote.getFestivosEmpresa();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			festivosEmpresaLista.clear();
			Collection<DiaFestivoJPA> es =sistemaCalendariosRemote.getFestivoEmpresa(diaFestivo);
			if (es !=null) {
				festivosEmpresaLista=es;
			    setResultadoCor("Personal filtrado");
			    }
			else {setResultadoInc("No existe personal con este nombre");}
		}
	}

	public void listaPersonalFiltro() throws Exception {
		listaFestivos();
	}
	public void limpiaFiltroLista() throws Exception {
		setFestivoBuscado("");
		setResultadoCor("");
		setResultadoInc("");
		listaFestivos();
	}
}
