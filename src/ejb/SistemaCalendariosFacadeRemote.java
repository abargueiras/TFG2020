/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;
import jpa.OperarioJPA;
import jpa.CalendarioEmpresaJPA;
import jpa.CalendarioSeccionJPA;
import jpa.CalendarioPersonalJPA;
import jpa.DiaPersonalJPA;
import jpa.DiaFestivoJPA;
import jpa.PeriodoVacacionalJPA;
import jpa.SeccionAlmacenJPA;
import jpa.TipoVacaciones;
import jpa.Turno;
import jpa.TurnoVacacionalJPA;

/**
 * EJB sesión interfaz remota
 */
@Remote
public interface SistemaCalendariosFacadeRemote {
	/*
	 * Dias festivos
	 */
	public DiaFestivoJPA crearDiaFestivo(LocalDate a, String festividadFestivo);
	public boolean eliminarDiaFestivoEmpresa(int id);	
	public Collection<DiaFestivoJPA> getFestivosEmpresa();
	public Collection<DiaFestivoJPA> getFestivoEmpresa(Date festivoBuscado);
	
	/*
	 * Dias personales
	 */
	public DiaPersonalJPA crearDiaPersonal(CalendarioPersonalJPA calendarioPersonal, String diaPersonal1Date,
			String diaPersonal1Motivo);	
	public void eliminarDiaPersonal(DiaPersonalJPA diaPersonal1);
	public DiaPersonalJPA updateDiaPersonal(DiaPersonalJPA diaPersonal1, String diaPersonal1Date,
			String diaPersonal1Motivo);
	public boolean solicitarDiaPersonal(SeccionAlmacenJPA sec, OperarioJPA operario, CalendarioPersonalJPA calendarioPersonal, java.sql.Date valueOf, String motivo);
	public boolean existeDiaPersonal(CalendarioPersonalJPA calendarioPersonal, java.sql.Date valueOf);
	public Collection<DiaPersonalJPA> getDiasPersonales(CalendarioPersonalJPA cal);	
	
	/*
	 * Periodos vacacionales
	 */
	public CalendarioEmpresaJPA setLimitePropuestaVac(Integer valueOf, java.sql.Date date);
	public boolean solicitarPeriodoVacacional(CalendarioPersonalJPA calendarioPersonal,
			PeriodoVacacionalJPA periodoVacacionalVerano);
	public PeriodoVacacionalJPA getPeriodoVacacionalVerano(CalendarioPersonalJPA calendarioPersonal);
	public PeriodoVacacionalJPA getPeriodoVacacionalInvierno(CalendarioPersonalJPA calendarioPersonal);
	
	/*
	 * Turnos y periodos vacacionales
	 */
	public boolean crearTurnoVacacional(java.sql.Date valueOf, java.sql.Date fin, TipoVacaciones tip);
	public boolean eliminarTurnoVacacionalEmpresa(Date id);
	public Collection<TurnoVacacionalJPA> getTurnosVacacionalesEmpresa();
	public Collection<TurnoVacacionalJPA> getTurnoVacacional(int ano);
	public void eliminarPeriodoVacacional(PeriodoVacacionalJPA periodoVacacionalVerano);
	public PeriodoVacacionalJPA updatePeriodoVacacional(PeriodoVacacionalJPA periodoVacacionalVerano, String iniTurnoV,
			String finTurnoV);
	public void crearPeriodoVacacional(CalendarioPersonalJPA calendarioPersonal,
			TipoVacaciones verano, String iniTurnoV, String finTurnoV);
	public void cerrarPropuestaVacacional();
	public Collection<PeriodoVacacionalJPA> getPeriodoVacacional(CalendarioPersonalJPA cal);
	
	/*
	 * Calendario empresa
	 */
	public boolean eliminarCalendarioEmpresa(int ano);
	public CalendarioEmpresaJPA crearCalendarioEmpresa(Integer ano, Integer numDiasFestivos);	
	public CalendarioEmpresaJPA getCalendarioEmpresaPorId(int id);
	public Collection<CalendarioEmpresaJPA> getCalendariosEmpresa();
	public CalendarioEmpresaJPA getCalendarioEmpresa(int id);

	/*
	 * Calendario sección
	 */
	public boolean eliminarCalendarioSeccion(int ano, SeccionAlmacenJPA seccion);	
	public boolean setCalendarioSeccion(int ano,SeccionAlmacenJPA seccion, Turno turno, int numOperarios);
	public CalendarioSeccionJPA getCalendarioSeccionAno(int ano, SeccionAlmacenJPA seccion);
	public Collection<CalendarioSeccionJPA> getCalendariosSeccion();
	public Collection<CalendarioSeccionJPA> getCalendarioSeccion(int ano);	
	
	/*
	 * Calendario personal
	 */
	public CalendarioPersonalJPA getCalendarioPersonal(CalendarioSeccionJPA calendarioSeccion, OperarioJPA operario);
	public CalendarioPersonalJPA getCalendarioPersonal(int parseInt);

}
