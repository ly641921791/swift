package com.github.ly641921791.swift.spring;

import com.github.ly641921791.swift.core.mapper.BaseMapper;
import com.github.ly641921791.swift.core.mapper.method.Save;
import com.github.ly641921791.swift.core.mapper.param.Condition;
import com.github.ly641921791.swift.core.util.ClassUtils;
import com.github.ly641921791.swift.core.util.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base Service Implement
 *
 * @param <T> Table Class
 * @param <M> Mapper Class
 * @author ly
 * @since 2019-01-24 15:45
 */
public class BaseService<T, M extends BaseMapper<T, ID>, ID> implements IService<T, ID> {

    @Autowired
    protected M mapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public long count(Condition c) {
        return mapper.count(c);
    }

    @Override
    public int save(T entity, boolean ignore) {
        try {
            return mapper.save(entity, ignore);
        } catch (DuplicateKeyException e) {
            if (ignore) {
                return 0;
            }
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAll(Collection<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        Class mapperInterface = (Class) ClassUtils.getSuperclassGenericType(getClass())[1];
        String sqlStatement = mapperInterface.getName() + "." + StringUtils.toLowerCamel(Save.class.getSimpleName());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            entities.forEach(e -> {
                MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
                paramMap.put("param1", e);
                paramMap.put("entity", e);
                paramMap.put("param2", e);
                paramMap.put("ignore", false);
                sqlSession.insert(sqlStatement, paramMap);
            });
            sqlSession.flushStatements();
        }
        // TODO 处理结果，不一定插入数量等于参数数量
        return entities.size();
    }

    @Override
    public int delete(Condition c) {
        return mapper.delete(c);
    }

    @Override
    public int deleteById(ID id) {
        if (id == null) {
            return 0;
        }
        return mapper.deleteById(id);
    }

    @Override
    public int deleteByColumn(String column, Object value) {
        return mapper.deleteByColumn(column, value);
    }

    @Override
    public int update(T p, Condition c) {
        return mapper.update(p, c);
    }

    @Override
    public int updateByColumn(T p, String column, Object value) {
        return mapper.updateByColumn(p, column, value);
    }

    @Override
    public int updateById(T p, ID id) {
        if (id == null) {
            return 0;
        }
        return mapper.updateById(p, id);
    }

    @Override
    public int updateColumnById(String column, Object value, ID id) {
        return mapper.updateColumnById(column, value, id);
    }

    @Override
    public List<T> findAll(Condition c) {
        return mapper.findAll(c);
    }

    @Override
    public List<T> findAllById(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return mapper.findAllById(ids);
    }

    @Override
    public T findById(ID id) {
        if (id == null) {
            return null;
        }
        return mapper.findById(id);
    }

    @Override
    public T findOneByColumn(String column, Object value) {
        return mapper.findOneByColumn(column, value);
    }

    @Override
    public List<T> findAllByColumn(String column, Object value) {
        return mapper.findAllByColumn(column, value);
    }

}
