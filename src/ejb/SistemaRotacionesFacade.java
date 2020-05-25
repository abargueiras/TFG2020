/*
 * @author Antonio Bargueiras Sanchez, 2.020
 */
package ejb;

import java.sql.Date;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import jpa.OperarioJPA;
import jpa.PuestoJPA;
import jpa.RotacionJPA;
import jpa.PosicionRotacionJPA;
import jpa.SeccionAlmacenJPA;
import jpa.Turno;
import ejb.SistemaRotacionesFacadeRemote;

/**
 * EJB Sesion clase Bean
 */

@Stateless
public class SistemaRotacionesFacade implements SistemaRotacionesFacadeRemote {
	
	//Persistence Unit Context
	@PersistenceContext(unitName="PracticalCase") 
	private EntityManager entman;

	@Override
    public Collection<OperarioJPA> asignarPosicion(RotacionJPA rotacion, PuestoJPA p, Collection<OperarioJPA> operarios,java.sql.Date f) {
    	boolean asig=false;
    	int numOpPasados=operarios.size();
    	int numCoger=p.getNumOperarios();
    	int numOpNecesarios= p.getNumOperarios();
    	int [][]orden=new int [numOpPasados][2];
    	List<OperarioJPA> opsSelect=new ArrayList<OperarioJPA> ();
    	try {	
        	Iterator <OperarioJPA> iter = operarios.iterator();
        	int min=1000;
        	int veces=0;
        	int i=0;
        	while(iter.hasNext()) {
	     		asig=false;			
	    		OperarioJPA it=iter.next();
                long x= (Long) entman.createQuery("SELECT COUNT(*) from PosicionRotacionJPA WHERE operario_id = " + it.getId() + " and puesto_id = "+p.getId()+ " and fecha >= '"+ f+"'" ).getSingleResult();
	    		veces=(int) x;
                orden[i][0]=it.getId();
		        orden[i][1]=veces;
	    	    i++;
	    	    if(veces<min) {min=veces;}    		
    	  	}
    	for( int ii=0; ii < numOpPasados; ii++){//ordena la matriz 
	    	for( int j=0;j< 2; j++){
        		for(int x=0; x < numOpPasados; x++){
            		for(int y=0; y <2; y++){
                		if(orden[ii][1] < orden[x][1]){
                			int t = orden[ii][0];
	                    	int tt = orden[ii][1];
                    		orden[ii][0] = orden[x][0];
                    		orden[ii][1] = orden[x][1];
                     		orden[x][0] = t;
                    		orden[x][1] = tt;}
                		}
            		}
        		}
	    	}
    	for(int k=0;k<numCoger;k++) {
			int idOp=orden[k][0];
			iter = operarios.iterator();
			boolean es=false;
			while(es==false && iter.hasNext()) {
				OperarioJPA it2= iter.next();
				opsSelect.add(it2);					
				if(it2.getId()==idOp) {
					es=true;
					PosicionRotacionJPA nuevo = new PosicionRotacionJPA(nuevoIdPosicionRotacion(),f, it2,p);
					entman.persist(nuevo);
					nuevo.setRotacion(rotacion);
					entman.merge(nuevo);
					}
				}
			}
    	}catch (Exception e) {return null;}
		return opsSelect;
	}
	public int nuevoIdPosicionRotacion()throws PersistenceException {
		Integer id=0;
		@SuppressWarnings("unchecked")
		Collection<PosicionRotacionJPA> p = entman.createQuery("FROM PosicionRotacionJPA").getResultList();
		Integer maxId = 0;
		for (Iterator<PosicionRotacionJPA> iterator = p.iterator(); iterator.hasNext();) {
			PosicionRotacionJPA cal = (PosicionRotacionJPA) iterator.next();
			id= cal.getId();
			if (id > maxId) maxId = id;
			}
		maxId=maxId+1;
		return maxId;
		}

