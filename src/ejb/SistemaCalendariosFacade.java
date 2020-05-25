/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.faces.context.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import jpa.RrhhJPA;
import jpa.PersonalJPA;
import ejb.SistemaUsuariosFacadeRemote;
import jpa.JefeAlmacenJPA;
import jpa.JefeEquipoJPA;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioPersonalJPA;
import jpa.CalendarioSeccionJPA;
import jpa.DiaFestivoJPA;
import jpa.DiaPersonalJPA;
import jpa.EncargadoJPA;
import jpa.OperarioJPA;
import jpa.PeriodoVacacionalJPA;
import jpa.PersonalAlmacenJPA;
import jpa.PuestoJPA;
import jpa.SeccionAlmacenJPA;
import jpa.TipoVacaciones;
import jpa.Turno;
import jpa.TurnoVacacionalJPA;
import ejb.SistemaCalendariosFacadeRemote;

/**
 * EJB Sesion clase Bean
 */

@Stateless
public class SistemaCalendariosFacade implements SistemaCalendariosFacadeRemote {
	
	//Persistence Unit Context
	@PersistenceContext(unitName="PracticalCase") 
	private EntityManager entman;
	
	
	@Override
	public DiaFestivoJPA crearDiaFestivo(LocalDate fecha, String fest) {
		DiaFestivoJPA nuevo=null;
		try {
			nuevo = new DiaFestivoJPA(Date.valueOf(fecha), fest);
			entman.persist(nuevo);
			CalendarioEmpresaJPA p =entman.find(CalendarioEmpresaJPA.class, fecha.getYear());
			if(p==null) {
				return null;
			}
			nuevo.setCalendarioEmpresa(p);
			}catch (Exception e) {
				return null;
			}
		return nuevo;
		}
   /**
    * Metodos calendario Empresa
    */
	@Override
	public boolean eliminarCalendarioEmpresa(int id) {
		Query query = entman.createQuery("DELETE FROM DiaFestivoJPA WHERE calendarioempresa_id = " + id);
		query.executeUpdate();
		CalendarioEmpresaJPA cal = entman.find(CalendarioEmpresaJPA.class, id);
		if ((cal!=null)){
			try {
			entman.remove(cal);
			return true;
			}
			catch(Exception e) {
				return false;
				}
			}
		return false;
		}

@SuppressWarnings("unchecked")
@Override
public Collection<CalendarioEmpresaJPA> getCalendariosEmpresa() {
	Collection<CalendarioEmpresaJPA> calendarios=null;
	try {
	calendarios = entman.createQuery("from CalendarioEmpresaJPA order by id").getResultList();
	}catch(Exception e) {
		return null;
	}
	return calendarios;
}

@Override
public CalendarioEmpresaJPA getCalendarioEmpresa(int id) {
	CalendarioEmpresaJPA calendarios =null;
	try {
	calendarios = (CalendarioEmpresaJPA) entman.createQuery("from CalendarioEmpresaJPA WHERE id = " + id).getSingleResult();
	}catch(Exception e) {
		return null;
	}    
	return calendarios;
}

@Override
public boolean eliminarDiaFestivoEmpresa(int id) {
	DiaFestivoJPA dia = entman.find(DiaFestivoJPA.class, id);
	if ((dia!=null)){
		try {
		entman.remove(dia);
		return true;
		}
		catch(Exception e) {
			return false;
		}
		}return false;	
	}

@Override
public Collection<DiaFestivoJPA> getFestivosEmpresa() {
	@SuppressWarnings("unchecked")
	Collection<DiaFestivoJPA> festivos = entman.createQuery("from DiaFestivoJPA order by id").getResultList();
    return festivos;
}

@Override
public Collection<DiaFestivoJPA> getFestivoEmpresa(java.util.Date id){
	@SuppressWarnings("unchecked")
	Collection<DiaFestivoJPA> festivos = entman.createQuery("from DiaFestivoJPA WHERE id = " + id).getResultList();
    return festivos;
}

@Override
public boolean crearTurnoVacacional(Date di, Date df, TipoVacaciones tip) {
	LocalDate a= di.toLocalDate();
	TurnoVacacionalJPA nuevo=null;
	try {
		nuevo = new TurnoVacacionalJPA(di);
		entman.persist(nuevo);
		CalendarioEmpresaJPA p =entman.find(CalendarioEmpresaJPA.class,a.getYear());
		if(p==null) {
			return false;
		}
		nuevo.setFechaFin(df);
		nuevo.setTemporada(tip);
		nuevo.setCalendarioEmpresa(p);
		entman.merge(nuevo);
		}catch (Exception e) {
			return false;
		}
	return true;
	}

@Override
public CalendarioEmpresaJPA getCalendarioEmpresaPorId(int id) {
	CalendarioEmpresaJPA cal = entman.find(CalendarioEmpresaJPA.class, id);
	return cal;
}

@Override
public Collection<TurnoVacacionalJPA> getTurnoVacacional(int ano) {
	@SuppressWarnings("unchecked")
	Collection<TurnoVacacionalJPA> turnos = entman.createQuery("from TurnoVacacionalJPA WHERE calendarioempresa_id = " + ano +" order by fechainicio").getResultList();
    return turnos;
}

@Override
public boolean eliminarTurnoVacacionalEmpresa(java.util.Date id) {
	TurnoVacacionalJPA turno = entman.find(TurnoVacacionalJPA.class, id);
	if ((turno!=null)){
		try {
		entman.remove(turno);
		return true;
		}
		catch(Exception e) {
			return false;
		}
		}return false;	
}

@Override
public Collection<TurnoVacacionalJPA> getTurnosVacacionalesEmpresa() {
	Collection<TurnoVacacionalJPA> turnos =null;
	try {
	turnos = entman.createQuery("from TurnoVacacionalJPA order by fechainicio").getResultList();
	}catch(Exception e) {
		return null;
	}
	return turnos;
}

@Override
public boolean setCalendarioSeccion(int ano, SeccionAlmacenJPA seccion, Turno turno, int numOperarios) {
	SeccionAlmacenJPA sec=null;
	CalendarioSeccionJPA calS=null;
	CalendarioEmpresaJPA calE=null;
	int id=nuevoIdCalendarioSeccion();
	try {
		calE =(CalendarioEmpresaJPA) entman.createQuery("from CalendarioEmpresaJPA WHERE id = " + ano).getSingleResult();
		calS = new CalendarioSeccionJPA(id,turno,numOperarios);
		entman.persist(calS);
		calS.setSeccion(seccion);
		calS.setCalendarioEmpresa(calE);
		entman.merge(calS);
		}catch (Exception e) {
			return false;
		}
	return true;
	}

public int nuevoIdCalendarioSeccion()throws PersistenceException {
	Integer id=0;
	Collection<CalendarioSeccionJPA> p = entman.createQuery("FROM CalendarioSeccionJPA").getResultList();
	Integer maxId = 0;
	for (Iterator<CalendarioSeccionJPA> iterator = p.iterator(); iterator.hasNext();) {
		CalendarioSeccionJPA sec = (CalendarioSeccionJPA) iterator.next();
		id= sec.getId();
		if (id > maxId) maxId = id;
		}
	maxId=maxId+1;
	return maxId;
	}

@Override
public CalendarioSeccionJPA getCalendarioSeccionAno(int ano, SeccionAlmacenJPA seccion) {
	CalendarioSeccionJPA cal;
	try {
	cal = (CalendarioSeccionJPA) entman.createQuery("from CalendarioSeccionJPA WHERE calendarioempresa_id = " + ano +" and seccionalmacen_id = "+seccion.getId()).getSingleResult();
	}catch (Exception e) {return null;}
	return cal;
}

@Override
public Collection<CalendarioSeccionJPA> getCalendariosSeccion() {
	Collection<CalendarioSeccionJPA> cal = entman.createQuery("from CalendarioSeccionJPA order by id DESC").getResultList();
    return cal;

}

@Override
public Collection<CalendarioSeccionJPA> getCalendarioSeccion(int ano) {
	Collection<CalendarioSeccionJPA> cal = entman.createQuery("from CalendarioSeccionJPA WHERE calendarioempresa_id = " + ano + " order by id desc").getResultList();
    return cal;
}

@Override
public boolean eliminarCalendarioSeccion(int ano, SeccionAlmacenJPA seccion) {
	try {
	int idCalSec=seccion.getId();
	Query query = entman.createQuery("DELETE FROM CalendarioPersonalJPA WHERE calendarioseccion_id = "+idCalSec);
	query = entman.createQuery("DELETE FROM CalendarioSeccionJPA WHERE calendarioempresa_id = "+ano + " and seccionalmacen_id =" +idCalSec);
	query.executeUpdate();
	Collection<CalendarioPersonalJPA> calP = entman.createQuery("from CalendarioPersonalJPA WHERE calendarioseccion_id ="+idCalSec).getResultList();
	CalendarioEmpresaJPA calE = entman.find(CalendarioEmpresaJPA.class, idCalSec);
	if ((calE!=null)&(calP==null)){
		entman.remove(calE);
		return true;}
	else {return false;}
	
		}catch(Exception e) {
			return false;
			}
        }
@Override
public CalendarioPersonalJPA getCalendarioPersonal(CalendarioSeccionJPA sec, OperarioJPA op) {
	CalendarioPersonalJPA calP=null;
	int idSec=sec.getId();
	int idOp=op.getId();
	try{	
	calP =(CalendarioPersonalJPA) entman.createQuery("from CalendarioPersonalJPA WHERE calendarioseccion_id = " + idSec + " and operario_id = "+idOp).getSingleResult();
	}catch (Exception e){
		int i=nuevoIdCalPersonal();
		CalendarioPersonalJPA nuevo = new CalendarioPersonalJPA(i);
		entman.persist(nuevo);
		nuevo.setCalendarioSeccion(sec);
		nuevo.setOperario(op);
		entman.merge(nuevo);
		return nuevo;
	}
		return calP;
	}

public int nuevoIdCalPersonal()throws PersistenceException {
	Integer id=0;
	Collection<CalendarioPersonalJPA> p = entman.createQuery("FROM CalendarioPersonalJPA").getResultList();
	Integer maxId = 0;
	for (Iterator<CalendarioPersonalJPA> iterator = p.iterator(); iterator.hasNext();) {
		CalendarioPersonalJPA cal = (CalendarioPersonalJPA) iterator.next();
		id= cal.getId();
		if (id > maxId) maxId = id;
		}
	maxId=maxId+1;
	return maxId;
	}


@Override
public Collection<DiaPersonalJPA> getDiasPersonales(CalendarioPersonalJPA cal) {
	Collection<DiaPersonalJPA> colP =null;
	try {
	   colP = entman.createQuery("FROM DiaPersonalJPA WHERE calendariopersonal_id = "+cal.getId()).getResultList();
	}catch (Exception e) {
		return null;
	}
	   return colP;
}
@Override
public Collection<PeriodoVacacionalJPA> getPeriodoVacacional(CalendarioPersonalJPA cal) {
	Collection<PeriodoVacacionalJPA> colP = entman.createQuery("FROM PeriodoVacacionalJPA WHERE calendariopersonal_id = "+cal.getId()).getResultList();
	return colP;
}
@Override
public CalendarioEmpresaJPA crearCalendarioEmpresa(Integer ano, Integer numDiasPers ) {
	CalendarioEmpresaJPA nuevo=null;
	try {
		nuevo = new CalendarioEmpresaJPA(ano);
		entman.persist(nuevo);
		nuevo.setNumDiasPersonales(numDiasPers);
		nuevo.setPropuestaCerrada(false);
		entman.merge(nuevo);
		}catch (Exception e) {
			return null;
		}
	return nuevo;
}
@Override
public boolean solicitarDiaPersonal(SeccionAlmacenJPA sec, OperarioJPA operario,CalendarioPersonalJPA calendarioPersonal, Date valueOf, String motivo) {
	Collection<OperarioJPA> colO = entman.createQuery("FROM OperarioJPA WHERE seccionalmacen_id = "+sec.getId()).getResultList();
	int diasAprobados=0;
	for (Iterator<OperarioJPA> iterator = colO.iterator(); iterator.hasNext();) {
		Collection <CalendarioPersonalJPA> calO= iterator.next().getCalendarios();
		for (Iterator<CalendarioPersonalJPA> iterator2 = calO.iterator(); iterator2.hasNext();) {
			Collection <DiaPersonalJPA> dia= iterator2.next().getDiasPersonales();
			for (Iterator<DiaPersonalJPA> iterator3 = dia.iterator(); iterator3.hasNext();) {
				if(iterator3.next().getDate().equals(valueOf)) {
					diasAprobados++;
					}
				}
			}
		}
	CalendarioEmpresaJPA calE=getCalendarioEmpresa(LocalDate.now().getYear());
	int max=calE.getNumDiasPersonales();
	if (max>diasAprobados) {
		try {
			DiaPersonalJPA nuevo = new DiaPersonalJPA(nuevoIdDiaPersonal(),valueOf, motivo);
			entman.persist(nuevo);
			CalendarioSeccionJPA cal= getCalendarioSeccionAno(LocalDate.now().getYear(), sec);
			calendarioPersonal=getCalendarioPersonal(cal, operario);
			nuevo.setCalendarioPersonal(calendarioPersonal);
			entman.merge(nuevo);
			}catch (Exception e) {
				return false;
			}
		return true;
	}
	return false;
}
public int nuevoIdDiaPersonal()throws PersistenceException {
	Integer id=0;
	Collection<DiaPersonalJPA> p = entman.createQuery("FROM DiaPersonalJPA").getResultList();
	Integer maxId = 0;
	for (Iterator<DiaPersonalJPA> iterator = p.iterator(); iterator.hasNext();) {
		DiaPersonalJPA dia = (DiaPersonalJPA) iterator.next();
		id= dia.getId();
		if (id > maxId) maxId = id;
		}
	maxId=maxId+1;
	return maxId;
	}
@Override
public boolean existeDiaPersonal(CalendarioPersonalJPA calendarioP, Date valueOf) {
	DiaPersonalJPA dia =null;
	int idCalP=calendarioP.getId();
	try {
		dia = (DiaPersonalJPA) entman.createQuery ("from DiaPersonalJPA WHERE calendariopersonal_id = "+ idCalP +" and date = '"+valueOf+ "'").getSingleResult();
		}
	catch (Exception e) {
		return false;}
	return true;
	}
@Override
public CalendarioEmpresaJPA setLimitePropuestaVac(Integer id, Date lim) {
	CalendarioEmpresaJPA calend = entman.find(CalendarioEmpresaJPA.class, id);
	calend.setlimitePropuestaVacaciones(lim);
	entman.merge(calend);
	return null;
}
@Override
public boolean solicitarPeriodoVacacional(CalendarioPersonalJPA calendarioPersonal,
		PeriodoVacacionalJPA periodoVacacional) {
	PeriodoVacacionalJPA nuevo=null;
	try {
		try{
			PeriodoVacacionalJPA a = (PeriodoVacacionalJPA) entman.createQuery ("from PeriodoVacacionalJPA WHERE calendariopersonal_id = "+ calendarioPersonal.getId() +" and primerdia = '"+periodoVacacional.getPrimerDia()+ "'").getSingleResult();
		}catch (Exception a) {
			nuevo = new PeriodoVacacionalJPA(nuevoIdDiaPeriodoVacacional(),periodoVacacional.getTipo(),periodoVacacional.getPrimerDia(),periodoVacacional.getUltimoDia());
			entman.persist(nuevo);
			nuevo.setCalendarioPersonal(calendarioPersonal);;
			entman.merge(nuevo);
			return true;
		}
		
		}catch (Exception e) {
			return false;
		}
	return false;
	  
}
public int nuevoIdDiaPeriodoVacacional()throws PersistenceException {
	Integer id=0;
	Collection<PeriodoVacacionalJPA> p = entman.createQuery("FROM PeriodoVacacionalJPA").getResultList();
	Integer maxId = 0;
	for (Iterator<PeriodoVacacionalJPA> iterator = p.iterator(); iterator.hasNext();) {
		PeriodoVacacionalJPA per = (PeriodoVacacionalJPA) iterator.next();
		id= per.getId();
		if (id > maxId) maxId = id;
		}
	maxId=maxId+1;
	return maxId;
	}
@Override
public PeriodoVacacionalJPA getPeriodoVacacionalVerano(CalendarioPersonalJPA calendarioPersonal) {
	PeriodoVacacionalJPA nuevo=null;
	try {
		nuevo = (PeriodoVacacionalJPA) entman.createQuery ("from PeriodoVacacionalJPA WHERE calendariopersonal_id = "+ calendarioPersonal.getId() +" and tipo = 0").getSingleResult();
		}catch (Exception a) {
			return null;
		}		
	return nuevo;
	}
@Override
public PeriodoVacacionalJPA getPeriodoVacacionalInvierno(CalendarioPersonalJPA calendarioPersonal) {
	PeriodoVacacionalJPA nuevo=null;
	try {
		nuevo = (PeriodoVacacionalJPA) entman.createQuery ("from PeriodoVacacionalJPA WHERE calendariopersonal_id = "+ calendarioPersonal.getId() +" and tipo = 1").getSingleResult();
		}catch (Exception a) {
			return null;
		}		
	return nuevo;
	}
@Override
public CalendarioPersonalJPA getCalendarioPersonal(int id) {
	CalendarioPersonalJPA cal = entman.find(CalendarioPersonalJPA.class, id);
    return cal;
}
@Override
public void eliminarPeriodoVacacional(PeriodoVacacionalJPA periodo) {
	Query query = entman.createQuery("DELETE FROM PeriodoVacacionalJPA WHERE id = " + periodo.getId());
	query.executeUpdate();
	return;
}
@Override
public PeriodoVacacionalJPA updatePeriodoVacacional(PeriodoVacacionalJPA periodo, String iniTurnoV,
		String finTurnoV) {
	PeriodoVacacionalJPA per= entman.find(PeriodoVacacionalJPA.class, periodo.getId());
	per.setPrimerDia(Date.valueOf(iniTurnoV));
	per.setUltimoDia(Date.valueOf(finTurnoV));
	entman.merge(per);
	return per;
}
@Override
public void eliminarDiaPersonal(DiaPersonalJPA dia) {
	Query query = entman.createQuery("DELETE FROM DiaPersonalJPA WHERE id = " + dia.getId());
	query.executeUpdate();
	return;
	
}
@Override
public DiaPersonalJPA updateDiaPersonal(DiaPersonalJPA dia, String date,
		String motivo) {
	DiaPersonalJPA d= entman.find(DiaPersonalJPA.class, dia.getId());
	d.setDate(Date.valueOf(date));;
	d.setMotivo(motivo);
	entman.merge(d);
	return d;
}
@Override
public void crearPeriodoVacacional(CalendarioPersonalJPA calPersonal,
		TipoVacaciones periodo, String iniTurno, String finTurno) {
	PeriodoVacacionalJPA nuevo=null;
	nuevo = new PeriodoVacacionalJPA(nuevoIdDiaPeriodoVacacional(),periodo,Date.valueOf(iniTurno),Date.valueOf(finTurno));
			entman.persist(nuevo);
			nuevo.setCalendarioPersonal(calPersonal);;
			entman.merge(nuevo);
			return;
}
@Override
public DiaPersonalJPA crearDiaPersonal(CalendarioPersonalJPA calPersonal, String date, String motivo) {
	DiaPersonalJPA nuevo=null;
	nuevo = new DiaPersonalJPA(nuevoIdDiaPersonal(),Date.valueOf(date),motivo);
			entman.persist(nuevo);
			nuevo.setCalendarioPersonal(calPersonal);;
			entman.merge(nuevo);
			return nuevo;
	
}
@Override
public void cerrarPropuestaVacacional() {
	//Año
	int anno=LocalDate.now().getYear();
	//Selecciona calendario año
	CalendarioEmpresaJPA calendarioEmpresa=getCalendarioEmpresaPorId(anno);
	//Lista de turnos del año
	Collection<TurnoVacacionalJPA> turnosVacaciones=getTurnoVacacional(anno);
	//Lista calendarios secciones del año
	Collection<CalendarioSeccionJPA> calendariosSecciones =getCalendarioSeccion(anno);
	for (Iterator<CalendarioSeccionJPA> iterCalendarios= calendariosSecciones.iterator(); iterCalendarios.hasNext();) {
		CalendarioSeccionJPA calendarioSeccion = (CalendarioSeccionJPA) iterCalendarios.next();
		int maxOperariosTurno=calendarioSeccion.getOperariosTurnoVacaciones();
		Collection<TurnoVacacionalJPA> turnosVacacionesSeccion=turnosVacaciones;
		Collection<TurnoVacacionalJPA> turnosVacacionesAux=new ArrayList<TurnoVacacionalJPA>();
		Collection<CalendarioPersonalJPA> calendariosPersonalesSeccion=calendarioSeccion.getCalendariosPersonales();
		List<PeriodoVacacionalJPA> periodosPersonalesACambiar= new ArrayList <PeriodoVacacionalJPA>();		
		for (Iterator<TurnoVacacionalJPA> iterTurno = turnosVacaciones.iterator(); iterTurno.hasNext();) {
			if(periodosPersonalesACambiar.isEmpty()==false) periodosPersonalesACambiar.clear();
			TurnoVacacionalJPA tur = (TurnoVacacionalJPA) iterTurno.next();
			Date turno =tur.getFechaInicio();
			int numOperarios=0;			
			for (Iterator<CalendarioPersonalJPA> iterCalendariosPersonalesSec= calendariosPersonalesSeccion.iterator(); iterCalendariosPersonalesSec.hasNext();) {
				CalendarioPersonalJPA calendarioPersonal = (CalendarioPersonalJPA) iterCalendariosPersonalesSec.next();
    			Collection<PeriodoVacacionalJPA> periodosVacacionales= calendarioPersonal.getPeriodoVacacional();
    			for (Iterator<PeriodoVacacionalJPA> iterPeriodosVacacionales= periodosVacacionales.iterator(); iterPeriodosVacacionales.hasNext();) {
    				PeriodoVacacionalJPA periodo = (PeriodoVacacionalJPA) iterPeriodosVacacionales.next();
    				if (periodo.getPrimerDia().compareTo(turno)==0) {
    					numOperarios++;
       				    periodosPersonalesACambiar.add(periodo);
    					}
    				}
    			}
			if(numOperarios<maxOperariosTurno) {
				turnosVacacionesAux.add(tur);
				}
			}		
		for (Iterator<TurnoVacacionalJPA> iterTurno = turnosVacaciones.iterator(); iterTurno.hasNext();) {
			if(periodosPersonalesACambiar.isEmpty()==false) periodosPersonalesACambiar.clear();
			TurnoVacacionalJPA tur = (TurnoVacacionalJPA) iterTurno.next();
			Date turno =tur.getFechaInicio();
			int numOperarios=0;			
			for (Iterator<CalendarioPersonalJPA> iterCalendariosPersonalesSec= calendariosPersonalesSeccion.iterator(); iterCalendariosPersonalesSec.hasNext();) {
				CalendarioPersonalJPA calendarioPersonal = (CalendarioPersonalJPA) iterCalendariosPersonalesSec.next();
    			Collection<PeriodoVacacionalJPA> periodosVacacionales= calendarioPersonal.getPeriodoVacacional();
    			for (Iterator<PeriodoVacacionalJPA> iterPeriodosVacacionales= periodosVacacionales.iterator(); iterPeriodosVacacionales.hasNext();) {
    				PeriodoVacacionalJPA periodo = (PeriodoVacacionalJPA) iterPeriodosVacacionales.next();
    				if (periodo.getPrimerDia().compareTo(turno)==0) {
    					numOperarios++;
       				    periodosPersonalesACambiar.add(periodo);
    					}
    				}
    			}
			if(numOperarios==maxOperariosTurno) {
				}
			else if(numOperarios<maxOperariosTurno) {
			}
			else if(numOperarios>maxOperariosTurno){
				Collection<PeriodoVacacionalJPA> calendarioPersonalNoAprobado= solicitudesVacacionesACambiar(periodosPersonalesACambiar, maxOperariosTurno, numOperarios);
				cambiarSolicitudes(calendarioPersonalNoAprobado, turnosVacacionesAux);
				}
			}//finTurnos
		}//finSecciones
	CalendarioEmpresaJPA cal = entman.find(CalendarioEmpresaJPA.class, LocalDate.now().getYear());
	cal.setPropuestaCerrada(true);
	entman.persist(cal);
	
	return;
	}

private Collection<PeriodoVacacionalJPA> solicitudesVacacionesACambiar(List<PeriodoVacacionalJPA> periodos, int maxOp, int op) {
	Collection<PeriodoVacacionalJPA> col=new ArrayList<PeriodoVacacionalJPA>();;
	int maxOperarios=maxOp;
	int numOperarios=op;
	int asignados=0;
	int ano=LocalDate.now().getYear();
	List<PeriodoVacacionalJPA> listaPeriodos=periodos;
	PeriodoVacacionalJPA[][] vector= new PeriodoVacacionalJPA[numOperarios][2];
	TipoVacaciones tipo= periodos.get(0).getTipo();
	for (Iterator<PeriodoVacacionalJPA> iterPeriodo= periodos.iterator(); iterPeriodo.hasNext();) {
		PeriodoVacacionalJPA calendario = (PeriodoVacacionalJPA) iterPeriodo.next();
		OperarioJPA operario= calendario.getCalendarioPersonal().getOperario();
		SeccionAlmacenJPA sec=calendario.getCalendarioPersonal().getOperario().getSeccion();
		CalendarioSeccionJPA calSecAnt=null;
		if(getCalendarioSeccionAno(ano-1, sec)==null) {calSecAnt= getCalendarioSeccionAno(ano, sec);}
		else calSecAnt= getCalendarioSeccionAno(ano-1, sec);
		CalendarioPersonalJPA calAnterior=null;
		if(getCalendarioPersonal(calSecAnt, operario)==null) {getCalendarioPersonal(getCalendarioSeccionAno(ano, sec), operario);}
		else calAnterior =getCalendarioPersonal(calSecAnt, operario);
		if (tipo==TipoVacaciones.Verano) {
			PeriodoVacacionalJPA p=getPeriodoVacacionalVerano(calAnterior);
			boolean escrito=false;
			int i=0;
			while((escrito==false)&(i<numOperarios)) {
				if(vector[i][0]==null) {
	    			vector[i][0]=calendario;
		    		vector[i][1]=p;
		    		escrito=true;
		    		}
				else if(difPeriodos(vector[i][1], calendario)<difPeriodos(p, calendario)) {
		    		int j =i+1;
		    		PeriodoVacacionalJPA aux1=null;
		    		PeriodoVacacionalJPA aux2=null;				
		    		do{
			    		aux1=vector[j+1][0];
			    		aux2=vector[j+1][1];
			    		vector[j][0]=vector[i][0];
			    		vector[j][1]=vector[i][1];
			    		j++;
			    		}while (vector[j][0]!=null);
		    		vector[i][0]=calendario;
		    		vector[i][1]=p;
		    		escrito=true;
		    		}
				if (i<numOperarios){
					i++;
					}
		    	}
			}			
		else{
			PeriodoVacacionalJPA p=getPeriodoVacacionalInvierno(calAnterior);
			boolean escrito=false;
			int i=0;
			while((escrito==false)&(i<numOperarios)) {
				if(vector[i][0]==null) {
	    			vector[i][0]=calendario;
		    		vector[i][1]=p;
		    		escrito=true;
		    		}
				else if(difPeriodos(vector[i][1], calendario)<difPeriodos(p, calendario)) {
		    		int j =i+1;
		    		PeriodoVacacionalJPA aux1=null;
		    		PeriodoVacacionalJPA aux2=null;				
		    		do{
			    		aux1=vector[j+1][0];
			    		aux2=vector[j+1][1];
			    		vector[j][0]=vector[i][0];
			    		vector[j][1]=vector[i][1];
			    		j++;
			    		}while (vector[j][0]!=null);
		    		vector[i][0]=calendario;
		    		vector[i][1]=p;
		    		escrito=true;
		    		}
				if (i<numOperarios){
					i++;
					}
		    	}

			}
		}
	for(int j=maxOp;j<numOperarios;j++) {
		  col.add(vector[j][0]);
		  }
	return col;
	}

public int difPeriodos(PeriodoVacacionalJPA periodo, PeriodoVacacionalJPA periodo2) {
    PeriodoVacacionalJPA p=periodo;
    PeriodoVacacionalJPA p2=periodo2;
    return Math.abs(p.getPrimerDia().compareTo(p2.getPrimerDia()));
    }

private void cambiarSolicitudes(Collection<PeriodoVacacionalJPA> calendarioPersonalNoAprobado, Collection<TurnoVacacionalJPA> turnosVacacionesSeccion) {
	Collection<TurnoVacacionalJPA> periodosVaca=turnosVacacionesSeccion;
	TurnoVacacionalJPA turno=null; 
	CalendarioSeccionJPA calSec=null;
	CalendarioPersonalJPA calPers=null;
	TipoVacaciones vac=null;
	for (Iterator<PeriodoVacacionalJPA> iter = calendarioPersonalNoAprobado.iterator(); iter.hasNext();) {
		Collection<TurnoVacacionalJPA> periodosVacaAux=periodosVaca;
		PeriodoVacacionalJPA per = (PeriodoVacacionalJPA) iter.next();
		Date fecha= per.getPrimerDia();
		calPers=per.getCalendarioPersonal();
		calSec=calPers.getCalendarioSeccion();
	    vac=per.getTipo();
		int i = 365;
		for (Iterator<TurnoVacacionalJPA> iter2 = periodosVacaAux.iterator(); iter2.hasNext();) {
			TurnoVacacionalJPA per2 = (TurnoVacacionalJPA) iter2.next();
			Date fecha2= per2.getFechaInicio();
			if (Math.abs(fecha2.compareTo(fecha))<i) {
				i=Math.abs(fecha2.compareTo(fecha));
				turno=per2;
				}
			}
		PeriodoVacacionalJPA cal =null;
		if(vac==TipoVacaciones.Invierno) {
		     cal = (PeriodoVacacionalJPA) entman.createQuery ("from PeriodoVacacionalJPA WHERE calendariopersonal_id = "+ calPers.getId() +" and tipo = 1").getSingleResult();
		}
		else {
			cal = (PeriodoVacacionalJPA) entman.createQuery ("from PeriodoVacacionalJPA WHERE calendariopersonal_id = "+ calPers.getId() +" and tipo = 0").getSingleResult();
		}
		if(cal!=null) {
    		cal.setPrimerDia(turno.getFechaInicio());
    		cal.setUltimoDia(turno.getFechaFin());
    		entman.merge(cal);
    		Collection<CalendarioPersonalJPA> operariosCal=entman.createQuery("FROM CalendarioPersonalJPA WHERE calendarioseccion_id = "+calSec.getId()).getResultList();
    		if (calSec.getOperariosTurnoVacaciones()==operariosCal.size()) {
	    		periodosVaca.remove(turno);
	    		}
	    	}
		}
	return ;
}

}






