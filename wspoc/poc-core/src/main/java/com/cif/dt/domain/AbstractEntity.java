package com.cif.dt.domain;


public abstract class AbstractEntity implements IEntity {

    public boolean isNew() {
        return (getId() == null || getId() == 0);
    }

    public boolean isPersisted() {
        return !isNew();
    }

}