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
import jpa.PosicionRotacionJPA;
import jpa.OperarioJPA;
import ejb.SistemaRotacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "listaRotacionesOperario")
@ViewScoped
public class ListaRotacionesOperarioMBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	@EJB
	private SistemaRotacionesFacadeRemote sistemaRotacionesRemote;	
	private Collection<PosicionRotacionJPA> rotacionesOperarioLista;
	private int screen = 0;
	protected Collection<PosicionRotacionJPA> rotacionesOperarioListaVista;
	private OperarioJPA operario;
	protected int numeroRotacion = 0;
	private String oper="";
	private int idRot=0;
	private String rotacionBuscada="0";	
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public ListaRotacionesOperarioMBean() throws Exception
	{
		//try{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String idAux=(String)session.getAttribute("id");
        Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		setOperario(sistemaUsuariosRemote.getOperario(Integer.valueOf(idAux)));
		setOper(idAux);
		this.rotacionesOperarioLista();		
		resultadoInc="";
		resultadoCor="";
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String op) {
		this.oper = op;
	}
	
	public OperarioJPA getOperario() throws NamingException {
		return operario;
	}

	public void setOperario(OperarioJPA op)  {
		this.operario = op;
	}
	public String getRotacionBuscada() {
		return rotacionBuscada;
	}

	public void setRotacionBuscada(String nomBuscado) throws Exception {
		try {Integer.valueOf(nomBuscado);}catch(Exception e) {rotacionBuscada="0";return;}
		setIdRot(Integer.valueOf(nomBuscado));
		this.rotacionBuscada = nomBuscado;
		listaRotaciones();
	}
	
	public int getIdRot() {
		return idRot;
	}
	
	public void setIdRot(int idRot) {
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
	 * Metodo que muestra 9 posiciones del conjunto de rotaciones 
	 * where the user is.
	 * @return Collection SeccionAlmacen
	 */
	public Collection<PosicionRotacionJPA> getRotacionListaVista()
	{
		int n =0;
		rotacionesOperarioListaVista = new ArrayList<PosicionRotacionJPA>();
		for (Iterator<PosicionRotacionJPA> iter2 = rotacionesOperarioLista.iterator(); iter2.hasNext();)
		{
			PosicionRotacionJPA cap = (PosicionRotacionJPA) iter2.next();
			if (n >= screen*9 && n< (screen*9+9))
			{				
				this.rotacionesOperarioListaVista.add(cap);
			}
			n +=1;
		}
		this.numeroRotacion = n;
		return rotacionesOperarioListaVista;
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
		if (((screen+1)*9 < rotacionesOperarioLista.size()))
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
		rotacionesOperarioLista();
		return "listaRotacionesOperario";
	}
	
	
	/**
	 * Method used for Facelet to be able to delete the element and then to reload the data 
	 * @return Facelet name
	 */
	
	/**
	 * Metodo que toma toda la colección de rotaciones
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void rotacionesOperarioLista() throws Exception
	{	
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		screen = 0;
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
		if (rotacionBuscada=="0") {			
			
			rotacionesOperarioLista =sistemaRotacionesRemote.getPosicionesOperario(getOperario());
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
			rotacionesOperarioLista.clear();
			
			PosicionRotacionJPA es =sistemaRotacionesRemote.getPosicionOperario(getOperario(),getIdRot());
			if (es !=null) {
				rotacionesOperarioLista.add(es);
			    setResultadoCor("Rotacion encontrada");
			    }
			else {setResultadoInc("No existe id de rotacion");}
		}
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