	@Override
	public RotacionJPA crearRotacion(Turno tur, Date dat, SeccionAlmacenJPA sec) {
		RotacionJPA nuevo =null;
		try {
			nuevo = new RotacionJPA(nuevoIdRotacion(),tur, dat);
    		entman.persist(nuevo);
    		nuevo.setSeccion(sec);
	    	entman.merge(nuevo);
		}catch(Exception e){
			return null;
		}
		return nuevo;
	}
	public int nuevoIdRotacion()throws PersistenceException {
		Integer id=0;
		@SuppressWarnings("unchecked")
		Collection<RotacionJPA> p = entman.createQuery("FROM RotacionJPA").getResultList();
		Integer maxId = 0;
		for (Iterator<RotacionJPA> iterator = p.iterator(); iterator.hasNext();) {
			RotacionJPA cal = (RotacionJPA) iterator.next();
			id= cal.getId();
			if (id > maxId) maxId = id;
			}
		maxId=maxId+1;
		return maxId;
		}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PosicionRotacionJPA> getRotacion(int id) {
		Collection<PosicionRotacionJPA> rot=null;
		try {
		rot = entman.createQuery("from PosicionRotacionJPA WHERE rotacion_id = "+ id).getResultList();
		}catch(Exception e) {
			return null;
		}
		return rot;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean eliminarRotacion(RotacionJPA rotacion) {
		Collection<PosicionRotacionJPA> rot=null;
		rot = entman.createQuery("from PosicionRotacionJPA WHERE rotacion_id = "+ rotacion.getId()).getResultList();
		Iterator <PosicionRotacionJPA> aux= rot.iterator();
		while(aux.hasNext()) {
			PosicionRotacionJPA x= aux.next();
			PosicionRotacionJPA xx= entman.find(PosicionRotacionJPA.class, x.getId());
			entman.remove(entman.merge(xx));
		}
		@SuppressWarnings("unused")
		RotacionJPA cal = entman.find(RotacionJPA.class, rotacion.getId());
		entman.remove(entman.merge(rotacion));
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<RotacionJPA> getRotacionesSeccion(SeccionAlmacenJPA sec) {
		Collection<RotacionJPA> p=null;
		try {
		p = entman.createQuery("FROM RotacionJPA WHERE seccionalmacen_id = "+sec.getId()+ " order by id Desc").getResultList();
		}catch(Exception e) {
			return null;
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<RotacionJPA> getRotacionesOperario(OperarioJPA operario) {
		Collection<RotacionJPA> p=null;
		try {
		p = entman.createQuery("FROM RotacionJPA WHERE operario_id = "+operario.getId()+ " order by id Desc").getResultList();
		}catch(Exception e) {
			return null;
		}
		return p;
	}

	@Override
	public RotacionJPA getRotacion(RotacionJPA rotacion) {
		RotacionJPA p=null;
		try {
			p = entman.find(RotacionJPA.class, rotacion.getId());
		}catch(Exception e) {
			return null;
		}	
		return p;
	}

	@Override
	public RotacionJPA getRotacionNum(int id) {
		RotacionJPA p=null;
		try {
			p = entman.find(RotacionJPA.class, id);
		}catch(Exception e) {
			return null;
		}	
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<RotacionJPA> getRotaciones() {
		Collection<RotacionJPA> p=null;
		try {
		 p= entman.createQuery("FROM RotacionJPA order by id Desc").getResultList();
		}catch(Exception e) {return null;}
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PosicionRotacionJPA> getPosicionesOperario(OperarioJPA operario) {
		Collection<PosicionRotacionJPA> rot=null;
		try {
		rot = entman.createQuery("from PosicionRotacionJPA WHERE operario_id = "+ operario.getId()+ " order by id Desc").getResultList();
		}catch(Exception e) {
			return null;
		}
		return rot;
	}
	@Override
	public PosicionRotacionJPA getPosicionOperario(OperarioJPA operario, int idRot) {
		PosicionRotacionJPA pp =entman.find(PosicionRotacionJPA.class, idRot);
		return pp;
	}
	

}
