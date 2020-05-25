/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.faces.context.*;
import javax.servlet.http.HttpSession;
import jpa.RrhhJPA;
import jpa.PersonalJPA;
import jpa.JefeAlmacenJPA;
import jpa.JefeEquipoJPA;
import jpa.CalendarioSeccionJPA;
import jpa.EncargadoJPA;
import jpa.InstalacionJPA;
import jpa.OperarioJPA;
import jpa.PersonalAlmacenJPA;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;
import ejb.SistemaUsuariosFacadeRemote;

/**
 * EJB Sesion clase Bean
 */

@Stateless
public class SistemaUsuariosFacade implements SistemaUsuariosFacadeRemote {
	
	//Persistence Unit Context
	@PersistenceContext(unitName="PracticalCase") 
	private EntityManager entman;
	
	@Override
	public boolean login(int id, String password) {	
		String rol = "";
		String usuarioNombre = "";
		
	/*Usuario para iniciar las pruebas que no esta creado en la B.D
	 *se podrá probar la BD, creando los diferentes tipos de usario. 
	 *Tomará el rol de RRHH ya que este tipo de usuario se encarga 
	 *de la gestión del personal de almacen. La primera tarea será la creación 
	 *en el sistema de un usuario de RRHH.
	 */
		if(id==-1234 & password.equals("administradorTW")) {
			rol = "rrhh";
			usuarioNombre = "administradorTW";
		}
		
	RrhhJPA rrhh = entman.find(RrhhJPA.class, id);

	if(rrhh != null) {
		if (rrhh.getPassword().equals(password)) {
			rol = "rrhh";
			usuarioNombre = rrhh.getnombre();
		}
	} else {
		JefeAlmacenJPA jefeAlmacen = entman.find(JefeAlmacenJPA.class, id);
		if(jefeAlmacen != null) {
			if (jefeAlmacen.getPassword().equals(password)) {
				rol = "jefeAlmacen";
				usuarioNombre = jefeAlmacen.getnombre() + " " + jefeAlmacen.getapellidos();
			}
		} else {				
			EncargadoJPA encargado = entman.find(EncargadoJPA.class, id);
			if(encargado != null) {
				if (encargado.getPassword().equals(password)) {
					rol = "encargado";
					usuarioNombre = encargado.getnombre() + " " + encargado.getapellidos();
				}
			} else {
				JefeEquipoJPA jefeEquipo = entman.find(JefeEquipoJPA.class, id);
				if(jefeEquipo != null) {
					if (jefeEquipo.getPassword().equals(password)) {
						rol = "jefeEquipo";
						usuarioNombre = jefeEquipo.getnombre() + " " + jefeEquipo.getapellidos();
						}
					}else {
						OperarioJPA operario = entman.find(OperarioJPA.class, id);
						if(operario != null) {
							if (operario.getPassword().equals(password)) {
								rol = "operario";
								usuarioNombre = operario.getnombre() + " " + operario.getapellidos();
								}
							}else {
								PersonalJPA personal = entman.find(PersonalJPA.class, id);
								if(personal != null) {
									if (personal.getPassword().equals(password)) {
										rol = "personal";
										usuarioNombre = personal.getnombre() + " " + personal.getapellidos();
										}
									}
								}
						}
				}
			}
		}
		
	if (!rol.equals("")) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		session.setAttribute("id", String.valueOf(id));
		session.setAttribute("password", password);
		session.setAttribute("rol", rol);
		session.setAttribute("usuarioNombre", usuarioNombre);
		return true;
	}
	
