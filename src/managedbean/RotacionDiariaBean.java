/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;


import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioPersonalJPA;
import jpa.CalendarioSeccionJPA;
import jpa.DiaPersonalJPA;
import jpa.EncargadoJPA;
import jpa.InstalacionJPA;
import jpa.OperarioJPA;
import jpa.PeriodoVacacionalJPA;
import jpa.PosicionRotacionJPA;
import jpa.PuestoJPA;
import jpa.RotacionJPA;
import jpa.SeccionAlmacenJPA;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaInstalacionesFacadeRemote;
import ejb.SistemaRotacionesFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * Managed Bean ListCAPsMBean
 */
@ManagedBean(name = "rotacionDiaria")
@ViewScoped
public class RotacionDiariaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private SistemaUsuariosFacadeRemote sistemaUsuariosRemote;
	private SistemaRotacionesFacadeRemote sistemaRotacionesRemote;
	private SistemaInstalacionesFacadeRemote sistemaInstalacionesRemote;
	private SistemaCalendariosFacadeRemote sistemaCalendariosRemote;
	private Collection<PosicionRotacionJPA> rotacionLista;
	private int screen = 0;
	protected Collection<PosicionRotacionJPA> rotacionListaVista;
	protected int numPosicion = 0;
	private int id;
	private String encargadoId="";
	private EncargadoJPA encargado;
	private SeccionAlmacenJPA seccion;
	private CalendarioSeccionJPA calendarioSeccion;
    private List <OperarioJPA> operarios= new ArrayList <OperarioJPA>();
    private Collection <InstalacionJPA> instalaciones;
    private int numOperarios=0;
    private List <PuestoJPA> puestos=new ArrayList<PuestoJPA> ();
    private int numPuestos=0;  
    private int numOperariosPuestos=0;
    private int maxPuesto=0;    
   // private int[][] asignacion= new int[numPuestos][maxPuesto];
	private RotacionJPA rotacion;
	private String resultadoInc="";
	private String resultadoCor="";	
	/**
	 * Constructor method
	 * @throws Exception
	 */
	public RotacionDiariaBean() throws Exception
	{
		try {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        setEncargadoId((String)session.getAttribute("id"));
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
		setRotacion(sistemaRotacionesRemote.crearRotacion(getSeccion().getTurno(),Date.valueOf(LocalDate.now()),getSeccion()));
		getOperariosL();
		getInstalaciones();
		Iterator<InstalacionJPA> iterInst= instalaciones.iterator();
		props = System.getProperties();
		ctx = new InitialContext(props);
		while (iterInst.hasNext()) {
			InstalacionJPA i= iterInst.next();
			Collection<PuestoJPA> puestosColeccion= null;
			sistemaInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
			puestosColeccion= sistemaInstalacionesRemote.getPuestos(i);
			Iterator<PuestoJPA> iterPues= puestosColeccion.iterator();
			while(iterPues.hasNext()) {
				PuestoJPA p= iterPues.next();
				puestos.add(p);
			}
		}
		setNumPuestos(puestos.size());
		Iterator<PuestoJPA> iterPues2= puestos.iterator();
		int numOperPuestos=0;
		while(iterPues2.hasNext()) {
			PuestoJPA p2= iterPues2.next();
			numOperPuestos=numOperPuestos+p2.getNumOperarios();
			if(maxPuesto<p2.getNumOperarios()) {
				maxPuesto=p2.getNumOperarios();
				}
			}
		setNumOperariosPuestos(numOperPuestos);
		}catch(Exception e) {
			resultadoInc="No se puede generar rotaciones: revise si existe personal e instalaciones asignadas";
		}
		rotacionLista();
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
	 * Method that returns an instance Collection of 10 or less SeccionAlmacen according screen 
	 * where the user is.
	 * @return Collection SeccionAlmacen
	 */
	public Collection<PosicionRotacionJPA> getRotacionListaVista()
	{
		int n =0;
		rotacionListaVista = new ArrayList<PosicionRotacionJPA>();
		for (Iterator<PosicionRotacionJPA> iter2 = rotacionLista.iterator(); iter2.hasNext();)
		{
			PosicionRotacionJPA cap = (PosicionRotacionJPA) iter2.next();
			if (n >= screen*15 && n< (screen*15+15))
			{				
				this.rotacionListaVista.add(cap);
			}
			n +=1;
		}
		this.numPosicion = n;
		return rotacionListaVista;
	}
	
	/**
	 * Returns the total number of instances of SeccionAlmacen
	 * @return CAPs number
	 */
	public int getNumeroSecciones()
	{ 
		return this.numPosicion;
	}
	
	/**
	 * allows forward or backward in user screens
	 */
	public void nextScreen()
	{
		if (((screen+1)*15 < rotacionLista.size()))
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
	 * Method used for Facelet to call rotacionListaVista Facelet
	 * @return Facelet name
	 * @throws NamingException 
	 * @throws Exception
	 */
	public void eliminar() throws NamingException {
	Properties props = System.getProperties();
	Context ctx = new InitialContext(props);
	sistemaRotacionesRemote = (SistemaRotacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaRotacionesFacade!ejb.SistemaRotacionesFacadeRemote");
	sistemaRotacionesRemote.eliminarRotacion(rotacion);
	resultadoInc="";
	resultadoCor="Rotación eliminada. Presiona botón confirmar";
	}
	
	
	public String listaRotacion() throws Exception
	{
		rotacionLista();
		return "rotacionListaVista";
	}
	
	/**
	 * Method used for Facelet to call AddCAPView Facelet
	 * @return Facelet name
	 */
	public String setCrearSeccion()
	{
		return "crearSeccionView";
	}
	
	/**
	 * Method used for Facelet to call UpdateCAPView Facelet
	 * @return Facelet name
	 */
	public String setModificaSeccion()
	{
		return "modificarSeccionView";
	}
	
	
	/**
	 * Method that takes a collection of instances of SeccionAlmacen calling 
	 * method listAllCAPs of the EJB Session
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void rotacionLista() 
	{		
		try {
		if (numOperariosPuestos>numOperarios) {
			resultadoInc="Operarios: "+operarios.size();
			resultadoCor="Puestos: "+puestos.size();			
			return;}
	    Collection <PuestoJPA> pos= puestos;
	    List <OperarioJPA> opsI= operarios;
	    List <OperarioJPA> opsF= operarios;
		Iterator <PuestoJPA> itsPo= puestos.iterator();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(Date.valueOf(LocalDate.now())); 
	    calendar.add(Calendar.DAY_OF_YEAR, -(int)(numOperarios*(2)));  // numero de días a añadir, o restar en caso de días<0
	    java.sql.Date f = new Date (calendar.getTimeInMillis());		
	    while (itsPo.hasNext()) {
	    	PuestoJPA p= itsPo.next();
			if(p.getRequerido()==true) {
				int operariosPuesto =p.getNumOperarios();
				List <OperarioJPA> opsSelect=new ArrayList<OperarioJPA>(sistemaRotacionesRemote.asignarPosicion(rotacion,p,opsI,f));				
				if(opsSelect!=null) {
					Iterator<OperarioJPA> it = opsSelect.iterator();
					while (it.hasNext()) {
						int i = it.next().getId();
						Iterator<OperarioJPA> it2 = operarios.iterator();
						while (it2.hasNext()) {
							int i2 = it2.next().getId();
							if(i==i2){
								it2.remove();
							}
						}
					}
					}

		        }
		}
		rotacionLista=sistemaRotacionesRemote.getRotacion(rotacion.getId());
		}catch(Exception e) {resultadoInc="CATCH ";}
		//resultadoInc="";
		//resultadoCor="OK";	
	}	

	public String getEncargadoId() {
		return encargadoId;
	}

	public void setEncargadoId(String encargadoId) {
		try {
		int id= Integer.valueOf(encargadoId);
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		setEncargado(sistemaUsuariosRemote.getEncargado(id));
		this.encargadoId = encargadoId;
		setSeccion(getEncargado().getSeccion());
		
		}catch(Exception e) {
			this.encargadoId = "";
		}
		return;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RotacionJPA getRotacion() {
		return rotacion;
	}

	public void setRotacion(RotacionJPA rotacion) {
		this.rotacion = rotacion;
	}

	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}

	public void setSeccion(SeccionAlmacenJPA secc) throws NamingException {
		this.seccion = secc;		
	}
	public CalendarioSeccionJPA getCalendarioSeccion() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		CalendarioEmpresaJPA calE= sistemaCalendariosRemote.getCalendarioEmpresa(LocalDate.now().getYear());
		CalendarioSeccionJPA calendarioSeccion=sistemaCalendariosRemote.getCalendarioSeccionAno(calE.getId(), seccion);
		return calendarioSeccion;
	}

	public void setCalendarioSeccion(CalendarioSeccionJPA cal) throws NamingException {
		this.calendarioSeccion = cal;		
	}
	public List <OperarioJPA> getOperariosL() throws NamingException {
		try {
		List <OperarioJPA> noDisponible=new ArrayList<OperarioJPA>();
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		List <OperarioJPA> opers=new ArrayList<OperarioJPA>(sistemaUsuariosRemote.getOperarioSeccion(seccion));
		Iterator <OperarioJPA> iterOp1= opers.iterator();
		while(iterOp1.hasNext()) {
			OperarioJPA it1=iterOp1.next();
			Properties props2 = System.getProperties();
			Context ctx2 = new InitialContext(props2);
			sistemaCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx2.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
			CalendarioPersonalJPA calP= sistemaCalendariosRemote.getCalendarioPersonal(getCalendarioSeccion(), it1);
			Collection <DiaPersonalJPA> dps=  sistemaCalendariosRemote.getDiasPersonales(calP);
			if (dps.isEmpty()==false) {
	    		Iterator <DiaPersonalJPA> diasP= dps.iterator();
		    	while(diasP.hasNext()) {
		    		DiaPersonalJPA d= diasP.next();
		    		if(d.getDate().equals(Date.valueOf(LocalDate.now()))) {
			    		noDisponible.add(it1);
			    		}
	    			}
		    	}
			Collection<PeriodoVacacionalJPA> vacOp=sistemaCalendariosRemote.getPeriodoVacacional(calP);
			if (vacOp.isEmpty()==false) {			
	    		Iterator <PeriodoVacacionalJPA> iterVacOp= vacOp.iterator();
	    		while(iterVacOp.hasNext()) {
		    		PeriodoVacacionalJPA p1=iterVacOp.next();
		    		if(LocalDate.now().isAfter(p1.getPrimerDia().toLocalDate()) & LocalDate.now().isBefore(p1.getUltimoDia().toLocalDate())){
			    		noDisponible.add(it1);
			    		}
			    	}
    			}
			}
			if(noDisponible!=null) {
				opers.removeAll(noDisponible);
			}
    	    operarios=opers;
    	    setNumOperarios(operarios.size());
    	    }catch(Exception e) {return null;}
            return operarios;
        }

	public List <OperarioJPA> getOperarios( ) {
		return this.operarios;
	}
	
	public void setOperarios(List <OperarioJPA> operarios) {
		this.operarios = operarios;
	}

	public Collection <PuestoJPA> getPuestos() throws NamingException {
		return puestos;
	}

	public void setPuestos(List <PuestoJPA> puestos) {
		this.puestos = puestos;
	}

	public int getNumOperarios() {
		return numOperarios;
	}

	public void setNumOperarios(int numOperarios) {
		this.numOperarios = numOperarios;
	}

	public int getNumOperariosPuestos() {
		return numOperariosPuestos;
	}

	public void setNumOperariosPuestos(int numOperarios) {
		this.numOperariosPuestos = numOperarios;
	}
	
	public int getNumPuestos() {
		return numPuestos;
	}

	public void setNumPuestos(int numPuestos) {
		this.numPuestos = numPuestos;
	}

	public EncargadoJPA getEncargado() {
		return encargado;
	}

	public void setEncargado(EncargadoJPA encargado) {
		this.encargado = encargado;
	}

	public Collection <InstalacionJPA> getInstalaciones() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistemaInstalacionesRemote = (SistemaInstalacionesFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaInstalacionesFacade!ejb.SistemaInstalacionesFacadeRemote");
		instalaciones=sistemaInstalacionesRemote.getInstalaciones(getSeccion()) ;
		return instalaciones;
	}

	public void setInstalaciones(Collection <InstalacionJPA> instalaciones) {
		this.instalaciones = instalaciones;
	}
}
