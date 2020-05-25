/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;


import java.io.Serializable;
import java.util.*;
import java.sql.Date;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jpa.TurnoVacacionalJPA;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaTurnosVacacionales")
@ViewScoped
public class ListaTurnosVacacionalesBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistemaCalendariosRemote;
	private int ano = 0;
	private String fechaIni="";
	private Collection<TurnoVacacionalJPA> turnosVacacionalesLista;
	private int screen = 0;
	protected Collection<TurnoVacacionalJPA> turnosVacacionalesListaVista;
	protected int numeroTurno = 0;
	private String turnoBuscado="";
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaTurnosVacacionalesBean() throws Exception
	{
		this.turnosVacacionalesLista();
	}
	public String getFestivoBuscado() {
		return turnoBuscado;
	}

	public void setFestivoBuscado(String turnoBuscado) throws NamingException {
		this.turnoBuscado = turnoBuscado;
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
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public String getTurnoBuscado() {
		return turnoBuscado;
	}

	public void setTurnoBuscado(String tur) {
		this.turnoBuscado = tur;
	}
	public String getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(String tur) {
		this.fechaIni = tur;
	}
	/**
	 * Metodo que retorna 9 instancias por pantalla
	 * @return Collection Turno vacacional
	 */
	public Collection<TurnoVacacionalJPA> getTurnosVacacionalesListaVista()
	{
		int n =0;
		turnosVacacionalesListaVista = new ArrayList<TurnoVacacionalJPA>();
		for (Iterator<TurnoVacacionalJPA> iter2 = turnosVacacionalesLista.iterator(); iter2.hasNext();)
		{
			TurnoVacacionalJPA cap = (TurnoVacacionalJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.turnosVacacionalesListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroTurno = n;
		return turnosVacacionalesListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroTurno()
	{ 
		return this.numeroTurno;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < turnosVacacionalesLista.size()))
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
	 * Method used for Facelet to call turnosVacacionalesListaVista Facelet
	 * @return Facelet name
	 * @throws Exception
	 */
	public String listaTurnos() throws Exception
	{
		turnosVacacionalesLista();
		return "listaTurnosVacacionalesView";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearTurnoVacacionalAnualEmpresa()
	{
		return "creacionTurnosVacacionalesAnuales";
	}
	
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	public String eliminaTurnoVacacionalEmpresa() throws Exception
	{
		Date id=null;
		try {
		id= Date.valueOf(fechaIni);}
		catch(Exception e) {
			setResultadoCor("");
			setResultadoInc("Formato incorrecto AAAA-MM-DD");
		}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		boolean res = sistemaCalendariosRemote.eliminarTurnoVacacionalEmpresa(id);
		return listaTurnos();
	}
	
	/**
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void turnosVacacionalesLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		if (turnoBuscado=="") {
			turnosVacacionalesLista = (Collection<TurnoVacacionalJPA>)sistemaCalendariosRemote.getTurnosVacacionalesEmpresa();
			}
		else {
			setResultadoInc("");
			setResultadoCor("");	
			turnosVacacionalesLista.clear();
			Collection<TurnoVacacionalJPA> es =sistemaCalendariosRemote.getTurnoVacacional(ano);
			if (es !=null) {
				turnosVacacionalesLista=es;
			    setResultadoCor("Turno filtrado");
			    }
			else {setResultadoInc("No existe turno vacacional del año");}
		}
	}

	public void listaTurnosFiltro() throws Exception {
		listaTurnos();
	}
	public void limpiaFiltroLista() throws Exception {
		setFestivoBuscado("");
		setResultadoCor("");
		setResultadoInc("");
		listaTurnos();
	}
}