	return false;
}

	@Override
	public void logout() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		session.invalidate();
	}
	
	@Override
	public PersonalJPA getUsuario(int id) {
		PersonalJPA personal = entman.find(PersonalJPA.class, id);
		return personal;
	}

	@Override
	public boolean cambioPassword(PersonalJPA personal, String newPassword) {
		boolean resultat=false;
		if(newPassword!=null) {
			if(newPassword != "") {
				personal.setPassword(newPassword);
				entman.merge(personal);
				resultat=true;
				}
			}
		return resultat;
	}

	@Override
	public String crearPersonal(int id, String nif, String nombre, String apellidos, String password, String email,String rol, SeccionAlmacenJPA seccion, Turno turno) {
		PersonalJPA per=null;
		if (nif.equals("") || nombre.equals("") || apellidos.equals("") || password.equals("") ) {
			return "Falta introducir datos";
		}
		try {
		per=(PersonalJPA) entman.createQuery("Select b FROM PersonalJPA b where b.nif like '" + nif + "'").getSingleResult();
		}
		catch (Exception e){
			}
		if (per!=null) {
			return "Ya existe NIF empleado";
			}		
		if((rol.equals("operario") || rol.equals("jefeEquipo") || rol.equals("encargado"))&&(seccion==null)) {
			
				return "Falta seleccionar sección";
		}
		if((rol.equals("operario") || rol.equals("jefeEquipo")) && (turno==null)) {
				return "Turno no encontrado";
        }		
	    Date fecha = Date.valueOf(LocalDate.now());
		if (rol.equals("operario")) {
			OperarioJPA nuevo;
			try {
			nuevo = new OperarioJPA(id,nif,nombre,apellidos,password,email,fecha,turno,seccion);
			}
			catch (Exception e) {
				return "Operario sin turno o sección";	
			}
			entman.persist(nuevo);
			}
		else if (rol.equals("jefeEquipo")) {
			JefeEquipoJPA nuevo;
			try {
    		nuevo = new JefeEquipoJPA(id,nif,nombre,apellidos,password,email,fecha,turno,seccion);
			}
			catch (Exception e) {
				return "Jefe de equipo sin turno o sección";	
			}
    		entman.persist(nuevo);
           	}
    	else if (rol.equals("encargado")) {
    		EncargadoJPA nuevo;
    		try {
    		nuevo = new EncargadoJPA(id,nif,nombre,apellidos,password,email,fecha,seccion);
    		}
			catch (Exception e) {
				return "Encargado sin sección";	
			}
   			entman.persist(nuevo);				
       		}
   		else if (rol.equals("jefeAlmacen")) {
   			JefeAlmacenJPA nuevo = new JefeAlmacenJPA(id,nif,nombre,apellidos,password,email,fecha);
    		entman.persist(nuevo);
    		}
	    else if (rol.equals("rrhh")) {
    		RrhhJPA nuevo = new RrhhJPA(id,nif,nombre,apellidos,password,email,fecha);
	    	entman.persist(nuevo);
	    	}
	    else {
	    	return ("NO existe cargo");
	    	}
		PersonalJPA person= entman.find(PersonalJPA.class, id);
		if (person==null) {
			return "REVISA LOS DATOS INTRODUCIDOS";
		}
		return ("SI");
	}
	@Override
	public boolean updatePersonal(int id, String nif, String nombre, String apellidos, String password, String email,
			String rol, SeccionAlmacenJPA seccion, Turno turno) {
        if (rol.equals("operario")) {
 			OperarioJPA je =null;
        	je = entman.find(OperarioJPA.class, id);
    		je.setNif(nif);
   			je.setnombre(nombre);
   			je.setapellidos(apellidos);
   			je.setPassword(password);
   			je.setEmail(email);   
   			je.setSeccion(seccion);   
   			je.setTurno(turno);        	
   			entman.merge(je);
        	}
		else if (rol.equals("jefeEquipo")) {
			JefeEquipoJPA je =null;
        	je = entman.find(JefeEquipoJPA.class, id);
       		je.setNif(nif);
   			je.setnombre(nombre);
   			je.setapellidos(apellidos);
   			je.setPassword(password);
   			je.setEmail(email);   
   			je.setSeccion(seccion);   
   			je.setTurno(turno);        	
   			entman.merge(je);
    		}

    	else if (rol.equals("encargado")) {
			EncargadoJPA je =null;
        	je = entman.find(EncargadoJPA.class, id);
       		je.setNif(nif);
   			je.setnombre(nombre);
   			je.setapellidos(apellidos);
   			je.setPassword(password);
   			je.setEmail(email);   
   			je.setSeccion(seccion);      	
   			entman.merge(je);
        	}
   		else if (rol.equals("jefeAlmacen")) {
			JefeAlmacenJPA je =null;
        	je = entman.find(JefeAlmacenJPA.class, id);
        	je.setNif(nif);
    		je.setnombre(nombre);
    		je.setapellidos(apellidos);
    		je.setPassword(password);
    		je.setEmail(email);   
    		entman.merge(je);
    		}
	    else if (rol.equals("rrhh")) {
			RrhhJPA je =null;
        	je = entman.find(RrhhJPA.class, id);
       		je.setNif(nif);
   			je.setnombre(nombre);
   			je.setapellidos(apellidos);
   			je.setPassword(password);
   			je.setEmail(email);   
   			entman.merge(je);
        	}
        return true;
	}
	
	
	@Override
	public boolean crearSeccion(String nombre, String descrip, Turno turno)throws PersistenceException {
		try
		{
			@SuppressWarnings("unchecked")
			Collection<SeccionAlmacenJPA> p = entman.createQuery("FROM SeccionAlmacenJPA b").getResultList();
			Integer maxId = 0;
			for (Iterator<SeccionAlmacenJPA> iterator = p.iterator(); iterator.hasNext();) {
				SeccionAlmacenJPA sec = (SeccionAlmacenJPA) iterator.next();
				if(sec.getNombre().equals(nombre)) {return false;}
				Integer secId= sec.getId();
				if (secId > maxId) maxId = secId;
			}
			SeccionAlmacenJPA sec = new SeccionAlmacenJPA(maxId+1,nombre, descrip,turno);
			entman.persist(sec);
			return true;
		}catch (PersistenceException e) {
			System.out.println(e);
		}
		
		return false;
	}

	@Override
	public SeccionAlmacenJPA getSeccion(int id) {
		SeccionAlmacenJPA seccion = entman.find(SeccionAlmacenJPA.class, id);
		if (seccion!=null) {
			return seccion;
		}
		return null;
	}

	@Override
	public int getSeccionNombr(String nomBuscado) {
		SeccionAlmacenJPA sec;
		try {
		sec= (SeccionAlmacenJPA) entman.createQuery("Select b FROM SeccionAlmacenJPA b where b.nombre like '" + nomBuscado +"'").getSingleResult();
		}catch (Exception e) {
			return(-1);
		}
		if (sec!=null)return sec.getId();
		else return(-1);
	}
	
	public int getPersonalNombr(String nomBuscado) {
		PersonalJPA per;
		try {
		per= (PersonalJPA) entman.createQuery("Select b FROM PersonalJPA b where b.nombre like '" + nomBuscado +"'").getSingleResult();
		}catch (Exception e) {
			return(-1);
		}
		if (per!=null)return per.getId();
		else return(-1);	
	}
	@Override
	public boolean eliminarSeccion(int id) {
		SeccionAlmacenJPA seccion = entman.find(SeccionAlmacenJPA.class, id);
		if ((seccion!=null)){
			try {
			entman.remove(seccion);
			return true;
			}
			catch(Exception e) {
				return false;
			}
			}return false;	
		}

	@Override
	public boolean eliminarPersonal(int idUs) {
		boolean resultat = false; //ens diu si hem pugut eliminar la visita
		String tipo=getTipoPersonal(idUs);
		if(tipo=="operario") {
			Query query = entman.createQuery("DELETE FROM CalendarioPersonalJPA WHERE id = " + idUs);
			query.executeUpdate();
		}
		Query query2 = entman.createQuery("DELETE FROM PersonalJPA WHERE id = " + idUs);
		int rowsDeleted = query2.executeUpdate();
		if (rowsDeleted > 0)
			resultat = true;		
		return resultat;

		}
	
	@Override
	public Collection<SeccionAlmacenJPA> getSecciones() {
		@SuppressWarnings("unchecked")
		Collection<SeccionAlmacenJPA> secciones = entman.createQuery("from SeccionAlmacenJPA order by nombre").getResultList();
	    return secciones;

	}

	@Override
	public boolean updateSeccion(int id, String nombre, String descripcion) {
		SeccionAlmacenJPA seccion = entman.find(SeccionAlmacenJPA.class, id);
		String nombre1=seccion.getNombre();
		String descr1=seccion.getDescripcion();
		seccion.setNombre(nombre);
		seccion.setDescripcion(descripcion);
		entman.merge(seccion);
		if ((nombre1==seccion.getNombre()) && (descr1==seccion.getDescripcion()))
		return false;
		else return true;
	}
	
	@Override
	public Collection<PersonalJPA> getPersonal() {
		@SuppressWarnings("unchecked")
		Collection<PersonalJPA> personal = entman.createQuery("from PersonalJPA order by nombre").getResultList();
	    return personal;

	}
	@Override
	public Collection<PersonalJPA> getPersonalConNombre(String nom) {
		@SuppressWarnings("unchecked")
		Collection<PersonalJPA> personal = entman.createQuery("Select b FROM PersonalJPA b where b.nombre like '" + nom +"' order by nombre").getResultList();
	    return personal;
	}
	
	public int nuevoIdPersonal()throws PersistenceException {
		Integer id=0;
		@SuppressWarnings("unchecked")
		Collection<PersonalJPA> p = entman.createQuery("FROM PersonalJPA").getResultList();
		Integer maxId = 0;
		for (Iterator<PersonalJPA> iterator = p.iterator(); iterator.hasNext();) {
			PersonalJPA sec = (PersonalJPA) iterator.next();
			id= sec.getId();
			if (id > maxId) maxId = id;
			}
		maxId=maxId+1;
		return maxId;
		}

	@Override
	public String getTipoPersonal(int id) {
		if(entman.find(OperarioJPA.class, id)!=null) return"operario";
		else if(entman.find(JefeEquipoJPA.class, id)!=null) return"jefeEquipo";
		else if(entman.find(EncargadoJPA.class, id)!=null) return"encargado";
		else if(entman.find(JefeAlmacenJPA.class, id)!=null) return"jefeAlmacen";
		else if(entman.find(RrhhJPA.class, id)!=null) return"rrhh";
		return null;
	}
	
	@Override
	public Turno getTurnoEmpleado(int id, String rol){
		if(rol.equals("operario")) {
			OperarioJPA p =entman.find(OperarioJPA.class, id);
			return p.getTurno();
		}
		else if(rol.equals("jefeEquipo")){
			JefeEquipoJPA p =entman.find(JefeEquipoJPA.class, id);
			return p.getTurno();
		}
		return null;
	}

	@Override
	public SeccionAlmacenJPA getSeccionEmpleado(int id) {
		PersonalJPA p =entman.find(PersonalJPA.class, id);
		if(p!=null) {
			PersonalAlmacenJPA pp =entman.find(PersonalAlmacenJPA.class, id); 
		    return pp.getSeccion();}
		return null;
		}
	@SuppressWarnings("unchecked")
	@Override
	public Collection<OperarioJPA> getOperarioSeccion(SeccionAlmacenJPA dataSeccion) {
		Collection<OperarioJPA> ops;
		try {
		ops= (Collection<OperarioJPA>) entman.createQuery("from OperarioJPA WHERE seccionalmacen_id = " + dataSeccion.getId()).getResultList();
		}catch(Exception e) {return null;}
		return ops;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeSeccionTurno(String seccion, String turno) {
		Collection<SeccionAlmacenJPA> sec;
		SeccionAlmacenJPA s;
		try {
		sec= (Collection<SeccionAlmacenJPA>) entman.createQuery("Select b FROM SeccionAlmacenJPA b where b.nombre like '" + seccion +"'").getResultList();
		for (Iterator<SeccionAlmacenJPA> iter = sec.iterator(); iter.hasNext();)
		{
			s=iter.next();
			if(s.getTurno().name().equals(turno))return true;
		}
		return false;
		}catch (Exception e) {
			return false;	
		}
		}

	@Override
	public SeccionAlmacenJPA getSeccionPorNombre(String sec) {
		SeccionAlmacenJPA s= (SeccionAlmacenJPA) entman.createQuery("Select b FROM SeccionAlmacenJPA b where b.nombre like '" + sec +"'").getSingleResult();
		if(s!=null) {
			return s;
		}else {
			return null;
			}
		}

	@Override
	public OperarioJPA getOperario(int id) {
		OperarioJPA p =entman.find(OperarioJPA.class, id);
		return p;
	}

	@Override
	public JefeEquipoJPA getJefeEquipo(int id) {
		JefeEquipoJPA p =entman.find(JefeEquipoJPA.class, id);
		return p;
	}

	@Override
	public EncargadoJPA getEncargado(int id) {
		EncargadoJPA p =entman.find(EncargadoJPA.class, id);
		return p;
	}

	@Override
	public JefeAlmacenJPA getJefeAlmacen(int id) {
		JefeAlmacenJPA p =entman.find(JefeAlmacenJPA.class, id);
		return p;
	}

	@Override
	public RrhhJPA getRrhh(int id) {
		RrhhJPA p =entman.find(RrhhJPA.class, id);
		return p;
		}

	@Override
	public Collection<OperarioJPA> getOperarios() {
		@SuppressWarnings("unchecked")
		Collection<OperarioJPA> personal = entman.createQuery("from OperarioJPA order by nombre").getResultList();
	    return personal;
	}

	@Override
	public Collection<OperarioJPA> getOperarioConNombre(String nom) {
		@SuppressWarnings("unchecked")
		Collection<OperarioJPA> personal = entman.createQuery("Select b FROM OperarioJPA b where b.nombre like '" + nom +"' order by nombre").getResultList();
	    return personal;
	}

	@Override
	public Collection <OperarioJPA> getOperarios(CalendarioSeccionJPA sec) {
		@SuppressWarnings("unchecked")
		Collection<OperarioJPA> personal = entman.createQuery("Select b FROM OperarioJPA b where b.seccionalmacen_id = " + sec.getSeccion().getId() ).getResultList();
	    return personal;
	}

	@Override
	public boolean desasignarInstalacion(SeccionAlmacenJPA dataSeccion, int id) {
		try {
		InstalacionJPA instalacion =entman.find(InstalacionJPA.class, id);
		@SuppressWarnings("unchecked")
		Collection<InstalacionJPA> instalaciones =entman.createQuery("from InstalacionJPA where b.seccionalmacen_id = "+dataSeccion.getId()).getResultList();
		instalaciones.remove(instalacion);
		SeccionAlmacenJPA seccion =entman.find(SeccionAlmacenJPA.class, dataSeccion.getId());
		seccion.setInstalaciones(null);
		entman.persist(seccion);
		seccion =entman.find(SeccionAlmacenJPA.class, dataSeccion.getId());
		seccion.setInstalaciones(instalaciones);
		entman.persist(seccion);
		}catch (Exception e) {
			return false;	
		}
		return true;
	}

}




