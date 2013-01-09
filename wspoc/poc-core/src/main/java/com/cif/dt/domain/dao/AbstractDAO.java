package com.cif.dt.domain.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.cif.dt.domain.AbstractEntity;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public abstract class AbstractDAO<EntityClass extends AbstractEntity>
		implements DAO<EntityClass> {

	@Inject
	protected EntityManager em;

	//CRUD
	@Transactional
	public void insert(final EntityClass toPersist) {
		if (toPersist.isNew()) {
			this.em.persist(toPersist);
		} else {
			update(toPersist);
		}
	}
	
	@Transactional
	public void update(final EntityClass toUpdate) {
		this.em.persist(this.em.merge(toUpdate));
	}

	@Transactional
	public void delete(final EntityClass toDelete) {
		this.em.remove(this.em.merge(toDelete));
	}
	
	@Transactional
	public void refresh( final EntityClass toRefresh ) {
		this.em.refresh( this.em.merge( toRefresh ) );
	}
	
	@Transactional
	public EntityClass merge(EntityClass entity) {
		return em.merge(entity);
	}

	public EntityClass findById(Long id) {
		return em.find(getClazz(), id);
	}
	
	// ///////////////

	@SuppressWarnings("unchecked")
	@Transactional
	public List<EntityClass> findAll() {
		Query query = getFindAllQuery();
		return query.getResultList();
	}

	@Transactional
	protected Query getFindAllQuery() {
		return em.createQuery(getBaseQueryBuilder().toString());
	}

	private StringBuilder getBaseQueryBuilder() {
		StringBuilder queryBuilder = new StringBuilder("select entity from ");
		queryBuilder.append(getClazz().getName());
		queryBuilder.append(" entity");
		return queryBuilder;
	}

	public EntityClass getReference(EntityClass entity) {
		if (entity.isNew()) {
			return entity;
		} else {
			return em.getReference(getClazz(), entity.getId());
		}
	}

	@Transactional
	public long count() {
		StringBuilder query = new StringBuilder("select count(entity) from ");
		query.append(getClazz().getName());
		query.append(" entity");
		Long result = (Long) em.createQuery(query.toString())
				.getSingleResult();
		return result.longValue();
	}

}