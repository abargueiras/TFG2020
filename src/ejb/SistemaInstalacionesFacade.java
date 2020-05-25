/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import jpa.InstalacionJPA;
import jpa.PuestoJPA;
import jpa.SeccionAlmacenJPA;
import ejb.SistemaInstalacionesFacadeRemote;

/**
 * EJB Sesion clase Bean
 */

@Stateless
public class SistemaInstalacionesFacade implements SistemaInstalacionesFacadeRemote {
	
	//Persistence Unit Context
	@PersistenceContext(unitName="PracticalCase") 
	private EntityManager entman;
   /*
    * Instalaciones
    */
	public int nuevoIdInstalacion()throws PersistenceException {
		Integer id=0;
		@SuppressWarnings("unchecked")
		Collection<InstalacionJPA> p = entman.createQuery("FROM InstalacionJPA").getResultList();
		Integer maxId = 0;
		for (Iterator<InstalacionJPA> iterator = p.iterator(); iterator.hasNext();) {
			InstalacionJPA in = (InstalacionJPA) iterator.next();
			id= in.getId();
			if (id > maxId) maxId = id;
			}
		maxId=maxId+1;
		return maxId;
		}
	
	@Override
	public boolean crearInstalacion(String nombre, String descripcion) {
		int id=nuevoIdInstalacion();
		InstalacionJPA instal=null;
		try {
			instal= new InstalacionJPA(id,nombre,descripcion);
			entman.persist(instal);
			}catch (Exception e) {
				return false;
			}
		return true;
		}


	@Override
	public InstalacionJPA getInstalacion(int id) {
		InstalacionJPA instal = entman.find(InstalacionJPA.class, id);
		return instal;
	}

	@Override
	public boolean eliminarInstalacion(InstalacionJPA instalacion) {
		try {
			entman.remove(instalacion);
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean eliminarInstalacion(int id) {
		try {
		InstalacionJPA instal = entman.find(InstalacionJPA.class, id);
		eliminarInstalacion(instal);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateInstalacion(int id, String nombre, String descripcion) {
		try {
		InstalacionJPA per= entman.find(InstalacionJPA.class, id);
		per.setNombre(nombre);
		per.setDescripcion(descripcion);		
		entman.merge(per);
		}catch(Exception e) {return false;}
		return true;
		}

	@SuppressWarnings({ "unchecked"})
	@Override
	public Collection<InstalacionJPA> getInstalaciones() {
		Collection<InstalacionJPA> instalaciones=null;
		try {
		instalaciones = entman.createQuery("from InstalacionJPA order by id").getResultList();
		}catch(Exception e) {
			return null;
		}
		return instalaciones;
	}

	@Override
	public InstalacionJPA getInstalacionNombre(String nom) {
		InstalacionJPA instalacion =null;
		try {
		instalacion = (InstalacionJPA) entman.createQuery("from InstalacionJPA WHERE nombre = '" + nom +"'").getSingleResult();
		}catch(Exception e) {
			return null;
		}    
		return instalacion;
	}
   /*
   * Puestos
   */
	public int nuevoIdPuesto()throws PersistenceException {
		Integer id=0;
		@SuppressWarnings("unchecked")
		Collection<PuestoJPA> p = entman.createQuery("FROM PuestoJPA").getResultList();
		Integer maxId = 0;
		for (Iterator<PuestoJPA> iterator = p.iterator(); iterator.hasNext();) {
			PuestoJPA pu = (PuestoJPA) iterator.next();
			id= pu.getId();
			if (id > maxId) maxId = id;
			}
		maxId=maxId+1;
		return maxId;
		}
	
	
	@Override
	public boolean crearPuesto(String nombre, String descripcion, int numOperarios, boolean requerido, InstalacionJPA instal) {
		int id=nuevoIdPuesto();
		PuestoJPA pos=null;
		try {
			pos= new PuestoJPA(id,nombre,descripcion,numOperarios,requerido);
			entman.persist(pos);
			pos.setInstalacion(instal);
			entman.merge(pos);
			}catch (Exception e) {
				return false;
			}
		return true;
	}

	@Override
	public PuestoJPA getPuesto(int id) {
		PuestoJPA pu = entman.find(PuestoJPA.class, id);
		return pu;
	}
	@Override
	public boolean eliminarPuesto(PuestoJPA pos) {
		PuestoJPA pu = null;
		try {
			pu = entman.find(PuestoJPA.class, pos);
			pu.setInstalacion(null);
			entman.persist(pu);
			entman.remove(pu);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean eliminarPuesto(int id) {
		try {
			PuestoJPA pu = entman.find(PuestoJPA.class, id);
			pu.setInstalacion(null);
			entman.persist(pu);
			pu = entman.find(PuestoJPA.class, id);
			entman.remove(pu);
			}catch(Exception e) {
				return false;
			}
			return true;
	}

	@Override
	public boolean updatePuesto(int id, String nombre, String descripcion, int numOperarios, boolean requerido, InstalacionJPA inst) {
		try {
		PuestoJPA per= entman.find(PuestoJPA.class, id);
		per.setNombre(nombre);
		per.setDescripcion(descripcion);
		per.setNumOperarios(numOperarios);
		per.setRequerido(requerido);
		per.setInstalacion(inst);
		entman.merge(per);
		}catch(Exception e) {return false;}
		return true;
		}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PuestoJPA> getPosiciones() {
		Collection<PuestoJPA> puestos=null;
		try {
		puestos = entman.createQuery("from PuestoJPA order by id").getResultList();
		}catch(Exception e) {
			return null;
		}
		return puestos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PuestoJPA> getPosicionNombre(String nom){
			Collection<PuestoJPA> puestos =null;
			try {
			puestos = (Collection<PuestoJPA>) entman.createQuery("from PuestoJPA WHERE nombre = '" + nom +"'").getResultList();
			}catch(Exception e) {
				return null;
			}    
			return puestos;
			}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<InstalacionJPA> getInstalaciones(SeccionAlmacenJPA dataSeccion) {
		Collection<InstalacionJPA> instalaciones=null;
		try {
		instalaciones = entman.createQuery("from InstalacionJPA WHERE seccionalmacen_id = "+dataSeccion.getId()).getResultList();
		}catch(Exception e) {
			return null;
		}
		return instalaciones;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PuestoJPA> getPuestos(InstalacionJPA inst) {
		Collection<PuestoJPA> puestos =null;
		try {
		puestos = (Collection<PuestoJPA>) entman.createQuery("from PuestoJPA WHERE instalacion_id = " + inst.getId()).getResultList();
		}catch(Exception e) {
			return null;
		}    
		return puestos;
		}

	@Override
	public void modificarInstalaciones(SeccionAlmacenJPA dataSeccion, Collection<InstalacionJPA> instalacionesLista) {
		try {
			SeccionAlmacenJPA pu = entman.find(SeccionAlmacenJPA.class, dataSeccion.getId());
			pu.setInstalaciones(instalacionesLista);
			entman.merge(pu);
			}catch(Exception e) {
				return;
			}
			return;
	}
	}


	

	



	

