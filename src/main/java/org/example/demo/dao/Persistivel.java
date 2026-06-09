package org.example.demo.dao;

import java.util.List;

public interface Persistivel<T> {
    boolean inserir(T entidade);
    T buscarPorId(int id);
    boolean atualizar(T entidade);
}