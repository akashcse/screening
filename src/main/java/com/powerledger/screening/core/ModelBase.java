package com.powerledger.screening.core;

public abstract class ModelBase<T> {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}

