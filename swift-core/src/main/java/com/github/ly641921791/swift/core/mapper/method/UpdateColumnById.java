package com.github.ly641921791.swift.core.mapper.method;

import com.github.ly641921791.swift.core.mapper.AbstractUpdateMethodResolver;
import com.github.ly641921791.swift.core.metadata.Table;
import com.github.ly641921791.swift.jdbc.SqlScript;
import com.github.ly641921791.swift.session.SwiftConfiguration;

/**
 * Target sql script <script>UPDATE table SET `${c}` = #{v} WHERE `id` = #{id}</script>
 *
 * @author ly
 * @since 2019-03-14 15:32
 **/
public class UpdateColumnById extends AbstractUpdateMethodResolver {

    @Override
    protected void handlerColumn(SqlScript sqlScript, Table table, SwiftConfiguration configuration) {
        sqlScript.SET_COLUMN("${c}", "#{v}");
    }

    @Override
    protected void handlerWhere(SqlScript sqlScript, Table table, SwiftConfiguration configuration) {
        sqlScript.WHERE_EQ("id", "#{id}");
        handlerDeleteColumn(sqlScript, table, configuration);
    }

}