/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;


import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import jpa.SeccionAlmacenJPA;
import jpa.RotacionJPA;
import ejb.SistemaRotacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaRotacionesSeccion")
@ViewScoped
public class ListaRotacionesSeccionMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaRotacionesFacadeRemote sistemaRotacionesRemote;	
	private Collection<RotacionJPA> rotacionesSeccionLista;
	private int screen = 0;
	protected Collection<RotacionJPA> rotacionesSeccionListaVista;
	private SeccionAlmacenJPA seccion;
	protected int numeroRotacion = 0;
	private String tUser="";
	private String idRot="0";
	private String rotacionBuscada="0";	
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaRotacionesSeccionMBean() throws Exception
	{
		//try{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String idAux=(String)session.getAttribute("id");
        Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		try {
			setSeccion(sistemaUsuariosRemote.getSeccionEmpleado(Integer.valueOf(idAux)));
		}catch(Exception e) {setTUser("privi");}
		this.rotacionesSeccionLista();
		
		resultadoInc="";
		resultadoCor="";
	}
	public String getTUser() {
		return tUser;
	}
	public void setTUser(String tUser) {
		this.tUser = tUser;
	}
	
	public SeccionAlmacenJPA getSeccion() throws NamingException {
		return seccion;
	}

	public void setSeccion(SeccionAlmacenJPA sec)  {
		this.seccion = sec;
	}
	public String getRotacionBuscada() {
		return rotacionBuscada;
	}

	public void setRotacionBuscada(String nomBuscado) throws NamingException {
		try {Integer.valueOf(nomBuscado);}catch(Exception e) {rotacionBuscada="0";}
		this.rotacionBuscada = nomBuscado;
	}
	
	public String getIdRot() {
		return idRot;
	}
	
	public void setIdRot(String idRot) {
		try {Integer.valueOf(idRot);}catch(Exception e) {idRot="0";return;}
		this.idRot = idRot;
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
	 * Method que retorna 9 instancias de SeccionAlmacen  
	 * @return Collection SeccionAlmacen
	 */
	public Collection<RotacionJPA> getRotacionListaVista()
	{
		int n =0;
		rotacionesSeccionListaVista = new ArrayList<RotacionJPA>();
		for (Iterator<RotacionJPA> iter2 = rotacionesSeccionLista.iterator(); iter2.hasNext();)
		{
			RotacionJPA cap = (RotacionJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.rotacionesSeccionListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroRotacion = n;
		return rotacionesSeccionListaVista;
	}
	
	/**
	 * Retorna el número total de calendarios de la empresa
	 * @return CAPs number
	 */
	public int getNumeroRotacioness()
	{ 
		return this.numeroRotacion;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*9 < rotacionesSeccionLista.size()))
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
	public String listaRotaciones() throws Exception
	{
		rotacionesSeccionLista();
		return "listaRotacionesSeccion";
	}
	
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	
	/**
	 * Metodo que muestra las Secciones de Almacen  
	 * @throws Exception
	 */

	private void rotacionesSeccionLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
		if(tUser.equals("privi")) {
			if (rotacionBuscada=="0") {			
				rotacionesSeccionLista =sistemaRotacionesRemote.getRotaciones();
				}
			else {
				try {
					Integer.parseInt(rotacionBuscada);}
					catch(Exception e) {
						return;
					}
				numeroRotacion=Integer.parseInt(rotacionBuscada);
				setResultadoInc("");
				setResultadoCor("");	
				rotacionesSeccionLista.clear();
				RotacionJPA es =sistemaRotacionesRemote.getRotacionNum(numeroRotacion);
				if (es !=null) {
					rotacionesSeccionLista.add(es);
				    setResultadoCor("Rotacion encontrada");
				    }
				else {setResultadoInc("No existe id de rotacion");}
			}
		}
		else if (rotacionBuscada=="0") {			
			rotacionesSeccionLista =sistemaRotacionesRemote.getRotacionesSeccion(seccion);
			}
		else {
			try {
				Integer.parseInt(rotacionBuscada);}
				catch(Exception e) {
					return;
				}
			numeroRotacion=Integer.parseInt(rotacionBuscada);
			setResultadoInc("");
			setResultadoCor("");	
			rotacionesSeccionLista.clear();
			RotacionJPA es =sistemaRotacionesRemote.getRotacionNum(numeroRotacion);
			if (es !=null) {
				rotacionesSeccionLista.add(es);
			    setResultadoCor("Rotacion encontrada");
			    }
			else {setResultadoInc("No existe id de rotacion");}
		}
	}

	public String eliminarRotacion() throws NamingException {
		int i;
		try {
			i=Integer.valueOf(idRot);
			}catch(Exception e) {idRot="0";return"";}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
		RotacionJPA r = sistemaRotacionesRemote.getRotacionNum(i);
		sistemaRotacionesRemote.eliminarRotacion(r);
		return "listaRotacionesSeccion";
		
	}
	public String getEliminarRotacion() throws NamingException {
		return "borrada";
	}
	
	
	
	public void listaRotacionesFiltro() throws Exception {
		listaRotaciones();
	}
	public void limpiaFiltroLista() throws Exception {
		setRotacionBuscada("0");
		setResultadoCor("");
		setResultadoInc("");
		listaRotaciones();
	}

}
