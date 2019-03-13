package com.github.ly641921791.swift.spring;

import com.github.ly641921791.swift.core.mapper.BaseMethod;

import java.util.List;

/**
 * Base Service Interface
 *
 * @param <T> Table Class
 * @author ly
 * @since 2019-01-24 15:44
 */
public interface IService<T, ID> extends BaseMethod<T, ID> {

    /**
     * Save all records
     *
     * @param entities records
     * @return null
     */
    List<T> saveAll(Iterable<T> entities);

}
