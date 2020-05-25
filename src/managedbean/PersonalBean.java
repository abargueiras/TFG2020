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
@ManagedBean(name = "personal")
@ViewScoped
public class PersonalBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	protected PersonalJPA personal;
	protected RrhhJPA dataRrhh;
	protected JefeAlmacenJPA dataJefeAlmacen;
	protected EncargadoJPA dataEncargado;
	protected JefeEquipoJPA dataJefeEquipo;
	protected OperarioJPA dataOperario;
	protected PersonalJPA dataPersonal;
	//Mantenimiento usuarios
	private int id;
	private String nif;	
	private String nombre;
	private String apellidos;
	private String password;
	private String email;
	private int secId=-1;
	private SeccionAlmacenJPA seccion;
	private SeccionAlmacenJPA seccionN;
	private List<SeccionAlmacenJPA> secciones;
	private String seccionId = "";
	private String nombreSeccion = "";
	private boolean seleccionadaSeccion=false;
	private String elegirSeccion;
	private Turno turno;
	private String elegirTurno="";
	private Date fechaAlta;
	private Date fechaBaja;
	protected String nuevoRol = "";
	protected String resultadoINC = "";
	protected String resultadoCOR = "";
	
	public PersonalBean() throws Exception 
	{
			setDataPersonal();
			cargarSecciones();
	}

	public void cargarSecciones() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		Collection<SeccionAlmacenJPA> es =sistUsuariosRemote.getSecciones();
		secciones= new ArrayList<SeccionAlmacenJPA>(es);
	}
	public List<SeccionAlmacenJPA> getSecciones() throws NamingException {
		return secciones;
	}
	public void setSecciones(List<SeccionAlmacenJPA> sec) {
		this.secciones = sec;
	}
	public String getSeccionId() {
		return seccionId;
	}

	public void setSeccionId(String id) throws NamingException {
		try {
			Integer.parseInt(id);
		}catch (Exception e) {
			this.seccionId = "";
			return;
		}
		if((id=="")||(id==null)) {
			this.seccionId = "";
			return;
		}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		this.seccion=sistUsuariosRemote.getSeccion(Integer.parseInt(id));
		this.seccionId = id;
	}
	public void nomSeccion() {
		this.seccionId =String.valueOf(this.seccion.getId());
		setSeleccionadaSeccion(true);
	}

	/**
	 * Atributos usados en mantenimiento usuarios
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int Id) throws Exception {
		this.id = Id;
		setDataPersonal();
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String Nif) {
		this.nif = Nif;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String Nombre) {
		this.nombre = Nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String Apellidos) {
		this.apellidos = Apellidos;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String Password) {
		this.password = Password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String Email) {
		this.email = Email;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date FechaAlta) {
		this.fechaAlta = FechaAlta;
	}

	public Date getfechaBaja() {
		return fechaBaja;
	}

	public void setfechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}
	public void setSeccion(SeccionAlmacenJPA sec) {
		this.seccion = sec;
		this.elegirSeccion= sec.getNombre();
	}
	
	public SeccionAlmacenJPA getSeccionN() {
		return seccionN;
	}
	
	public String getElegirSeccion() {
		return elegirSeccion;
	}
	public void setElegirSeccion(String elegirSeccion) throws NamingException {
		if (elegirSeccion.equals("") || elegirSeccion.equals("NO" )) {
			this.seccion=null;
			this.elegirSeccion = "";
			return;
		}
		else {
    		this.elegirSeccion = elegirSeccion;
    		Properties props = System.getProperties();
    		Context ctx = new InitialContext(props);
    		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
    		secId = sistUsuariosRemote.getSeccionNombr(elegirSeccion);
    		setSeccion(sistUsuariosRemote.getSeccion(secId)); 
    		return;
    		}
	}
	

	public void setTurno(Turno tur) {
		this.turno = tur;
	}
	public String getElegirTurno() {
		return elegirTurno;
	}

	public void setElegirTurno(String tur) {
		if (tur.equals("NO")) {
			this.turno = null;
     	}
		else if(tur.equals("Mañana")) {
			this.turno = Turno.Mañana;
		}
		else if(tur.equals("Tarde")) {
			this.turno  = Turno.Tarde;
		}
		else if(tur.equals("Noche")) {
			this.turno = Turno.Noche;
		}
		else {
			this.turno = null;
		}
		this.elegirTurno = tur;
	}	
	
	
	
	public String getNuevoRol() {
		return nuevoRol;
	}

	public void setNuevoRol(String rol) throws Exception {
		this.nuevoRol = rol.toString();		
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
	public void setDataPersonal() throws Exception
	{	
		dataPersonal = new PersonalJPA( );
	}
	
	public OperarioJPA getDataOperario()
	{
		return dataOperario;
	}	
	public void setDataOperario() throws Exception
	{	
		dataOperario = new OperarioJPA( );
	}

	public JefeEquipoJPA getDataJefeEquipo()
	{
		return dataJefeEquipo;
	}	
	public void setDataJefeEquipo() throws Exception
	{	
		dataJefeEquipo = new JefeEquipoJPA( );
	}	
	public EncargadoJPA getDataEncargado()
	{
		return dataEncargado;
	}	
	public void setDataEncargado() throws Exception
	{	
		dataEncargado = new EncargadoJPA( );
	}	
	public JefeAlmacenJPA getDataJefeAlmacen()
	{
		return dataJefeAlmacen;
	}	
	public void setDataJefeAlmacen() throws Exception
	{	
		dataJefeAlmacen = new JefeAlmacenJPA( );
	}
	public RrhhJPA getDataRrhh()
	{
		return dataRrhh;
	}	
	public void setDataRrhh() throws Exception
	{	
		dataRrhh = new RrhhJPA( );
	}
    
	public void crearPersonal() throws Exception {
		if(nuevoRol=="rrhh" || nuevoRol=="jefeAlmacen") {
			this.seccion=null;
			turno=null;
		}
		if((nuevoRol=="operario" || nuevoRol=="jefePersonal")&&(turno==null)) {
			resultadoINC = "Seleccione un turno de trabajo";
			resultadoCOR = "";
		}
		if((nuevoRol=="operario" || nuevoRol=="jefePersonal" || nuevoRol=="encargado")&&(seccion==null)) {
			resultadoINC = "Seleccione una sección de trabajo";
			resultadoCOR = "";
		}		
		String res="";
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		setId(nuevoIdentificadorPersonal());
		res= sistUsuariosRemote.crearPersonal(id,nif, nombre, apellidos, password,email,nuevoRol, seccion, turno);
        	if(res=="SI") {
        		resultadoCOR = "Usuario Creado: " +id;
        		resultadoINC = "";	        		
        		}
    		else {
    			resultadoINC = res;
    			resultadoCOR = "";
    			}
        	setDataOperario();
	}
	
	public void eliminarPersonal() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		res= sistUsuariosRemote.eliminarPersonal(id);
		if(res) {
    		resultadoCOR = " Personal eliminado ";
    		resultadoINC = "";	        		
    		}
		else {
			resultadoINC = " ¡ERROR AL ELIMINAR! ";
			resultadoCOR = "";
			}
    	setDataPersonal();
        }
	
	public void verPersonal() throws Exception
	{
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		dataPersonal= sistUsuariosRemote.getUsuario(id);
        }
	
	private int nuevoIdentificadorPersonal() throws NamingException {
		int i=0;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		i= sistUsuariosRemote.nuevoIdPersonal();
		return i;
	}

	public boolean isSeleccionadaSeccion() {
		return seleccionadaSeccion;
	}

	public void setSeleccionadaSeccion(boolean seleccionadaSeccion) {
		this.seleccionadaSeccion = seleccionadaSeccion;
	}

	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}

}

