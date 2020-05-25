/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioPersonalJPA;
import jpa.CalendarioSeccionJPA;
import jpa.DiaPersonalJPA;
import jpa.OperarioJPA;
import jpa.PeriodoVacacionalJPA;
import jpa.SeccionAlmacenJPA;
import jpa.TipoVacaciones;
import jpa.TurnoVacacionalJPA;


@ManagedBean(name = "calendarioPersonal")
@ViewScoped
public class CalendarioPersonalBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistCalendariosRemote;	
	private String idCal="";
	private String fechaMaxPropuestaVacacional="";
	private int anoCal=0;
	private int maxDiasFestivos=0;
	private String idOp="";
	private OperarioJPA operario=null;
	private SeccionAlmacenJPA sec=null;
	private CalendarioPersonalJPA calendarioPersonal=null;
	private CalendarioSeccionJPA calendarioSeccion=null;
	private CalendarioEmpresaJPA calE=null;
	private DiaPersonalJPA diaPersonal1;
	private DiaPersonalJPA diaPersonal2;
	private DiaPersonalJPA diaPersonal3;
	private DiaPersonalJPA diaPersonal4;
	private DiaPersonalJPA diaPersonal5;
	private DiaPersonalJPA diaPersonal6;
	private String numDiaPersonal1="";
	private String numDiaPersonal2="";
	private String numDiaPersonal3="";
	private String numDiaPersonal4="";	
	private String numDiaPersonal5="";
	private String numDiaPersonal6="";	
	private Collection<DiaPersonalJPA> diasPersonales=null;
	private Collection<PeriodoVacacionalJPA> periodoVacacional=null;
	private PeriodoVacacionalJPA periodoVacacionalVerano=null;
	private PeriodoVacacionalJPA periodoVacacionalInvierno=null;
	private String iniTurnoV="";
	private String finTurnoV="";
	private String iniTurnoI="";
	private String finTurnoI="";
	protected String resultadoINC = "";
	protected String resultadoCOR = "";
	protected String resultadoEnTramite = "";
	protected String resultadoAprobado = "";
	
	private String festivoSolicitado="";
	private String motivo="";	
	//SEL turnos
	private boolean seleccionadaSeccionVer=false;
	private boolean seleccionadaSeccionInv=false;
	private boolean pedidoVer=false;
	private boolean pedidoInv=false;
	private String turnoVerFecha="";
	private String turnoInvFecha="";
	private Collection<TurnoVacacionalJPA> turnosVacacionales=null;
	private TurnoVacacionalJPA turnoVer;
	private TurnoVacacionalJPA turnoInv;	
	protected List<TurnoVacacionalJPA> periodosVacaVer=null;
	protected List<TurnoVacacionalJPA> periodosVacaInv=null;
	private String[] turnosV= new String[11]; 
	private String[] turnosI= new String[11]; 
	private String turnoV1="";
	private String turnoV2="";
	private String turnoV3="";
	private String turnoV4="";
	private String turnoV5="";
	private String turnoV6="";
	private String turnoV7="";
	private String turnoV8="";
	private String turnoV9="";
	private String turnoV10="";
	private String turnoI1="";
	private String turnoI2="";
	private String turnoI3="";
	private String turnoI4="";
	private String turnoI5="";
	private String turnoI6="";
	private String turnoI7="";
	private String turnoI8="";
	private String turnoI9="";
	private String turnoI10="";
	
	
	
	public CalendarioPersonalBean() throws NamingException {
		setAnoCal(LocalDate.now().getYear());
		setIdCal(String.valueOf(anoCal));
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		setIdOp((String)session.getAttribute("id"));
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote)ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote)ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		calE= sistCalendariosRemote.getCalendarioEmpresa(LocalDate.now().getYear());
		if (Date.valueOf(LocalDate.now()).before(calE.getlimitePropuestaVacaciones())==false) {
			if(calE.isPropuestaCerrada()==false) {
				sistCalendariosRemote.cerrarPropuestaVacacional();
			}
		}
		setOperario(sistUsuariosRemote.getOperario(Integer.parseInt(idOp)));
		setSec(sistUsuariosRemote.getSeccionEmpleado(Integer.parseInt(idOp)));
		setCalendarioSeccion(sistCalendariosRemote.getCalendarioSeccionAno(anoCal, sec));
		setMaxDiasFestivos(calendarioSeccion.getOperariosTurnoVacaciones());
		setCalendarioPersonal(sistCalendariosRemote.getCalendarioPersonal(calendarioSeccion, operario));
		if(sistCalendariosRemote.getDiasPersonales(calendarioPersonal)!=null) {
		setDiasPersonales(sistCalendariosRemote.getDiasPersonales(calendarioPersonal));}
		if(sistCalendariosRemote.getTurnoVacacional(getAnoCal())!=null) {
		setTurnosVacacionales(sistCalendariosRemote.getTurnoVacacional(getAnoCal()));}
		cargarTurnosV();
		cargarTurnosI();	
		fechaMaxPropuestaVacacional=calE.getlimitePropuestaVacaciones().toString();
		if(sistCalendariosRemote.getPeriodoVacacionalVerano(calendarioPersonal)!=null) {
			setPeriodoVacacionalVerano(sistCalendariosRemote.getPeriodoVacacionalVerano(calendarioPersonal));
		}
		if(sistCalendariosRemote.getPeriodoVacacionalInvierno(calendarioPersonal)!=null) {
			setPeriodoVacacionalInvierno(sistCalendariosRemote.getPeriodoVacacionalInvierno(calendarioPersonal));
		}
		if((turnosVacacionales== null) ||(turnosVacacionales.isEmpty())) {
			resultadoINC =" No estan creados los turnos";
			return;
		}
	    }
	private void cargarTurnosV(){
		try {
			for (int i = 0; i < 11; ++i) {
		        turnosV[i]="";
		        }
			int i =1;
			Iterator <TurnoVacacionalJPA> it = periodosVacaVer.iterator();
			while(it.hasNext()) {
				  String x=it.next().getFechaInicio().toString();
				  String xx=calcularFinTurno(x).toString();
				  turnosV[i]=x+" a "+xx;
			      i++;
			      }
			}catch (Exception e) {return;}
		}
	private void cargarTurnosI(){
		try {
			for (int i = 0; i < 11; ++i) {
				turnosI[i]="";
	        }
			int i =1;
    		Iterator <TurnoVacacionalJPA> it = periodosVacaInv.iterator();
     		while(it.hasNext()) {
     			  String x=it.next().getFechaInicio().toString();
	    		  String xx=calcularFinTurno(x).toString();
	    		  turnosI[i]=x+" a "+xx;
	    	      i++;
		          }
		    }catch (Exception e) {return;}
	}		
	private Date calcularFinTurno(String d) {
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(Date.valueOf(d)); // Configuramos la fecha que se recibe
	      calendar.add(Calendar.DAY_OF_YEAR, 15);  // numero de días a añadir, o restar en caso de días<0
	      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	      String formatted = format1.format(calendar.getTime());
	      return Date.valueOf(formatted);
	}
	/***
	 * AQUI ME QUEDE
	 * 
	 */
	public void turnoSeccionVer() {
		seleccionadaSeccionVer=true;
	}
	public void turnoSeccionInv() {
		seleccionadaSeccionInv=true;
	}
	public void salvarCalendarioVacacional() throws NamingException {
		resultadoINC = "";
		resultadoCOR = "";
		boolean creado = false;
		boolean creado2=false;
		if(sistCalendariosRemote.getPeriodoVacacionalVerano(calendarioPersonal)!=null) {
			setPeriodoVacacionalVerano(sistCalendariosRemote.getPeriodoVacacionalVerano(calendarioPersonal));
			setPedidoVer(true);
		}
		else {setPedidoVer(false);}
		if(sistCalendariosRemote.getPeriodoVacacionalInvierno(calendarioPersonal)!=null) {
			setPeriodoVacacionalInvierno(sistCalendariosRemote.getPeriodoVacacionalInvierno(calendarioPersonal));
			setPedidoInv(true);
		}
		else {setPedidoInv(false);}
		try {
		if(isPedidoVer()==true||isPedidoInv()==true) {
			resultadoINC = "No se pudo realizar la operación. Existe una solicitud previa";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return;
		}	
	
		if (seleccionadaSeccionVer==false||seleccionadaSeccionInv==false) {
			resultadoINC = "Confirme las dos selecciones";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return;
		}
		if(calE.isPropuestaCerrada()==true) {	
		    resultadoINC = "Plazo de selección cerrado";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return;
			}
		if((turnoVerFecha=="")||(turnoInvFecha=="")) {
			resultadoINC = "Falta seleccionar turnos";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return;
			}
		creado = sistCalendariosRemote.solicitarPeriodoVacacional(calendarioPersonal,periodoVacacionalVerano);
		creado2 = sistCalendariosRemote.solicitarPeriodoVacacional(calendarioPersonal,periodoVacacionalInvierno);
		}catch(Exception e) {
			resultadoINC = "Error en creacion";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return ;
			}
		if ((creado==false)||(creado2==false)){
			resultadoINC = "Ya existía solicitud previa. Contacta con RRHH";
			resultadoCOR = "";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			return;
		}
		else {
			resultadoINC = "";
			resultadoCOR = "Turnos salvados";
			seleccionadaSeccionVer=false;
			seleccionadaSeccionInv=false;
			pedidoInv=true;
			pedidoVer=true;
			return;
		}
	}
	public int diasPersonalesAsignados() throws NamingException {
		try {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote)ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		Collection<DiaPersonalJPA> a= sistCalendariosRemote.getDiasPersonales(calendarioPersonal);
		return a.size();
		}catch(Exception e) {return 0;}
	}
	
	public boolean asignarDia(SeccionAlmacenJPA sec, OperarioJPA operario, CalendarioPersonalJPA calendarioPersonal, java.sql.Date valueOf, String motivo) throws NamingException {
		boolean creado =false;
		try {
			Properties props = System.getProperties();
			Context ctx = new InitialContext(props);
			sistCalendariosRemote = (SistemaCalendariosFacadeRemote)ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
			creado = sistCalendariosRemote.solicitarDiaPersonal(sec,operario,calendarioPersonal,valueOf,motivo);
		}catch(Exception e) {return false;}
		return creado;
	}
	
	public void solicitarFestivo() throws NamingException {
		maxDiasFestivos=(calE.getNumDiasPersonales());
		resultadoINC = "";
		resultadoCOR = "";
		try {
			int a=diasPersonalesAsignados();
		if((a> (maxDiasFestivos-1))) {
			resultadoINC = "Dias agotados";
			return;
		}
		if (Date.valueOf(festivoSolicitado).before(Date.valueOf(LocalDate.now()))){
			resultadoINC = "¡El día solicitado ha pasado!";
			resultadoCOR = "";
			return ;
		}
		boolean encontrado=false;
		encontrado= diaPersonalExistente(calendarioPersonal,festivoSolicitado);
		if (encontrado==true) {
			resultadoINC = "El día ya estaba confirmado";
			resultadoCOR = "";
			return ;
		}
		if (festivoSolicitado=="" || motivo=="") {
			resultadoINC = "Completa datos de solicitud";
			resultadoCOR = "";
			return ;
		}
		if ((diasPersonales==null)||(diasPersonales.size()<maxDiasFestivos)) {
			boolean creado =asignarDia(sec,operario,calendarioPersonal,Date.valueOf(festivoSolicitado),motivo);
			if (creado==false) {
				resultadoINC = "No aceptado - Contacta con RRHH ";
				resultadoCOR = "";
				return ;
			}
			else {
				resultadoINC = "";
				resultadoCOR = "Dia personal confirmado";
				return;
			}
		}
		}catch(Exception e) {
			resultadoINC = "No se ha realizado la petición ";
			resultadoCOR = "";
			return;
		}

		return;
	}
	
	private boolean diaPersonalExistente(CalendarioPersonalJPA cal,String fecha) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote)ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		boolean existe = sistCalendariosRemote.existeDiaPersonal(cal, Date.valueOf(fecha));
		return existe;
	}
	public String getIdCal() {		
		return idCal;
	}
	public void setIdCal(String idCal) {
		this.idCal = idCal;
	}
	public String getFechaMaxPropuestaVacacional() {
		return fechaMaxPropuestaVacacional;
	}
	public void setFechaMaxPropuestaVacacional(String fechaMaxPropuestaVacacional) {
		this.fechaMaxPropuestaVacacional = fechaMaxPropuestaVacacional;
	}
	public int getAnoCal() {
		return anoCal;
	}
	public void setAnoCal(int anoCal) {
		this.anoCal = anoCal;
	}
	public String getIdOp() {
		return idOp;
	}
	public void setIdOp(String idOp) {
		this.idOp = idOp;
	}
	public OperarioJPA getOperario() {
		return operario;
	}
	public void setOperario(OperarioJPA operario) {
		this.operario = operario;
	}
	public CalendarioPersonalJPA getCalendarioPersonal() {
		return calendarioPersonal;
	}
	public void setCalendarioPersonal(CalendarioPersonalJPA calendarioP) {
		this.calendarioPersonal = calendarioP;
	}
	public CalendarioSeccionJPA getCalendarioSeccion() {
		return calendarioSeccion;
	}
	public void setCalendarioSeccion(CalendarioSeccionJPA calendarioS) {
		this.calendarioSeccion = calendarioS;
	}
	public Collection<DiaPersonalJPA> getDiasPersonales() {
		return diasPersonales;
	}
	public void setDiasPersonales(Collection<DiaPersonalJPA> diasPers) {
		if(diasPers==null) {
			this.diasPersonales=null;
			return;
		}
		Iterator<DiaPersonalJPA> iter = diasPers.iterator();
		int i=1;
		try {
			if(iter.hasNext()) {
				diaPersonal1=iter.next();	
         		numDiaPersonal1=String.valueOf(i);
        		i++;}
			if(iter.hasNext()) {
				diaPersonal2=iter.next();	
         		numDiaPersonal2=String.valueOf(i);
        		i++;}			
			if(iter.hasNext()) {
				diaPersonal3=iter.next();	
         		numDiaPersonal3=String.valueOf(i);
        		i++;}
			if(iter.hasNext()) {
				diaPersonal4=iter.next();	
         		numDiaPersonal4=String.valueOf(i);
        		i++;}
			if(iter.hasNext()) {
				diaPersonal5=iter.next();	
         		numDiaPersonal5=String.valueOf(i);
        		i++;}
			if(iter.hasNext()) {
				diaPersonal6=iter.next();	
         		numDiaPersonal6=String.valueOf(i);
        		}		
		}catch (Exception e) {}
		diasPersonales=diasPers;
	}
	
	public Collection<PeriodoVacacionalJPA> getPeriodoVacacional() {
		return periodoVacacional;
	}
	public void setPeriodoVacacional(Collection<PeriodoVacacionalJPA> periodoVac) {
		this.periodoVacacional=periodoVac;
	}

	public boolean isPedidoVer() {
		return pedidoVer;
	}
	public void setPedidoVer(boolean pedidoV) {
		this.pedidoVer = pedidoV;
	}
	public boolean isPedidoInv() {
		return pedidoInv;
	}
	
	public void setPedidoInv(boolean pedidoI) {
		this.pedidoInv = pedidoI;
	}
	
	public SeccionAlmacenJPA getSec() {
		return this.sec;
	}

	public void setSec(SeccionAlmacenJPA secc) {
		this.sec = secc;
	}
	
	public String getIniTurnoV() {
		return iniTurnoV;
	}

	public void setIniTurnoV(String iniTurnoV) {
		this.iniTurnoV = iniTurnoV;
	}

	public String getFinTurnoV() {
		return finTurnoV;
	}

	public void setFinTurnoV(String finTurnoV) {
		this.finTurnoV = finTurnoV;
	}

	public String getIniTurnoI() {
		return iniTurnoI;
	}

	public void setIniTurnoI(String iniTurnoI) {
		this.iniTurnoI = iniTurnoI;
	}

	public String getFinTurnoI() {
		return finTurnoI;
	}

	public void setFinTurnoI(String finTurnoI) {
		this.finTurnoI = finTurnoI;
	}

	public DiaPersonalJPA getDiaPersonal1() {
		return diaPersonal1;
	}

	public void setDiaPersonal1(DiaPersonalJPA diasPersonal1) {
		this.diaPersonal1 = diasPersonal1;
	}

	public DiaPersonalJPA getDiaPersonal2() {
		return diaPersonal2;
	}

	public void setDiaPersonal2(DiaPersonalJPA diasPersonal2) {
		this.diaPersonal2 = diasPersonal2;
	}

	public DiaPersonalJPA getDiaPersonal3() {
		return diaPersonal3;
	}

	public void setDiaPersonal3(DiaPersonalJPA diasPersonal3) {
		this.diaPersonal3 = diasPersonal3;
	}

	public DiaPersonalJPA getDiaPersonal4() {
		return diaPersonal4;
	}

	public void setDiaPersonal4(DiaPersonalJPA diasPersonal4) {
		this.diaPersonal4 = diasPersonal4;
	}

	public DiaPersonalJPA getDiaPersonal5() {
		return diaPersonal5;
	}

	public void setDiaPersonal5(DiaPersonalJPA diasPersonal5) {
		this.diaPersonal5 = diasPersonal5;
	}

	public DiaPersonalJPA getDiaPersonal6() {
		return diaPersonal6;
	}

	public void setDiaPersonal6(DiaPersonalJPA diasPersonal6) {
		this.diaPersonal6 = diasPersonal6;
	}
	
	public String getNumDiaPersonal1() {
		return numDiaPersonal1;
	}
	public void setNumDiaPersonal1(String diaPersonal) {
		this.numDiaPersonal1 = diaPersonal;
	}
	
	public String getNumDiaPersonal2() {
		return numDiaPersonal2;
	}
	public void setNumDiaPersonal2(String diaPersonal) {
		this.numDiaPersonal2 = diaPersonal;
	}
	
	public String getNumDiaPersonal3() {
		return numDiaPersonal3;
		}
	public void setNumDiaPersonal3(String diaPersonal) {
		this.numDiaPersonal3 = diaPersonal;
	}
	
	public void setNumDiaPersonal4(String diaPersonal) {
		this.numDiaPersonal4 = diaPersonal;
	}
	public String getNumDiaPersonal4() {
		return numDiaPersonal4;
	}
	
	public String getNumDiaPersonal5() {
		return numDiaPersonal5;
	}
	public void setNumDiaPersonal5(String diaPersonal) {
		this.numDiaPersonal5 = diaPersonal;
	}
	public String getNumDiaPersonal6() {
		return numDiaPersonal6;
	}

	public void setNumDiaPersonal6(String diaPersonal) {
		this.numDiaPersonal6 = diaPersonal;
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
	
	public String getResultadoAprobado() {
		try {
		Date a= Date.valueOf(LocalDate.now());
		Date b=Date.valueOf(fechaMaxPropuestaVacacional);	
		if (a.before(b)==true){
			resultadoEnTramite = "En tramite";
			resultadoAprobado = "";
		}
		else {
			resultadoEnTramite = "";
			resultadoAprobado = "Aprobadas";
		}
		}catch (Exception e){
		resultadoEnTramite = "En tramite";
		resultadoAprobado = "";
		}
		return resultadoAprobado;
	}
		

	public void setResultadoAprobado(String res) {
		this.resultadoAprobado = res;
	}
	
	public String getResultadoEnTramite() {
		try {
		Date a= Date.valueOf(LocalDate.now());
		Date b=Date.valueOf(fechaMaxPropuestaVacacional);
		if (a.before(b)==true){
			resultadoEnTramite = "En tramite";
			resultadoAprobado = "";			
		}
		else {
			resultadoEnTramite = "";
			resultadoAprobado = "Aprobadas";
		}
		}catch(Exception e){resultadoEnTramite = "En tramite";}
		return resultadoEnTramite;
	}

	public void setResultadoEnTramite(String res) {
		this.resultadoEnTramite = res;
	}
	

	public String getFestivoSolicitado() {
		return festivoSolicitado;
	}

	public void setFestivoSolicitado(String festivoSolicitado) {
		this.festivoSolicitado = festivoSolicitado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public int getMaxDiasFestivos() {
		return maxDiasFestivos;
	}

	public void setMaxDiasFestivos(int maxDiasFestivos) {
		this.maxDiasFestivos = maxDiasFestivos;
	}
	
	public String getTurnoV1() {return turnosV[1];}
	public void setTurnoV1(String turno1) {this.turnoV1=turno1;turnosV[1] = this.turnoV1;}

	public String getTurnoV2() {return turnosV[2];}
	public void setTurnoV2(String turno2) {this.turnoV2=turno2;turnosV[2]= this.turnoV2;}

	public String getTurnoV3() {return turnosV[3];}
    public void setTurnoV3(String turno3) {this.turnoV3=turno3;turnosV[3]= this.turnoV3;}

	public String getTurnoV4() {return turnosV[4];}
	public void setTurnoV4(String turno4) {this.turnoV4=turno4;turnosV[4]= this.turnoV4;}

	public String getTurnoV5() {return turnosV[5];}
	public void setTurnoV5(String turno5) {this.turnoV5=turno5;turnosV[5]= this.turnoV5;}

	public String getTurnoV6() {return turnosV[6];}
	public void setTurnoV6(String turno6) {this.turnoV6=turno6;turnosV[6]= this.turnoV6;}

	public String getTurnoV7() {return turnosV[7];}
    public void setTurnoV7(String turno7) {this.turnoV7=turno7;turnosV[7]= this.turnoV7;}

	public String getTurnoV8() {return turnosV[8];}
	public void setTurnoV8(String turno8) {this.turnoV8=turno8;turnosV[8]= this.turnoV8;}

	public String getTurnoV9() {return turnosV[9];}
	public void setTurnoV9(String turno9) {this.turnoV9=turno9;turnosV[9] = this.turnoV9;}

	public String getTurnoV10() {return turnosV[10];}
	public void setTurnoV10(String turno10) {this.turnoV10=turno10;turnosV[10 ]= this.turnoV10;}
	
	public String getTurnoI1() {return turnosI[1];}
	public void setTurnoI1(String turno1) {this.turnoI1=turno1;turnosI[1] = this.turnoI1;}

	public String getTurnoI2() {return turnosI[2];}
	public void setTurnoI2(String turno2) {this.turnoI2=turno2;turnosI[2]= this.turnoI2;}

	public String getTurnoI3() {return turnosI[3];}
    public void setTurnoI3(String turno3) {this.turnoI3=turno3;turnosI[3]= this.turnoI3;}

	public String getTurnoI4() {return turnosI[4];}
	public void setTurnoI4(String turno4) {this.turnoI4=turno4;turnosI[4]= this.turnoI4;}

	public String getTurnoI5() {return turnosI[5];}
	public void setTurnoI5(String turno5) {this.turnoI5=turno5;turnosI[5]= this.turnoI5;}

	public String getTurnoI6() {return turnosI[6];}
	public void setTurnoI6(String turno6) {this.turnoI6=turno6;turnosI[6]= this.turnoI6;}

	public String getTurnoI7() {return turnosI[7];}
    public void setTurnoI7(String turno7) {this.turnoI7=turno7;turnosI[7]= this.turnoI7;}

	public String getTurnoI8() {return turnosI[8];}
	public void setTurnoI8(String turno8) {this.turnoI8=turno8;turnosI[8]= this.turnoI8;}

	public String getTurnoI9() {return turnosI[9];}
	public void setTurnoI9(String turno9) {this.turnoI9=turno9;turnosI[9] = this.turnoI9;}

	public String getTurnoI10() {return turnosI[10];}
	public void setTurnoI10(String turno10) {this.turnoI10=turno10;turnosI[10 ]= this.turnoI10;}
	
	public String getTurnoVerFecha() {
		return turnoVerFecha;
	}
	public void setTurnoVerFecha(String turnoVerF) {
		int id=0;
		TipoVacaciones tipo= TipoVacaciones.Verano;
		Date primerDia= Date.valueOf(turnoVerF);
		Date ultimoDia= finalTurno(Date.valueOf(turnoVerF));
		periodoVacacionalVerano= new PeriodoVacacionalJPA(id,tipo,primerDia,ultimoDia);
		this.turnoVerFecha = turnoVerF;
	}
	
	private Date finalTurno(Date turnoVerF) {
		try {
			Iterator <TurnoVacacionalJPA> it = turnosVacacionales.iterator();
    		while (it.hasNext()) {
    			TurnoVacacionalJPA i=it.next();
    			Date x=i.getFechaInicio();
    			if (x.equals(turnoVerF)){
    				return i.getFechaFin();
    				}
    			}
    		}catch (Exception e) {return null;}
		return null;
	}
	public String getTurnoInvFecha() {
		return turnoInvFecha;
	}
	public void setTurnoInvFecha(String turnoInvF) {
		int id=0;
		TipoVacaciones tipo= TipoVacaciones.Invierno;
		Date primerDia= Date.valueOf(turnoInvF);
		Date ultimoDia= finalTurno(Date.valueOf(turnoInvF));
		periodoVacacionalInvierno= new PeriodoVacacionalJPA(id,tipo,primerDia,ultimoDia);
		this.turnoInvFecha = turnoInvF;
	}
	public PeriodoVacacionalJPA getPeriodoVacacionalInvierno() {
		return periodoVacacionalInvierno;
	}
	public void setPeriodoVacacionalInvierno(PeriodoVacacionalJPA periodoVacacionalInv) {
		iniTurnoI=periodoVacacionalInv.getPrimerDia().toString();
		finTurnoI=periodoVacacionalInv.getUltimoDia().toString();
		this.periodoVacacionalInvierno = periodoVacacionalInv;
	}
	public TurnoVacacionalJPA getTurnoVer() {
		return turnoVer;
	}
	public void setTurnoVer(TurnoVacacionalJPA turnoVer) {
		this.turnoVer = turnoVer;
	}
	public TurnoVacacionalJPA getTurnoInv() {
		return turnoInv;
	}
	public void setTurnoInv(TurnoVacacionalJPA turnoInv) {
		this.turnoInv = turnoInv;
	}
	public Collection<TurnoVacacionalJPA> getTurnosVacacionales() {
		return turnosVacacionales;
	}
	public void setTurnosVacacionales(Collection<TurnoVacacionalJPA> turnoVac) {
		int ind1=0;
		int ind2=0;
		List<TurnoVacacionalJPA> lista1 = new ArrayList<TurnoVacacionalJPA>();
		List<TurnoVacacionalJPA> lista2 = new ArrayList<TurnoVacacionalJPA>();
		try {
			this.turnosVacacionales = turnoVac;
    		Iterator <TurnoVacacionalJPA> it = turnoVac.iterator();
    		while (it.hasNext()) {
    			TurnoVacacionalJPA i=it.next();
    			String x=i.getTemporada().name();
    			if (x.equals("Verano")){
    				lista1.add(ind1,i);
    				ind1++;
    				}
    			else { 				
	    			lista2.add(ind2,i);
	    			ind2++;
	    			}
    		}
    		periodosVacaVer=lista1;
    		periodosVacaInv=lista2;
    	}catch (Exception e){return ;}
		return ;
	}
	public List<TurnoVacacionalJPA> getPeriodosVacaVer() throws NamingException {
		return periodosVacaVer;
	}
	public void setPeriodosVacaVer(List<TurnoVacacionalJPA> periodos) {
		this.periodosVacaVer = periodos;
	}
	public List<TurnoVacacionalJPA> getPeriodosVacaInv() throws NamingException {
		return periodosVacaInv;
	}
	public void setPeriodosVacaInv(List<TurnoVacacionalJPA> periodos) {
		this.periodosVacaInv = periodos;
	}
	public PeriodoVacacionalJPA getPeriodoVacacionalVerano() {
		return periodoVacacionalVerano;
	}
	public void setPeriodoVacacionalVerano(PeriodoVacacionalJPA periodoVacacionalVer) {
		iniTurnoV=periodoVacacionalVer.getPrimerDia().toString();
		finTurnoV=periodoVacacionalVer.getUltimoDia().toString();
		this.periodoVacacionalVerano = periodoVacacionalVer;
	}
		
	}