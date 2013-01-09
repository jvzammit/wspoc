package com.cif.dt.domain.dao;

import java.util.List;

import com.cif.dt.domain.IEntity;

public interface DAO<EntityClass extends IEntity> {

	void insert(final EntityClass toPersist);

	EntityClass findById(Long id);

	void update(final EntityClass toUpdate);

	void delete(final EntityClass toDelete);

	void refresh(final EntityClass toRefresh);

	EntityClass merge(EntityClass entity);

	List<EntityClass> findAll();

	long count();

	Class<EntityClass> getClazz();

	EntityClass getReference(EntityClass entity);
}