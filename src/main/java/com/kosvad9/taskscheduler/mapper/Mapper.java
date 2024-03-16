package com.kosvad9.taskscheduler.mapper;

public interface Mapper<D,E> {
    //D-DTO
    //E-Entity
    public D mapToDto(E entity);
    public E mapToEntity(D dto);
}
