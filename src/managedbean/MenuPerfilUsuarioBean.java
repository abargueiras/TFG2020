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

import jpa.PersonalJPA;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean UpdateCAPMBean
 */
@ManagedBean(name = "menuperfilusuario")
@ViewScoped
public class MenuPerfilUsuarioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	protected int usuarioID;
	protected String nuevoPassword = "";
	protected String password = "";
	protected String resultadoInc = "";
	protected String resultadoCor = "";
	
	public MenuPerfilUsuarioBean() throws NamingException 
	{
		
	}

	public int getUsuarioID() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		if (session != null) {
			String id=(String) session.getAttribute("id");
			this.usuarioID =Integer.valueOf(id);
		}
		
		return usuarioID;
	}

	public String getPassword() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		PersonalJPA user= sistUsuariosRemote.getUsuario(usuarioID);
		this.password = user.getPassword();
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;	 
	}	
	
	public void setUsuarioId(int id) {
		this.usuarioID = id;
	}
	
	public String getNuevoPassword() {
		return nuevoPassword;
	}

	public void setNuevoPassword(String nuevoPassword) {
		this.nuevoPassword = nuevoPassword;
	}
	
	public String getResultadoInc() {
		return resultadoInc;
	}

	public void setResultadoInc(String resultado) {
		this.resultadoInc = resultado;
	}
	public String getResultadoCor() {
		return resultadoCor;
	}

	public void setResultadoCor(String resultado) {
		this.resultadoCor = resultado;
	}
	
	/**
	 * Method used for Facelet to be able to do the submit
	 * @return Facelet name
	 * @throws Exception
	 */
	public void updatePassword() throws Exception
	{
		if (nuevoPassword.length()>0) {
			boolean res=false;
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		PersonalJPA user= sistUsuariosRemote.getUsuario(usuarioID);
    		res = sistUsuariosRemote.cambioPassword(user, nuevoPassword);
	        	if(res) {
	        		resultadoCor = " Actualitzado correctamente ";
	        		resultadoInc = "";	        		
	        		}
        		else {
        			resultadoInc = " No actualitzado ";
        			resultadoCor = "";
        		}
	            }
		else {
    		resultadoInc = " Escriba la nueva contraseña";
    		resultadoCor = "";
    		}
   		}
}
