/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.sql.Date;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.EncargadoJPA;
import jpa.JefeAlmacenJPA;
import jpa.JefeEquipoJPA;
import jpa.OperarioJPA;
import jpa.PersonalJPA;
import jpa.RrhhJPA;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;

/**
 * Managed Bean PersonalMBean
 */
@ManagedBean(name = "updatepersonal")
@ViewScoped
public class UpdatePersonalBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	protected PersonalJPA dataPersonal;
	protected PersonalJPA dataPersonalN;
	protected SeccionAlmacenJPA dataSeccion;
	protected SeccionAlmacenJPA dataSeccionN;
	private String nombreSeccion="";
	private String nombreSeccionN="";
	protected Turno turno;
	protected Turno turnoN;
	protected String nombreTurno;
	protected String nombreTurnoN;
	protected String rol = "";
	protected String rolN = "";
	private int id;
	protected String resultadoINC = "";
	protected String resultadoCOR = "";
	
	public UpdatePersonalBean() throws Exception 
	{    
	}

	/**
	 * Atributos usados en mantenimiento usuarios
	 * @return
	 * @throws Exception 
	 */
	public int getId() throws Exception {
		return id;
	}

	public void setId(int Id) throws Exception {
		this.id = Id;
		setDataPersonal(id);
		setDataPersonalN(id);
		setRol(id);
		rolN=rol;
		if (rol.equals("operario")||rol.equals("jefeEquipo")) {
			setTurno(id,rol);
			turnoN=turno;
			nombreTurno=turno.name();
			nombreTurnoN=nombreTurno;
			setDataSeccion(id);
			setDataSeccionN(id);
			nombreSeccion= dataSeccion.getNombre();
			nombreSeccionN=nombreSeccion;
		}
		else if (rol.equals("encargado")) {
			turno=null;
			turnoN=turno;
			nombreTurno="";
			nombreTurnoN="";
			setDataSeccion(id);
			setDataSeccionN(id);
			nombreSeccion= dataSeccion.getNombre();
			nombreSeccionN=nombreSeccion;
		}else {
			turno=null;
			turnoN=turno;
			nombreTurno="";
			nombreTurnoN="";
			dataSeccion=null;
			dataSeccionN=null;
			nombreSeccion= "";
			nombreSeccionN=nombreSeccion;
		}
		
	}

	public String getNombreSeccionN() {
		return nombreSeccionN;
	}
	public void setNombreSeccionN(String elegida) throws NamingException {
		if (elegida.equals(null) || elegida.equals("")) {
			this.nombreSeccionN = "";
			dataSeccionN=null;
			return;
		}
		else {
     		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		SeccionAlmacenJPA esSeccion=sistUsuariosRemote.getSeccionPorNombre(elegida);
    		if (esSeccion!=null) {
    			dataSeccionN=esSeccion;
    	   		this.nombreSeccionN = elegida;
    	   		}
    		else {
    			this.nombreSeccionN = "";
    			dataSeccionN=null;
    		}
    		return;
    		}
	}
	
	public void setTurno(int id, String rol) throws NamingException {
		if(rol.equals("operario")  || rol.equals("jefeEquipo")) {
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		turno = sistUsuariosRemote.getTurnoEmpleado(id,rol);
		}else {
			turno=null;
		}
		return;
	}
	public Turno getTurno() {
		return this.turno;
	}
	public void setTurno(Turno tur) {		
		this.turno=tur;
	}
	public Turno getTurnoN() {
		return turnoN;
	}
	public void setTurnoN(Turno tur) {		
		this.turnoN=tur;
	}
	public String getNombreTurno() {
		return nombreTurno;
	}
	public void setNombreTurno(String tur) {
		this.nombreTurno=tur;
	}
	public String getNombreTurnoN() {
		return nombreTurnoN;
	}
	public void setNombreTurnoN(String tur) {
		if(tur.equals("Sin turno")) {
			this.nombreTurnoN="";
			turnoN=null;
		}
		else if(tur.equals("Mañana")) {
			this.nombreTurnoN=tur;
			turnoN=Turno.Mañana;
		}
		else if(tur.equals("Tarde")) {
			this.nombreTurnoN=tur;
			turnoN=Turno.Tarde;
		}
	    else if(tur.equals("Noche")) {
			this.nombreTurnoN=tur;
			turnoN=Turno.Noche;
		}
	}
	public String getRol() throws Exception {             
		return rol;
	}
	public void setRol(int id) throws Exception {    
		this.rol= sistUsuariosRemote.getTipoPersonal(id);
	}
	public String getRolN() {
		return rolN;
	}

	public void setRolN(String r) throws Exception {
			if (r.equals("operario")) {
    			this.rolN="operario";
         	}
    		else if(r.equals("jefeEquipo")) {
	    		this.rolN="jefeEquipo";
    		}
    		else if(r.equals("encargado")) {
   	    		this.rolN="encargado";
	    		setNombreTurnoN("Sin turno");
	    		if(rol.equals("encargado")==false) {
	    			setNombreSeccionN("");
	    			dataSeccionN=null;
	    			}
    		}
    		else if(r.equals("jefeAlmacen")) {
    			this.rolN="jefeAlmacen";
	    		setNombreTurnoN("Sin turno");
	    		setNombreSeccionN("");
	    	}
    		else if(r.equals("rrhh")) {
	    		this.rolN="rrhh";
	    		setNombreTurnoN("Sin turno");
	    		setNombreSeccionN("");
	    	}	
	    	else {
	    		this.rolN="";
	    		setNombreTurnoN("Sin turno");
	    		setNombreSeccionN("");
    	   	}
	}
	
	public String getResultadoINC() {
		return resultadoINC;
	}

	public void setResultadoINC(String res) {
		this.resultadoINC = res;
	}
	
	public String getResultadoCOR() {
		return resultadoCOR;
	}

	public void setResultadoCOR(String res) {
		this.resultadoCOR = res;
	}
	
	/**
	 * Metodos usados por Facelet personal
	 * @return Facelet name
	 * @throws Exception
	 */
	
	public PersonalJPA getDataPersonal()
	{
		return dataPersonal;
	}	
	
	public void setDataPersonal(int id) throws Exception
	{	
		dataPersonal = new PersonalJPA( );
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataPersonal = (PersonalJPA) sistUsuariosRemote.getUsuario(id);		
		}
	public PersonalJPA getDataPersonalN()
	{
		return dataPersonalN;
	}	
	
	public void setDataPersonalN(int id) throws Exception
	{	
		dataPersonalN = new PersonalJPA( );
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataPersonalN = (PersonalJPA) sistUsuariosRemote.getUsuario(id);		
		}	
	public void setDataSeccion(int id) throws Exception
	{	
		dataSeccion = new SeccionAlmacenJPA( );
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataSeccion = (SeccionAlmacenJPA) sistUsuariosRemote.getSeccionEmpleado(id);
		setNombreSeccion(dataSeccion.getNombre());
	}
	public SeccionAlmacenJPA getDataSeccionN()
	{
		return dataSeccionN;
	}	
	public void setDataSeccionN(int id) throws Exception
	{	
		dataSeccionN = new SeccionAlmacenJPA( );
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataSeccionN = (SeccionAlmacenJPA) sistUsuariosRemote.getSeccionEmpleado(id);
		setNombreSeccion(dataSeccionN.getNombre());
	}
	public SeccionAlmacenJPA getDataSeccion()
	{
		return dataSeccion;
	}
	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public void setNombreSeccion(String nombreSeccion) throws NamingException {
		this.nombreSeccion = nombreSeccion;
	}

	public void modificarPersonal() throws Exception
	{		
		//String a="";
		resultadoINC = "";		
		resultadoCOR = "";
		boolean res = true;
		if (rolN.equals(null)||rolN.equals("")){
			resultadoINC = " Selecciona categoria";
			resultadoCOR = "";
			return;
			}
		if(nombreSeccionN.equals("")){
			if (rolN.equals("encargado")|| rolN.equals("operario") || rolN.equals("jefeEquipo")){
				resultadoINC = " Selecciona sección de trabajo";
				resultadoCOR = "";
				return;
				}
		}
		if(nombreTurnoN.equals("")){
			if (rolN.equals("jefeEquipo")|| rolN.equals("operario")){
				resultadoINC = " Selecciona turno de trabajo";
				resultadoCOR = "";
				return;
				}
		}
		if (rolN.equals("operario") || rolN.equals("jefeEquipo")) {
			if (existeSeccionTurno(nombreSeccionN, nombreTurnoN)==false) {
				resultadoINC = " NO existe la sección y turno elegido";
				resultadoCOR = "";
				return;
				}
			}
		if (rolN.equals("encargado")) {
			if ((existeSeccion(nombreSeccionN)==false)) {
				resultadoINC = " NO existe la sección";
				resultadoCOR = "";
				return;
				}
		}
		boolean mismoRol = rol.equals(rolN);
		Date dataAlta=null;
		if(mismoRol==false) {
			dataAlta = fechaAlta(dataPersonal.getId());
			eliminarUsuario(dataPersonal.getId());
			crearUsuario(dataPersonal.getId());
			recuperarFechaAlta(rolN, dataAlta);
			}
		else {
			Properties props = System.getProperties();
			Context ctx = new InitialContext(props);
			sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		if (rolN.equals("operario") || rolN.equals("jefeEquipo")) {
    			res= sistUsuariosRemote.updatePersonal(dataPersonalN.getId(),dataPersonalN.getNif(), dataPersonalN.getnombre(), dataPersonalN.getapellidos(),dataPersonalN.getPassword(),dataPersonalN.getEmail(),rolN, dataSeccionN, turnoN);
    			}
    		else if (rolN.equals("encargado")) {
    			res= sistUsuariosRemote.updatePersonal(dataPersonalN.getId(),dataPersonalN.getNif(), dataPersonalN.getnombre(), dataPersonalN.getapellidos(),dataPersonalN.getPassword(),dataPersonalN.getEmail(),rolN, dataSeccionN, null);
    			}
	    	else if (rolN.equals("jefeAlmacen") || rolN.equals("rrhh"))  {
	    		res= sistUsuariosRemote.updatePersonal(dataPersonalN.getId(),dataPersonalN.getNif(), dataPersonalN.getnombre(), dataPersonalN.getapellidos(),dataPersonalN.getPassword(),dataPersonalN.getEmail(),rolN, null, null);
	    		}
    		}

		try {
			if(res==true) {
				resultadoCOR = " Empleado modificado ";
        		resultadoINC = ""; 		
        		rol=rolN;
        		if (rolN.equals("operario")||rolN.equals("jefeEquipo")){
        			nombreTurno=nombreTurnoN;
        			turno=turnoN;
        			dataSeccion=dataSeccionN;
        			nombreSeccion=nombreSeccionN;
        			}
        		else if (rolN.equals("encargado")){
        			nombreTurno="";
        			turno=null;
        			dataSeccion=dataSeccionN;
        			nombreSeccion=nombreSeccionN;
        			}
        		else {
        			nombreTurno="";
        			turno=null;
        			dataSeccion=null;
        			nombreSeccion="";
        			}
        		}
		else {
			resultadoINC = "\n -->  Error en la  modificación rol " +res;		
			resultadoCOR = "";		
    		}
		
    }catch (Exception e) {
		    resultadoINC = " Revisa datos solicitados"+rol+" sec  "+dataSeccion.getNombre()
			+ "  turno  "+ turno.name() + " con rol "+rolN +" sec  "+dataSeccionN.getNombre()
			+ "  turno  "+ turnoN.name() + "   DATOS  "
			+dataPersonalN.getId()+" nif "+dataPersonalN.getNif()+" nom "
			+ dataPersonalN.getnombre()+" apel "+ dataPersonalN.getapellidos()
			+" pass "+dataPersonalN.getPassword()+" ema "+dataPersonalN.getEmail()
			+" rol "+rolN+" sec "+ dataSeccionN+"  tur "+turnoN;			
		    resultadoCOR = "";
		    }
    }
	private boolean existeSeccion(String nombreSeccionN2) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		SeccionAlmacenJPA existeSec=sistUsuariosRemote.getSeccionPorNombre(nombreSeccionN);
		if (existeSec!=null)return true;
		return false;
	}

	private boolean existeSeccionTurno(String nombreSeccionN, String nombreTurnoN) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		boolean	existeSecTurno= sistUsuariosRemote.existeSeccionTurno(nombreSeccionN, nombreTurnoN);
		return existeSecTurno;
		}

	private Date recuperarFechaAlta(String rolN, Date dataAlta) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		Date data=null;
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if(rolN.equals("operario")) {
    		OperarioJPA op = sistUsuariosRemote.getOperario(dataPersonal.getId());
    		op.setFechaAlta(dataAlta);
    		data = op.getFechaAlta();
         	}
    	else if(rolN.equals("jefeEquipo")) {
	    	JefeEquipoJPA op = sistUsuariosRemote.getJefeEquipo(dataPersonal.getId());
	    	op.setFechaAlta(dataAlta);
	    	data = op.getFechaAlta();
	    	}
    	else if(rolN.equals("encargado")) {
	        EncargadoJPA op = sistUsuariosRemote.getEncargado(dataPersonal.getId());
	        op.setFechaAlta(dataAlta);
	        data = op.getFechaAlta();
	    	}
    	else if(rolN.equals("jefeAlmacen")) {
	    	JefeAlmacenJPA op = sistUsuariosRemote.getJefeAlmacen(dataPersonal.getId());
	    	op.setFechaAlta(dataAlta);
	    	data = op.getFechaAlta();
        	}
    	else if(rolN.equals("rrhh")) {
    		RrhhJPA op = sistUsuariosRemote.getRrhh(dataPersonal.getId());
    		op.setFechaAlta(dataAlta);
    		data = op.getFechaAlta();
        	}	
		return data;
	}

	public boolean eliminarUsuario(int id) throws NamingException {
		boolean resultado=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		resultado=sistUsuariosRemote.eliminarPersonal(id);
		return resultado;
		}
	
	public boolean crearUsuario(int id) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);		
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		String result=sistUsuariosRemote.crearPersonal(dataPersonalN.getId(), dataPersonalN.getNif(), dataPersonalN.getnombre(),
				dataPersonalN.getapellidos(),dataPersonalN.getPassword(), dataPersonalN.getEmail(), rolN, dataSeccionN, turnoN);
		if (result.equals("SI"))return true;
		return false;
		}
	
	public Date fechaAlta(int id) throws NamingException {
		Date dataAlta=null;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		if(rol.equals("operario")) {
    		OperarioJPA op = sistUsuariosRemote.getOperario(dataPersonal.getId());
    		dataAlta=op.getFechaAlta();
         	}
    	else if(rol.equals("jefeEquipo")) {
	    	JefeEquipoJPA op = sistUsuariosRemote.getJefeEquipo(dataPersonal.getId());
	    	dataAlta=op.getFechaAlta();
	    	}
    	else if(rol.equals("encargado")) {
	        	EncargadoJPA op = sistUsuariosRemote.getEncargado(dataPersonal.getId());
		    	dataAlta=op.getFechaAlta();
	    	}
    	else if(rol.equals("jefeAlmacen")) {
	    	JefeAlmacenJPA op = sistUsuariosRemote.getJefeAlmacen(dataPersonal.getId());
    		dataAlta=op.getFechaAlta();
        	}
    	else if(rol.equals("rrhh")) {
    		RrhhJPA op = sistUsuariosRemote.getRrhh(dataPersonal.getId());
    		dataAlta=op.getFechaAlta();
        	}
		return dataAlta;
		}
}


		


