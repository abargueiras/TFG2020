/*
* @author Antonio Bargueiras Sanchez, 2.020
 */
package managedbean;

import java.io.Serializable;
import java.sql.Date;
import java.text.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.SistemaCalendariosFacadeRemote;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.CalendarioEmpresaJPA;
import jpa.DiaFestivoJPA;
import jpa.DiaPersonalJPA;
import jpa.TipoVacaciones;
import jpa.TurnoVacacionalJPA;


/**
 * Managed Bean Calendario empresa
 */

@ManagedBean(name = "calendarioEmpresa")
@ViewScoped
public class CalendarioEmpresaBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@EJB
	private SistemaUsuariosFacadeRemote sistUsuariosRemote;
	@EJB
	private SistemaCalendariosFacadeRemote sistCalendariosRemote;
	protected DiaPersonalJPA dataDiaPersonal;
	protected DiaFestivoJPA dataDiaFestivo;
	protected CalendarioEmpresaJPA dataCalendarioEmpresa;
	protected Collection<DiaFestivoJPA> listaDiasFestivos;
	private Date fecha=null;
	private String hoy="";
	private int ano=0;
	private String nuevoAno="";
	private String maxDiasPersonales="";
	private Date fechalimitePropuesta =null;
	private String limitePropuesta="";
	private int mesActual=1;
	private String nombreMes="";
	private int mesAnterior =1;
	private String [] dias= new String[42]; 
	private String fechaFestivo="";
	private String festividadFestivo="";
	protected String resultadoINC = "";
	protected String resultadoCOR = "";
    //Dias del mes
	private String dia1="";
	private String dia2="";
	private String dia3="";
	private String dia4="";
	private String dia5="";
	private String dia6="";
	private String dia7="";
	private String dia8="";
	private String dia9="";
	private String dia10="";
	private String dia11="";
	private String dia12="";
	private String dia13="";
	private String dia14="";
	private String dia15="";
	private String dia16="";
	private String dia17="";
	private String dia18="";
	private String dia19="";
	private String dia20="";
	private String dia21="";
	private String dia22="";
	private String dia23="";
	private String dia24="";
	private String dia25="";
	private String dia26="";
	private String dia27="";
	private String dia28="";
	private String dia29="";
	private String dia30="";
	private String dia31="";
	private String dia32="";
	private String dia33="";
	private String dia34="";
	private String dia35="";
	private String dia36="";
	private String dia37="";
	private String dia38="";
	private String dia39="";
	private String dia40="";
	private String dia41="";
	private String dia42="";
	private String dia1f="";
	private String dia2f="";
	private String dia3f="";
	private String dia4f="";
	private String dia5f="";
	private String dia6f="";
	private String dia7f="";
	private String dia8f="";
	private String dia9f="";
	private String dia10f="";
	private String dia11f="";
	private String dia12f="";
	private String dia13f="";
	private String dia14f="";
	private String dia15f="";
	private String dia16f="";
	private String dia17f="";
	private String dia18f="";
	private String dia19f="";
	private String dia20f="";
	private String dia21f="";
	private String dia22f="";
	private String dia23f="";
	private String dia24f="";
	private String dia25f="";
	private String dia26f="";
	private String dia27f="";
	private String dia28f="";
	private String dia29f="";
	private String dia30f="";
	private String dia31f="";
	private String dia32f="";
	private String dia33f="";
	private String dia34f="";
	private String dia35f="";
	private String dia36f="";
	private String dia37f="";
	private String dia38f="";
	private String dia39f="";
	private String dia40f="";
	private String dia41f="";
	private String dia42f="";
	private String[] turnos= new String[22]; 
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
	private String turno11="";
	private String turno12="";
	private String turno13="";
	private String turno14="";
	private String turno15="";
	private String turno16="";
	private String turno17="";
	private String turno18="";
	private String turno19="";
	private String turno20="";
	public CalendarioEmpresaBean() {
		crearCalendario();
		for (int i=1;i<20;i++) {
			turnos[i]="";
			}
	    }

	public void crearCalendarioAnual() throws Exception {
		if(nuevoAno=="" || maxDiasPersonales=="") {
			resultadoCOR = "";
			resultadoINC = "Faltan datos de creacion";
			return;
		}
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		CalendarioEmpresaJPA cal = sistCalendariosRemote.getCalendarioEmpresa(Integer.valueOf(nuevoAno));
		if (cal!=null) {
			resultadoCOR = "";
			resultadoINC = "El calendario ya existe.";	
			return;
		}
		CalendarioEmpresaJPA creado= sistCalendariosRemote.crearCalendarioEmpresa(Integer.valueOf(nuevoAno),Integer.valueOf(maxDiasPersonales));
		if(creado!=null) {
			setDataCalendarioEmpresa(creado);
			resultadoCOR = "Calendario creado";
			resultadoINC = "";
		}
		else {
			resultadoCOR = "";
			resultadoINC = "Calendario no creado";
		}
	}
	
	public void prevAno() {
		setAno(ano-1);
	    String mes1 = String.valueOf(mesActual);
	    if (mes1.length()<2) {mes1="0"+mes1;}
	    String fecha1 = ano +"-"+ mes1 +"-01";
	    for (int i = 0; i < 42; ++i) {
	        dias[i]="";
	        }
	    int x = getDiaSemana(fecha1);
	    int y=1;
	    for (int i = x; i < 42; ++i) {
	        dias[i]= String.valueOf(y);
	        String dia1 = "0"+dias[i];
		    if (dia1.length()>2) {dia1=dia1.substring(1, 2);}
		    if (esFechaValida(ano, Integer.parseInt(dias[i]),mesActual)==false) {dias[i]="";}
	        ++y;
	        }
	}
	public void proxAno() {
		setAno(ano+1);
	    String mes1 = String.valueOf(mesActual);
	    if (mes1.length()<2) {mes1="0"+mes1;}
	    String fecha1 = ano +"-"+ mes1 +"-01";
	    for (int i = 0; i < 42; ++i) {
	        dias[i]="";
	        }
	    int x = getDiaSemana(fecha1);
	    int y=1;
	    for (int i = x; i < 42; ++i) {
	        dias[i]= String.valueOf(y);
	        String dia1 = "0"+dias[i];
		    if (dia1.length()>2) {dia1=dia1.substring(1, 2);}
		    if (esFechaValida(ano, Integer.parseInt(dias[i]),mesActual)==false) {dias[i]="";}
	        ++y;
	        }
	}
	public void prevMes() {
		setMesActual(mesActual-1);
	    String mes1 = String.valueOf(mesActual);
	    if (mes1.length()<2) {mes1="0"+mes1;}
	    String fecha1 = ano +"-"+ mes1 +"-01";
	    for (int i = 0; i < 42; ++i) {
	        dias[i]="";
	        }
	    int x = getDiaSemana(fecha1);
	    int y=1;
	    for (int i = x; i < 42; ++i) {
	        dias[i]= String.valueOf(y);
	        String dia1 = "0"+dias[i];
		    if (dia1.length()>2) {dia1=dia1.substring(1, 2);}
		    if (esFechaValida(ano, Integer.parseInt(dias[i]),mesActual)==false) {dias[i]="";}
	        ++y;
	        }
	}
	public void proxMes() {
		setMesActual(mesActual+1);
	    String mes1 = String.valueOf(mesActual);
	    if (mes1.length()<2) {mes1="0"+mes1;}
	    String fecha1 = ano +"-"+ mes1 +"-01";
	    for (int i = 0; i < 42; ++i) {
	        dias[i]="";
	        }
	    int x = getDiaSemana(fecha1);
	    int y=1;
	    for (int i = x; i < 42; ++i) {
	        dias[i]= String.valueOf(y);
	        String dia1 = "0"+dias[i];
		    if (dia1.length()>2) {dia1=dia1.substring(1, 2);}
		    if (esFechaValida(ano, Integer.parseInt(dias[i]),mesActual)==false) {dias[i]="";}
	        ++y;
	        }
	}
	
	public String getDias(int i) {
		return dias[i-1];
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Collection <DiaFestivoJPA> getListaDiasFestivos() throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		listaDiasFestivos= sistCalendariosRemote.getFestivosEmpresa();
		return listaDiasFestivos;
	}

	public void setListaDiasFestivos(Collection <DiaFestivoJPA> lista) {
		this.listaDiasFestivos= lista;
	}
	
	public Date getFechalimitePropuesta() {
		return fechalimitePropuesta;
	}

	public void setFechalimitePropuesta(Date fecha) {
		this.fechalimitePropuesta = fecha;
		}
	public String getLimitePropuesta() {
		return limitePropuesta;
	}

	public void setLimitePropuesta(String limiteP) {
		try {
			this.fechalimitePropuesta=Date.valueOf(limiteP);
			this.limitePropuesta = limiteP;
		}catch (Exception e) {return;}
	}
	
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}
	public void setAno() {
		this.ano = 0;
	}
	public int getMesActual() {
		return mesActual;
	}

	public void setMesActual(int mes) {
		if(mes>12) {
			this.mesActual = 1;
			setNombreMes("ENERO");
			this.ano=ano+1;
			return;
		}
		if(mes<1) {
			this.mesActual = 12;
			setNombreMes("DICIEMBRE");
			this.ano=ano-1;
			return;
		}
		this.mesActual = mes;
		if(mes==1) {setNombreMes("ENERO");}
		else if(mes==2) {setNombreMes("FEBRERO");}
		else if(mes==3) {setNombreMes("MARZO");}
		else if(mes==4) {setNombreMes("ABRIL");}
		else if(mes==5) {setNombreMes("MAYO");}
		else if(mes==6) {setNombreMes("JUNIO");}
		else if(mes==7) {setNombreMes("JULIO");}
		else if(mes==8) {setNombreMes("AGOSTO");}
		else if(mes==9) {setNombreMes("SEPTIEMBRE");}
		else if(mes==10) {setNombreMes("OCTUBRE");}
		else if(mes==11) {setNombreMes("NOVIEMBRE");}
		else if(mes==12) {setNombreMes("DICIEMBRE");}
	}
	
	
	public String getNombreMes() {
		return nombreMes;
	}

	public void setNombreMes(String mes) {
		this.nombreMes = mes;
	} 
	public int getMesAnterior() {
		return mesAnterior;
	}

	public void setMesAnterior(int mesVisualizado) {
		this.mesAnterior = mesVisualizado;
	}

	public String getHoy() {
		return hoy;
	}

	public void setHoy(String hoy) {
		this.hoy = hoy;
	}        
    
	public void crearCalendario() {
		LocalDate localDate=null;
		String a="";
	    int b =0;
		if(ano==0) {
			localDate = LocalDate.now();
			this.fecha= java.sql.Date.valueOf(localDate);
			setAno(localDate.getYear());
			setMesActual(localDate.getMonthValue());
			a = fecha.toString();
			b = localDate.getDayOfMonth();
			}
		else {
			a = getAno()+"-"+"01-01";
			this.fecha= Date.valueOf(a);
			setAno(ano);
			setMesActual(1);
			nombreMes="ENERO";
			b = 1;
		}
		a= a.substring(0, 3);	    
	    if (a.equalsIgnoreCase("Mon")) {hoy="Lunes";}
	    else if (a.equalsIgnoreCase("Tue")) {hoy="Lunes";}
	    else if (a.equalsIgnoreCase("Wed")) {hoy="Miercoles";}
	    else if (a.equalsIgnoreCase("Thu")) {hoy="Jueves";}
	    else if (a.equalsIgnoreCase("Fri")) {hoy="Viernes";}
	    else if (a.equalsIgnoreCase("Sat")) {hoy="Sabado";}
	    else if (a.equalsIgnoreCase("Sun")) {hoy="Domingo";}
	    else {hoy=a;}
	    String mes1 = String.valueOf(mesActual);
	    if (mes1.length()<2) {mes1="0"+mes1;}
	    String fecha1 = ano +"-"+ mes1 +"-01";
	    for (int i = 0; i < 42; ++i) {
	        dias[i]="";
	        }
	    int x = getDiaSemana(fecha1);
	    int y=1;
	    for (int i = x; i < 42; ++i) {
	        dias[i]= String.valueOf(y);
	        String dia1 = "0"+dias[i];
		    if (dia1.length()>2) {dia1=dia1.substring(1, 2);}
		    if (esFechaValida(ano, Integer.parseInt(dias[i]),mesActual)==false) {dias[i]="";}
	        ++y;
	        }	
		
	}
	
	
	
	public int getDiaSemana(String fecha) {
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 java.util.Date fechaActual = null;
		 try {
		 fechaActual = df.parse(fecha);
		 } catch (ParseException e) {
		 System.err.println("No se ha podido parsear la fecha.");
		 e.printStackTrace();
		 }
		 GregorianCalendar fechaCalendario = new GregorianCalendar();
		 fechaCalendario.setTime(fechaActual);
		 int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK);
		 if (diaSemana>1) diaSemana=diaSemana-1;
		 else if (diaSemana==1) diaSemana=7;
		 return diaSemana;
		 }

	 private boolean esFechaValida(int anio, int dia, int mes){
	        boolean esFechaValida = true;
	        try{
	            LocalDate.of(anio, mes, dia);
	        }catch(DateTimeException e) {
	            esFechaValida = false;
	        }
	        return esFechaValida;
	    }
	
	 public CalendarioEmpresaJPA getDataCalendarioEmpresa() throws NamingException{
		 return dataCalendarioEmpresa;
			}
	public void setDataCalendarioEmpresa(CalendarioEmpresaJPA cal) throws Exception{
			dataCalendarioEmpresa = cal;
			} 
	 
	public DiaPersonalJPA getDataDiaPersonal(){
		return dataDiaPersonal;
		}
	public void setDataDiaPersonal() throws Exception{
		dataDiaPersonal = new DiaPersonalJPA( );
		}
	/***
	 * Tratamiento festivos
	 * @return
	 */
	public DiaFestivoJPA getDataDiaFestivo(){
		return dataDiaFestivo;
		}
	public void setDataDiaFestivo(Date a, String b) throws Exception{
		dataDiaFestivo = new DiaFestivoJPA(a,b );
		}
	public String getFechaFestivo() {
		return fechaFestivo;
	}

	public void setFechaFestivo(String fechaFestivo) {
		this.fechaFestivo = fechaFestivo;
	}

	public String getFestividadFestivo() {
		return festividadFestivo;
	}

	public void setFestividadFestivo(String festividadFestivo) {
		this.festividadFestivo = festividadFestivo;
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
	public void crearDiaFestivo() throws Exception {
		//AAAA-MM-DD
		String res="";
		try {
		String aa= fechaFestivo.substring(0,4);
		String dd= fechaFestivo.substring(8,10);
		String mm= fechaFestivo.substring(5,7);
		res=res+"  año:"+aa+"xx  dia:"+dd+ "xx  mes:"+mm+"xx ";
		boolean es= esFechaValida(Integer.parseInt(aa), Integer.parseInt(dd), Integer.parseInt(mm));
		if (es==false) {
			resultadoINC = "La fecha introducida no es correcta" + res;
			resultadoCOR = "";
			return;
			}
		LocalDate a= LocalDate.of(Integer.parseInt(fechaFestivo.substring(0,4)), Integer.parseInt(fechaFestivo.substring(5,7)), Integer.parseInt(fechaFestivo.substring(8,10)));
		res=res+" local date " +a.toString();
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		DiaFestivoJPA nuevo = sistCalendariosRemote.crearDiaFestivo(a,festividadFestivo );
		res=res + " existe: ";
        	if(nuevo!=null) {
        		resultadoCOR = " Festivo añadido ";
        		resultadoINC = "";
        		}
    		else {
    			resultadoINC = "Festivo no añadido "+ res;
    			resultadoCOR = "";
    			}
		}catch (Exception e) {
			resultadoINC = "Debe introducir formato especificado "+res;
			resultadoCOR = "";	
			return;
		}
	}
	public void eliminarCalendarioEmpresa() throws Exception
	{
		boolean res=false;
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		res= sistCalendariosRemote.eliminarCalendarioEmpresa(ano);
		if(res) {
    		resultadoCOR = " Calendario eliminado ";
    		resultadoINC = "";	        		
    		}
		else {
			resultadoINC = " ¡ERROR AL ELIMINAR! ";
			resultadoCOR = "";
			}
        }
	/***
	 * Tratar turnos
	 * @return
	 * @throws NamingException 
	 * @throws NumberFormatException 
	 */	
	public void grabarTurnos() throws NumberFormatException, NamingException{
		int numTurnos=0;
		resultadoCOR = "";
		resultadoINC = "";
		boolean crearTurnos=false;
		if(comprobarDatosTurnos()==0) {
			resultadoCOR = "";
			resultadoINC = "Falta introducir datos";
			return;
		}
		if (limitePropuesta=="") {
			resultadoCOR = "";
			resultadoINC = "Falta fecha limite de propuesta";
			return;
		}
		Date h= Date.valueOf(LocalDate.now());
		if (h.after(Date.valueOf(turnos[1]))) {
			resultadoCOR = "";
			resultadoINC = "Fecha limite de propuesta incorrecta";
			return;
		}
		if (crearTurnos!=existenTurnos(Integer.valueOf(turnos[1].substring(0, 4)))) {
			resultadoCOR = "";
			resultadoINC = "Ya existen turnos de vacaciones";
			return;
		}
		numTurnos =comprobarDatosTurnos();
		boolean res=true;
		if(numTurnos>0) {
			for (int i=1;i<numTurnos+1;i++) {
				String d = turnos[i];
				Date fin= calcularFinTurno(d);
				TipoVacaciones tip= asignarTipoVacaciones(d);
				Date iniFec=Date.valueOf(d);
				res= crearTurno(iniFec,fin,tip);
				if(res==false) {				
					setResultadoCOR("");
					setResultadoINC("Turnos no grabados");
					turnos[i]="";
					//return;
					}
				}
			Properties props = System.getProperties();
			Context ctx = new InitialContext(props);
			sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
			CalendarioEmpresaJPA cal = sistCalendariosRemote.setLimitePropuestaVac(Integer.valueOf(turnos[1].substring(0, 4)),Date.valueOf(limitePropuesta));
			resultadoINC = "";
			resultadoCOR = "Turnos grabados";
			}
		}
	
	private boolean existenTurnos(int i) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		Collection <TurnoVacacionalJPA> res= sistCalendariosRemote.getTurnoVacacional(i);
		if (res.size()==0) return false;
		return true;
	}

	private boolean crearTurno(Date iniFec, Date fin, TipoVacaciones tip) throws NamingException {
		Properties props = System.getProperties();
		Context ctx = new InitialContext(props);
		sistCalendariosRemote = (SistemaCalendariosFacadeRemote) ctx.lookup("java:app/PracticalCaseStudyJEE.jar/SistemaCalendariosFacade!ejb.SistemaCalendariosFacadeRemote");
		boolean res= sistCalendariosRemote.crearTurnoVacacional(iniFec,fin,tip);
		return res;
	}

	private TipoVacaciones asignarTipoVacaciones(String d) {
		if ((Integer.parseInt(d.substring(5, 7))>3)&(Integer.parseInt(d.substring(5, 7))<10)){
			return TipoVacaciones.Verano;
		}
		else return TipoVacaciones.Invierno;
	}

	private Date calcularFinTurno(String d) {
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(Date.valueOf(d)); // Configuramos la fecha que se recibe
	      calendar.add(Calendar.DAY_OF_YEAR, 15);  // numero de días a añadir, o restar en caso de días<0
	      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	      String formatted = format1.format(calendar.getTime());
	      return Date.valueOf(formatted);
	}


	public int comprobarDatosTurnos() {
		String es="";
		int a=0;
		for (int i=1;i<20;i++) {
			if((turnos[i].equals(null)==false)&(turnos[i].equals("")==false)) {
				try {
				LocalDate.of(
						Integer.parseInt(turnos[i].substring(0,4)),
						Integer.parseInt(turnos[i].substring(5,7)),
			    		Integer.parseInt(turnos[i].substring(8,10)));
				a++;
				}catch(Exception e) {
					es=es+ "errorEn "+turnos[i].substring(0,4)+
							turnos[i].substring(5,7)+
				    		turnos[i].substring(8,10);
					return a;
					}
				}
			}
		return a;
	}

	/***
	 * Dias del mes
	 * @return
	 */	
	private boolean esFestivo(String dia1) {
		String dat= ano+"-"+mesActual+"-"+dia1;
		Iterator <DiaFestivoJPA>iter = listaDiasFestivos.iterator();
		while (iter.hasNext()) {
			if (Date.valueOf(dat)==iter.next().getId()) {
				resultadoINC = dat;
				resultadoCOR = listaDiasFestivos.iterator().next().getId().toString();
				return true;
			}
		}
		resultadoINC = dat;
		resultadoCOR = listaDiasFestivos.iterator().next().getId().toString();
		return false;
	}
	
	
	public String getDia1() {return dias[1];}
	public void setDia1(String dia1) {this.dia1 = dia1;this.dia1f = "";}
	
	public String getDia2() {return dias[2];}
	public void setDia2(String dia2) {this.dia2 = dia2;this.dia2f = "";}

	public String getDia3() {return dias[3];}
    public void setDia3(String dia3) {this.dia3 = dia3;this.dia3f = "";}

	public String getDia4() {return dias[4];}
	public void setDia4(String dia4) {this.dia4 = dia4;this.dia4f = "";}

	public String getDia5() {return dias[5];}
	public void setDia5(String dia5) {this.dia5 = dia5;this.dia5f = "";}

	public String getDia6() {return dias[6];}
	public void setDia6(String dia6) {this.dia6 = dia6;this.dia6f = "";}

	public String getDia7() {return dias[7];}
    public void setDia7(String dia7) {this.dia7 = dia7;this.dia7f = "";}

	public String getDia8() {return dias[8];}
	public void setDia8(String dia8) {this.dia8 = dia8;this.dia8f = "";}

	public String getDia9() {return dias[9];}
	public void setDia9(String dia9) {this.dia9 = dia9;this.dia9f = "";}

	public String getDia10() {return dias[10];}
	public void setDia10(String dia10) {this.dia10 = dia10;this.dia10f = "";}
	
	public String getDia11() {return dias[11];}
	public void setDia11(String dia11) {this.dia11 = dia11;this.dia11f = "";}

	public String getDia12() {return dias[12];}
	public void setDia12(String dia12) {this.dia12 = dia12;this.dia12f = "";}

	public String getDia13() {return dias[13];}
	public void setDia13(String dia13) {this.dia13 = dia13;this.dia13f = "";}

	public String getDia14() {return dias[14];}
	public void setDia14(String dia14) {this.dia14 = dia14;this.dia14f = "";}

	public String getDia15() {return dias[15];}
	public void setDia15(String dia15) {this.dia15 = dia15;this.dia15f = "";}

	public String getDia16() {return dias[16];}
	public void setDia16(String dia16) {this.dia16 = dia16;this.dia16f = "";}

	public String getDia17() {return dias[17];}
	public void setDia17(String dia17) {this.dia17 = dia17;this.dia17f = "";}

	public String getDia18() {return dias[18];}
	public void setDia18(String dia18) {this.dia18 = dia18;this.dia18f = "";}

	public String getDia19() {return dias[19];}
	public void setDia19(String dia19) {this.dia19 = dia19;this.dia19f = "";}
	
	public String getDia20() {return dias[20];}
	public void setDia20(String dia20) {this.dia20 = dia20;this.dia20f = "";}
	
	public String getDia21() {return dias[21];}
	public void setDia21(String dia21) {this.dia21 = dia21;this.dia21f = "";}

	public String getDia22() {return dias[22];}
	public void setDia22(String dia22) {this.dia22 = dia22;this.dia22f = "";}

	public String getDia23() {return dias[23];}
	public void setDia23(String dia23) {this.dia23 = dia23;this.dia23f = "";}

	public String getDia24() {return dias[24];}
	public void setDia24(String dia24) {this.dia24 = dia24;this.dia24f = "";}

	public String getDia25() {return dias[25];}
	public void setDia25(String dia25) {this.dia25 = dia25;this.dia25f = "";}

	public String getDia26() {return dias[26];}
	public void setDia26(String dia26) {this.dia26 = dia26;this.dia26f = "";}

	public String getDia27() {return dias[27];}
	public void setDia27(String dia27) {this.dia27 = dia27;this.dia27f = "";}

	public String getDia28() {return dias[28];}
	public void setDia28(String dia28) {this.dia28 = dia28;this.dia28f = "";}

	public String getDia29() {return dias[29];}
	public void setDia29(String dia29) {this.dia29 = dia29;this.dia29f = "";}

	public String getDia30() {return dias[30];}
	public void setDia30(String dia30) {this.dia30 = dia30;this.dia30f = "";}

	public String getDia31() {return dias[31];}
	public void setDia31(String dia31) {this.dia31 = dia31;this.dia31f = "";}

	public String getDia32() {return dias[32];}
	public void setDia32(String dia32) {this.dia32 = dia32;this.dia32f = "";}

	public String getDia33() {return dias[33];}
	public void setDia33(String dia33) {this.dia33 = dia33;this.dia33f = "";}

	public String getDia34() {return dias[34];}
	public void setDia34(String dia34) {this.dia34 = dia34;this.dia34f = "";}

	public String getDia35() {return dias[35];}
	public void setDia35(String dia35) {this.dia35 = dia35;this.dia35f = "";}

	public String getDia36() {return dias[36];}
	public void setDia36(String dia36) {this.dia36 = dia36;this.dia36f = "";}

	public String getDia37() {return dias[37];}
	public void setDia37(String dia37) {this.dia37 = dia37;this.dia37f = "";}

	public String getDia38() {return dias[38];}
	public void setDia38(String dia38) {this.dia38 = dia38;this.dia38f = "";}

	public String getDia39() {return dias[39];}
	public void setDia39(String dia39) {this.dia39 = dia39;this.dia39f = "";}

	public String getDia40() {return dias[40];}
	public void setDia40(String dia40) {this.dia40 = dia40;this.dia40f = "";}
	
	public String getDia41() {return dias[41];}
	public void setDia41(String dia41) {this.dia41 = dia41;this.dia41f = "";}
    
	public String getDia1f() {return this.dia1f;}
	public void setDia1f(String dia1) {this.dia1f = dia1;this.dia1 = "";}

	public String getDia2f() {return this.dia2f;}
	public void setDia2f(String dia2) {this.dia2f = dia2;this.dia2 = "";}

	public String getDia3f() {return this.dia3f;}
        public void setDia3f(String dia3) {this.dia3f = dia3;this.dia3 = "";}

	public String getDia4f() {return this.dia4f;}
	public void setDia4f(String dia4) {this.dia4f = dia4;this.dia4 = "";}

	public String getDia5f() {return this.dia5f;}
	public void setDia5f(String dia5) {this.dia5f = dia5;this.dia5 = "";}

	public String getDia6f() {return this.dia6f;}
	public void setDia6f(String dia6) {this.dia6f = dia6;this.dia6 = "";}

	public String getDia7f() {return this.dia7f;}
        public void setDia7f(String dia7) {this.dia7f = dia7;this.dia7 = "";}

	public String getDia8f() {return this.dia8f;}
	public void setDia8f(String dia8) {this.dia8f = dia8;this.dia8 = "";}

	public String getDia9f() {return this.dia9f;}
	public void setDia9f(String dia9) {this.dia9f = dia9;this.dia9 = "";}

	public String getDia10f() {return this.dia10f;}
	public void setDia10f(String dia10) {this.dia10f = dia10;this.dia10 = "";}
	
	public String getDia11f() {return this.dia11f;}
	public void setDia11f(String dia11) {this.dia11f = dia11;this.dia11 = "";}

	public String getDia12f() {return this.dia12f;}
	public void setDia12f(String dia12) {this.dia12f = dia12;this.dia12 = "";}

	public String getDia13f() {return this.dia13f;}
	public void setDia13f(String dia13) {this.dia13f = dia13;this.dia13 = "";}

	public String getDia14f() {return this.dia14f;}
	public void setDia14f(String dia14) {this.dia14f = dia14;this.dia14 = "";}

	public String getDia15f() {return this.dia15f;}
	public void setDia15f(String dia15) {this.dia15f = dia15;this.dia15 = "";}

	public String getDia16f() {return this.dia16f;}
	public void setDia16f(String dia16) {this.dia16f = dia16;this.dia16 = "";}

	public String getDia17f() {return this.dia17f;}
	public void setDia17f(String dia17) {this.dia17f = dia17;this.dia17 = "";}

	public String getDia18f() {return this.dia18f;}
	public void setDia18f(String dia18) {this.dia18f = dia18;this.dia18 = "";}

	public String getDia19f() {return this.dia19f;}
	public void setDia19f(String dia19) {this.dia19f = dia19;this.dia19 = "";}
	
	public String getDia20f() {return this.dia20f;}
	public void setDia20f(String dia20) {this.dia20f = dia20;this.dia20 = "";}
	
	public String getDia21f() {return this.dia21f;}
	public void setDia21f(String dia21) {this.dia21f = dia21;this.dia21 = "";}

	public String getDia22f() {return this.dia22f;}
	public void setDia22f(String dia22) {this.dia22f = dia22;this.dia22 = "";}

	public String getDia23f() {return this.dia23f;}
	public void setDia23f(String dia23) {this.dia23f = dia23;this.dia23 = "";}

	public String getDia24f() {return this.dia24f;}
	public void setDia24f(String dia24) {this.dia24f = dia24;this.dia24 = "";}

	public String getDia25f() {return this.dia25f;}
	public void setDia25f(String dia25) {this.dia25f = dia25;this.dia25 = "";}

	public String getDia26f() {return this.dia26f;}
	public void setDia26f(String dia26) {this.dia26f = dia26;this.dia26 = "";}

	public String getDia27f() {return this.dia27f;}
	public void setDia27f(String dia27) {this.dia27f = dia27;this.dia27 = "";}

	public String getDia28f() {return this.dia28f;}
	public void setDia28f(String dia28) {this.dia28f = dia28;this.dia28 = "";}

	public String getDia29f() {return this.dia29f;}
	public void setDia29f(String dia29) {this.dia29f = dia29;this.dia29 = "";}

	public String getDia30f() {return this.dia30f;}
	public void setDia30f(String dia30) {this.dia30f = dia30;this.dia30 = "";}

	public String getDia31f() {return this.dia31f;}
	public void setDia31f(String dia31) {this.dia31f = dia31;this.dia31 = "";}

	public String getDia32f() {return this.dia32f;}
	public void setDia32f(String dia32) {this.dia32f = dia32;this.dia32 = "";}

	public String getDia33f() {return this.dia33f;}
	public void setDia33f(String dia33) {this.dia33f = dia33;this.dia33 = "";}

	public String getDia34f() {return this.dia34f;}
	public void setDia34f(String dia34) {this.dia34f = dia34;this.dia34 = "";}

	public String getDia35f() {return this.dia35f;}
	public void setDia35f(String dia35) {this.dia35f = dia35;this.dia35 = "";}

	public String getDia36f() {return this.dia36f;}
	public void setDia36f(String dia36) {this.dia36f = dia36;this.dia36 = "";}

	public String getDia37f() {return this.dia37f;}
	public void setDia37f(String dia37) {this.dia37f = dia37;this.dia37 = "";}

	public String getDia38f() {return this.dia38f;}
	public void setDia38f(String dia38) {this.dia38f = dia38;this.dia38 = "";}

	public String getDia39f() {return this.dia39f;}
	public void setDia39f(String dia39) {this.dia39f = dia39;this.dia39 = "";}

	public String getDia40f() {return this.dia40f;}
	public void setDia40f(String dia40) {this.dia40f = dia40;this.dia40 = "";}
	
	public String getDia41f() {return this.dia41f;}
	public void setDia41f(String dia41) {this.dia41f = dia41;this.dia41 = "";}

	public String getTurno1() {return turnos[1];}
	public void setTurno1(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno1=turno;	turnos[1] = this.turno1;return;};
			}catch(Exception e) {this.turno1="";return;}this.turno1="";
		}

	public String getTurno2() {return turnos[2];}
	public void setTurno2(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno2=turno;	turnos[2] = this.turno2;return;};
			}catch(Exception e) {this.turno2="";return;}this.turno2="";
		}

	public String getTurno3() {return turnos[3];}
    public void setTurno3(String turno) {
    	try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno3=turno;	turnos[3] = this.turno3;return;};
			}catch(Exception e) {this.turno3="";return;}this.turno3="";
		}

	public String getTurno4() {return turnos[4];}
	public void setTurno4(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno4=turno;	turnos[4] = this.turno4;return;};
			}catch(Exception e) {this.turno4="";return;}this.turno4="";
		}

	public String getTurno5() {return turnos[5];}
	public void setTurno5(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno5=turno;	turnos[5] = this.turno5;return;};
			}catch(Exception e) {this.turno5="";return;}this.turno5="";
		}

	public String getTurno6() {return turnos[6];}
	public void setTurno6(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno6=turno;	turnos[6] = this.turno6;return;};
			}catch(Exception e) {this.turno6="";return;}this.turno6="";
		}

	public String getTurno7() {return turnos[7];}
    public void setTurno7(String turno) {
    	try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno7=turno;	turnos[7] = this.turno7;return;};
			}catch(Exception e) {this.turno7="";return;}this.turno7="";
		}

	public String getTurno8() {return turnos[8];}
	public void setTurno8(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno8=turno;	turnos[8] = this.turno8;return;};
			}catch(Exception e) {this.turno8="";return;}this.turno8="";
		}

	public String getTurno9() {return turnos[9];}
	public void setTurno9(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno9=turno;	turnos[9] = this.turno9;return;};
			}catch(Exception e) {this.turno9="";return;}this.turno9="";
		}

	public String getTurno10() {return turnos[10];}
	public void setTurno10(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno10=turno;	turnos[10] = this.turno10;return;};
			}catch(Exception e) {this.turno10="";return;}this.turno10="";
		}
	
	public String getTurno11() {return turnos[11];}
	public void setTurno11(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno11=turno;	turnos[11] = this.turno11;return;};
			}catch(Exception e) {this.turno11="";return;}this.turno11="";
		}

	public String getTurno12() {return turnos[12];}
	public void setTurno12(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno12=turno;	turnos[12] = this.turno12;return;};
			}catch(Exception e) {this.turno12="";return;}this.turno12="";
		}

	public String getTurno13() {return turnos[13];}
	public void setTurno13(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno13=turno;	turnos[13] = this.turno13;return;};
			}catch(Exception e) {this.turno13="";return;}this.turno13="";
		}

	public String getTurno14() {return turnos[14];}
	public void setTurno14(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno14=turno;	turnos[14] = this.turno14;return;};
			}catch(Exception e) {this.turno14="";return;}this.turno14="";
		}

	public String getTurno15() {return turnos[15];}
	public void setTurno15(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno15=turno;	turnos[15] = this.turno15;return;};
			}catch(Exception e) {this.turno15="";return;}this.turno15="";
		}

	public String getTurno16() {return turnos[16];}
	public void setTurno16(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno16=turno;	turnos[16] = this.turno16;return;};
			}catch(Exception e) {this.turno16="";return;}this.turno16="";
		}

	public String getTurno17() {return turnos[17];}
	public void setTurno17(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno17=turno;	turnos[17] = this.turno17;return;};
			}catch(Exception e) {this.turno17="";return;}this.turno17="";
		}

	public String getTurno18() {return turnos[18];}
	public void setTurno18(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno18=turno;	turnos[18] = this.turno18;return;};
			}catch(Exception e) {this.turno18="";return;}this.turno18="";
		}

	public String getTurno19() {return turnos[19];}
	public void setTurno19(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno19=turno;	turnos[19] = this.turno19;return;};
			}catch(Exception e) {this.turno19="";return;}this.turno19="";
		}
	
	public String getTurno20() {return turnos[20];}
	public void setTurno20(String turno) {
		try {
			if(Date.valueOf(turno).after(fechalimitePropuesta)){
				this.turno20=turno;	turnos[20] = this.turno20;return;};
			}catch(Exception e) {this.turno20="";return;}this.turno20="";
		}

	public String getMaxDiasPersonales() {
		return maxDiasPersonales;
	}

	public void setMaxDiasPersonales(String maxDias) {
		try {
			Integer.valueOf(maxDias);
		}catch(Exception e) {
			this.maxDiasPersonales="";
		}
		this.maxDiasPersonales = maxDias;
	}

	public String getNuevoAno() {
		return nuevoAno;
	}

	public void setNuevoAno(String nuevoAno) {
		try {
			Integer.valueOf(nuevoAno);
		}catch(Exception e) {this.nuevoAno ="" ; return;}
		this.nuevoAno = nuevoAno;
	}


	}