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

import jpa.PersonalAlmacenJPA;
import jpa.SeccionAlmacenJPA;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioSeccionJPA;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaCalendariosSeccion")
@ViewScoped
public class ListaCalendariosSeccionMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistemaCalendariosRemote;	
	private Collection<CalendarioSeccionJPA> calendarioSeccionLista;
	private int screen = 0;
	protected Collection<CalendarioSeccionJPA> calendarioSeccionListaVista;
	private SeccionAlmacenJPA seccion;
	protected int numeroCalendario = 0;
	private String anoBuscado="0";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaCalendariosSeccionMBean() throws Exception
	{
		this.calendarioSeccionLista();
	}
	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}

	public void setSeccion(SeccionAlmacenJPA sec)  {
		this.seccion = sec;
	}
	public String getAnoBuscado() {
		return anoBuscado;
	}

	public void setAnoBuscado(String nomBuscado) throws NamingException {
		try {Integer.valueOf(nomBuscado);}catch(Exception e) {anoBuscado="0";}
		this.anoBuscado = nomBuscado;
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
	 * Method that returns an instance Collection of 9 or less SeccionAlmacen according screen 
	 * where the user is.
	 * @return Collection SeccionAlmacen
	 */
	public Collection<CalendarioSeccionJPA> getCalendarioListaVista()
	{
		int n =0;
		calendarioSeccionListaVista = new ArrayList<CalendarioSeccionJPA>();
		for (Iterator<CalendarioSeccionJPA> iter2 = calendarioSeccionLista.iterator(); iter2.hasNext();)
		{
			CalendarioSeccionJPA cap = (CalendarioSeccionJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.calendarioSeccionListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroCalendario = n;
		return calendarioSeccionListaVista;
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
		if (((screen+1)*9 < calendarioSeccionLista.size()))
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
		calendarioSeccionLista();
		return "listaCalendarioSeccion";
	}
	
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	
	/**
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void calendarioSeccionLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		if (anoBuscado=="0") {
			calendarioSeccionLista =sistemaCalendariosRemote.getCalendariosSeccion();
			}
		else {
			try {
				Integer.parseInt(anoBuscado);}
				catch(Exception e) {
					return;
				}
			numeroCalendario=Integer.parseInt(anoBuscado);
			setResultadoInc("");
			setResultadoCor("");	
			calendarioSeccionLista.clear();
			Collection<CalendarioSeccionJPA> es =sistemaCalendariosRemote.getCalendarioSeccion(numeroCalendario);
			if (es !=null) {
				calendarioSeccionLista=es;
			    setResultadoCor("Calendario filtrado");
			    }
			else {setResultadoInc("No existen datos de calendario");}
		}
	}

	public void listaCalendariosFiltro() throws Exception {
		listaCalendarios();
	}
	public void limpiaFiltroLista() throws Exception {
		setAnoBuscado("0");
		setResultadoCor("");
		setResultadoInc("");
		listaCalendarios();
	}

}
