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
import javax.servlet.http.HttpSession;
import javax.faces.context.*;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.EncargadoJPA;
import jpa.JefeAlmacenJPA;
import jpa.JefeEquipoJPA;
import jpa.OperarioJPA;
import jpa.PersonalJPA;
import jpa.RrhhJPA;

/**
 * Managed Bean UsuarioMBean
 */
@ManagedBean(name = "usuario")
@SessionScoped
public class UsuarioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	PersonalJPA personal;
	RrhhJPA dataRrhh;
	JefeAlmacenJPA dataJefeAlmacen;
	EncargadoJPA dataEncargado;
	JefeEquipoJPA dataJefeEquipo;
	OperarioJPA dataOperario;
	//UsuarioLogeado
	protected String resultado = "";
	protected int id;
	protected String password = "";
	protected String rol = "";
	protected String usuarioID = "";
	protected String usuarioNombre= "";

	
	public UsuarioBean() throws Exception 
	{
		
		resultado = "";
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	/**
	 * Atributos usados en login
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRol() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		if (session != null) {
			this.rol = (String)session.getAttribute("rol");
		}
		
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getUsuarioID() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		if (session != null) {
			this.usuarioID = (String)session.getAttribute("id");
		}
		
		return usuarioID;
	}

	public void setUsuarioID(String usuarioID) {
		this.usuarioID = usuarioID;
	}

	public String getUsuarioNombre() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		if (session != null) {
			usuarioNombre= (String)session.getAttribute("usuarioNombre");
		}
		
		return usuarioNombre;
	}

	public void setUsuarioNombre(String username) {
		this.usuarioNombre= username;
	}
	
	
	/**
	 * Metodos usados por Facelet usuario logeado
	 * @return Facelet name
	 * @throws Exception
	 */
	public String login() throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		boolean res = sistUsuariosRemote.login(this.getId(), this.getPassword());
		if(res) {
		    return "homeTeamWorkPlannerView";
		} else {
			setResultado("Error de autenticación: revisa las credenciales.");
			return "loginTeamWorkPlannerView";
		}
	}
	
	public String logout() throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		sistUsuariosRemote.logout();
		return "loginTeamWorkPlannerView";
	}

}
	
