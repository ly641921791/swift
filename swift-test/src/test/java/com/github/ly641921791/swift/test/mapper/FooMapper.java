package com.github.ly641921791.swift.test.mapper;

import com.github.ly641921791.swift.core.mapper.BaseMapper;
import com.github.ly641921791.swift.test.table.Foo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ly
 * @since 2019-03-13 09:29
 **/
@Mapper
public interface FooMapper extends BaseMapper<Foo, Long> {
}
