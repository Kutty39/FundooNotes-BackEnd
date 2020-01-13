package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.service.CustomMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class CustomMapperImpl<T, U, E, R> implements CustomMapper<T, U, E, R> {
    private R repo;

    @Override
    public List<E> getListVal(List<T> list, E entity, R repo) {
        this.repo = repo;
        return list.stream().map(this::getEntity).collect(Collectors.toList());
    }

    public E getEntity(T val) {
        try {
            Class<?> c = repo.getClass();
            List<Method> methodList = Arrays.stream(c.getDeclaredMethods()).filter(method -> method.getName().contains("findByUniqKey")).collect(Collectors.toList());
            return (E) methodList.get(0).invoke(repo, val);
        } catch (IllegalAccessException | InvocationTargetException  e) {
            e.printStackTrace();
        }
        return null;
    }

}
