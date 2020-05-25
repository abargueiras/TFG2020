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
import jpa.CalendarioEmpresaJPA;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaCalendariosEmpresa")
@ViewScoped
public class ListaCalendariosEmpresaMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistemaCalendariosRemote;
	private Collection<CalendarioEmpresaJPA> calendarioLista;
	private int screen = 0;
	protected Collection<CalendarioEmpresaJPA> calendarioListaVista;
	protected int numeroCalendario = 0;
	private String nomBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaCalendariosEmpresaMBean() throws Exception
	{
		this.calendarioLista();
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
	 * Metodo que muestra por pantalla 9 instancias de la coleccion. 
	 * @return Collection Calendario empresa
	 */
	public Collection<CalendarioEmpresaJPA> getCalendarioListaVista()
	{
		int n =0;
		calendarioListaVista = new ArrayList<CalendarioEmpresaJPA>();
		for (Iterator<CalendarioEmpresaJPA> iter2 = calendarioLista.iterator(); iter2.hasNext();)
		{
			CalendarioEmpresaJPA cap = (CalendarioEmpresaJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.calendarioListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroCalendario = n;
		return calendarioListaVista;
	}
	
	/**
	 * Retorna el número total de calendarios de la empresa
	 * @return CAPs number
	 */
	public int getNumeroCalendarioss()
	{ 
		return this.numeroCalendario;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < calendarioLista.size()))
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
	 * Method used for Facelet to call CalendarioListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaCalendarios() throws Exception
	{
		calendarioLista();
		return "listaCalendariosEmpresa";
	}
	
	/**
	 * Metodo que toma todas las instancias del calendario 
	 * @throws Exception
	 */

	private void calendarioLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		if (nomBuscado=="") {
			calendarioLista = (Collection<CalendarioEmpresaJPA>)sistemaCalendariosRemote.getCalendariosEmpresa();
			}
		else {
			try {
				Integer.parseInt(nomBuscado);}
				catch(Exception e) {
					return;
				}
			numeroCalendario=Integer.parseInt(nomBuscado);
			setResultadoInc("");
			setResultadoCor("");	
			calendarioLista.clear();
			CalendarioEmpresaJPA es =sistemaCalendariosRemote.getCalendarioEmpresa(numeroCalendario);
			if (es !=null) {
				calendarioLista.clear();
				calendarioLista.add(es);
			    setResultadoCor("Calendario filtrado");
			    }
			else {setResultadoInc("No existen datos de calendario");}
		}
	}

	public void listaCalendariosFiltro() throws Exception {
		listaCalendarios();
	}
	public void limpiaFiltroLista() throws Exception {
		setNomBuscado("");
		setResultadoCor("");
		setResultadoInc("");
		listaCalendarios();
	}

}
