/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.sql.Date;
import java.text.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioSeccionJPA;
import jpa.DiaFestivoJPA;
import jpa.DiaPersonalJPA;
import jpa.SeccionAlmacenJPA;
import jpa.TipoVacaciones;
import jpa.Turno;
import jpa.TurnoVacacionalJPA;


/**
 * Managed Bean Calendario empresa
 */

@ManagedBean(name = "calendarioSeccion")
@ViewScoped
public class CalendarioSeccionBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistCalendariosRemote;
	protected CalendarioEmpresaJPA dataCalendarioEmpresa;
	protected Set<TurnoVacacionalJPA> listaTurnosVacacionales;
	private String seccionNombre="";
	private SeccionAlmacenJPA seccion;
	private Turno turnoS;
	private String turno;
	private List<SeccionAlmacenJPA> secciones;
	private String numOperarios="";
	private Date fecha=null;
	private String hoy="";
	private int ano=0;
	protected String resultadoINC = "";
	protected String resultadoCOR = "";
	public boolean seleccionadaSeccion=false;
    //Dias del mes
	private String[] turnos= new String[42]; 
	private String turno1="";
	private String turno2="";
	private String turno3="";
	private String turno4="";
	private String turno5="";
	private String turno6="";
	private String turno7="";
	private String turno8="";
	private String turno9="";
	private String turno10="";
	private String turno1F="";
	private String turno2F="";
	private String turno3F="";
	private String turno4F="";
	private String turno5F="";
	private String turno6F="";
	private String turno7F="";
	private String turno8F="";
	private String turno9F="";
	private String turno10F="";
	public CalendarioSeccionBean() throws NamingException {
		getAno();
		cargarTurnos();
		cargarSecciones();
	    }
	public String getSeccionNombre() {
		return seccionNombre;
	}

	public void setSeccionNombre(String seccion) throws NamingException {
		if((seccion=="")||(seccion==null)) {
			this.seccionNombre = "";
			return;
		}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		int i =sistUsuariosRemote.getSeccionNombr(seccion);
		this.seccion=sistUsuariosRemote.getSeccion(i);
		this.seccionNombre = seccion;
	}
	
	public void turnoSeccion() {
		turnoS=this.seccion.getTurno();
		turno=turnoS.name();
		seleccionadaSeccion=true;
	}
	public Turno getTurnoS() {
		return turnoS;
	}

	public void setTurnoS(Turno turno) {
		this.turnoS = turno;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	
	public SeccionAlmacenJPA getSeccion() {
		return seccion;
	}
	public void setSeccion(SeccionAlmacenJPA seccion) {
		this.seccion = seccion;
	}
	public void cargarSecciones() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistUsuariosRemote = (SistemaUsuariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaUsuariosFacade!ejb.SistemaUsuariosFacadeRemote");
		Collection<SeccionAlmacenJPA> es =sistUsuariosRemote.getSecciones();
		secciones= new ArrayList<SeccionAlmacenJPA>(es);
	}
	
	public void salvarCalendarioSeccion() throws NamingException {
		resultadoINC = "";
		resultadoCOR = "";
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		CalendarioSeccionJPA existeCalSeccion=sistCalendariosRemote.getCalendarioSeccionAno(ano,seccion);
		if((seleccionadaSeccion==false)||(seccionNombre==null)||(seccionNombre=="")) {
			resultadoINC = "No ha confirmado la sección";
			resultadoCOR = "";
			seleccionadaSeccion=false;
			return;
		}
		if(existeCalSeccion!=null) {
			resultadoINC = "Ya existe el calendario de la sección";
			resultadoCOR = "";
			seleccionadaSeccion=false;
			return;
		}
		if((secciones==null)||(secciones.isEmpty())) {
			resultadoINC = "No existen turnos vacacionales";
			resultadoCOR = "";
			seleccionadaSeccion=false;
			return;
		}
		if(ano==0 ||seccion==null||turnoS==null||numOperarios=="") {
			resultadoINC = "Faltan datos"+ " ano:"+ ano + " sec:"+ seccion.getNombre()+ " turno:"+ turnoS+ " numOperarios:"+ numOperarios;
			resultadoCOR = "";
			seleccionadaSeccion=false;
			return;
		}
		boolean es =sistCalendariosRemote.setCalendarioSeccion(ano,seccion,turnoS,Integer.valueOf(numOperarios));
		if (es==true) {
			resultadoINC = "";
			resultadoCOR = "Guardado";
			seleccionadaSeccion=false;
		}
		else {
			resultadoINC = "Error en guardado ¿Esta creado el calendario de empresa? ";
			resultadoCOR = "";
			seleccionadaSeccion=false;
		}
	}
	
	public List<SeccionAlmacenJPA> getSecciones() throws NamingException {
		return secciones;
	}


	public void setSecciones(List<SeccionAlmacenJPA> secciones) {
		this.secciones = secciones;
	}	
	public String getNumOperarios() {
		return numOperarios;
	}
	public void setNumOperarios(String numOp) {
		try {
			Integer.valueOf(numOp);
		}catch (Exception e) {
			resultadoINC = "Inserta un número en el campo núm. operarios";
			resultadoCOR = "";
			return;}
		this.numOperarios = numOp;
	}
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}	

	public int getAno() {
		if(ano==0) {this.ano = LocalDate.now().getYear();}
		return this.ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}
	public void setAno() {
		this.ano = 0;
	}

	public String getHoy() {
		return hoy;
	}

	public void setHoy(String hoy) {
		this.hoy = hoy;
	}        
    
	public void cargarTurnos() throws NamingException {
		for (int i = 0; i < 21; ++i) {
	        turnos[i]="";
	        }
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		Collection<TurnoVacacionalJPA> es =sistCalendariosRemote.getTurnoVacacional(ano);
		int i =1;
		Iterator <TurnoVacacionalJPA> it = es.iterator();
		while(it.hasNext()) {
			  String x=it.next().getFechaInicio().toString();
			  Date xx=calcularFinTurno(x);
			  turnos[i]=x;
			  turnos[10+i]=xx.toString();
		      i++;
		      }
	}
	private Date calcularFinTurno(String d) {
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(Date.valueOf(d)); // Configuramos la fecha que se recibe
	      calendar.add(Calendar.DAY_OF_YEAR, 15);  // numero de días a añadir, o restar en caso de días<0
	      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	      String formatted = format1.format(calendar.getTime());
	      return Date.valueOf(formatted);
	}
	public void eliminarCalendarioSeccion() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		res= sistCalendariosRemote.eliminarCalendarioSeccion(ano, seccion);
		if(res) {
    		resultadoCOR = " Calendario eliminado ";
    		resultadoINC = "";	        		
    		}
		else {
			resultadoINC = " ¡ERROR AL ELIMINAR datos asociados (turnos vacaciones, días personales,etc! ";
			resultadoCOR = "";
			}
        }
	
	public CalendarioEmpresaJPA getDataCalendarioEmpresa() throws NamingException{
		 return dataCalendarioEmpresa;
			}
	//mirar el 2
	public void setDataCalendarioEmpresa() throws Exception{
			dataCalendarioEmpresa = new CalendarioEmpresaJPA( ano);
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
	
	public String getTurno1() {return turnos[1];}
	public void setTurno1(String turno1) {this.turno1=turno1;turnos[1] = this.turno1;}

	public String getTurno2() {return turnos[2];}
	public void setTurno2(String turno2) {this.turno2=turno2;turnos[2]= this.turno2;}

	public String getTurno3() {return turnos[3];}
    public void setTurno3(String turno3) {this.turno3=turno3;turnos[3]= this.turno3;}

	public String getTurno4() {return turnos[4];}
	public void setTurno4(String turno4) {this.turno4=turno4;turnos[4]= this.turno4;}

	public String getTurno5() {return turnos[5];}
	public void setTurno5(String turno5) {this.turno5=turno5;turnos[5]= this.turno5;}

	public String getTurno6() {return turnos[6];}
	public void setTurno6(String turno6) {this.turno6=turno6;turnos[6]= this.turno6;}

	public String getTurno7() {return turnos[7];}
    public void setTurno7(String turno7) {this.turno7=turno7;turnos[7]= this.turno7;}

	public String getTurno8() {return turnos[8];}
	public void setTurno8(String turno8) {this.turno8=turno8;turnos[8]= this.turno8;}

	public String getTurno9() {return turnos[9];}
	public void setTurno9(String turno9) {this.turno9=turno9;turnos[9] = this.turno9;}

	public String getTurno10() {return turnos[10];}
	public void setTurno10(String turno10) {this.turno10=turno10;turnos[10 ]= this.turno10;}
	
	public String getTurno1F() {return turnos[11];}
	public void setTurno1F(String turno1) {this.turno1F=turno1;turnos[11]= this.turno1F;}

	public String getTurno2F() {return turnos[12];}
	public void setTurno2F(String turno2) {this.turno2F=turno2;turnos[12]= this.turno2F;}

	public String getTurno3F() {return turnos[13];}
    public void setTurno3F(String turno3) {this.turno3F=turno3;turnos[13]= this.turno3F;}

	public String getTurno4F() {return turnos[14];}
	public void setTurno4F(String turno4) {this.turno4F=turno4;turnos[14]= this.turno4F;}

	public String getTurno5F() {return turnos[15];}
	public void setTurno5F(String turno5) {this.turno5F=turno5;turnos[15]= this.turno5F;}

	public String getTurno6F() {return turnos[16];}
	public void setTurno6F(String turno6) {this.turno6F=turno6;turnos[16]= this.turno6F;}

	public String getTurno7F() {return turnos[17];}
    public void setTurno7F(String turno7) {this.turno7F=turno7;turnos[17]= this.turno7F;}

	public String getTurno8F() {return turnos[18];}
	public void setTurno8F(String turno8) {this.turno8F=turno8;turnos[18]= this.turno8F;}

	public String getTurno9F() {return turnos[19];}
	public void setTurno9F(String turno9) {this.turno9F=turno9;turnos[19] = this.turno9F;}

	public String getTurno10F() {return turnos[20];}
	public void setTurno10F(String turno10) {this.turno10F=turno10;turnos[20 ]= this.turno10F;}

	}