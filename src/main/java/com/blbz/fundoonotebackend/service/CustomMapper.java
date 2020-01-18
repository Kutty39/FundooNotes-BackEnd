package com.blbz.fundoonotebackend.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomMapper<T,U,E,R> {
    List<E> getListVal(List<T> list,E entity,R repo);
}
