package com.github.ly641921791.swift.core.mapper.method;

import com.github.ly641921791.swift.core.mapper.AbstractDeleteMethodResolver;
import com.github.ly641921791.swift.core.metadata.Table;
import com.github.ly641921791.swift.jdbc.SqlScript;
import com.github.ly641921791.swift.session.SwiftConfiguration;

/**
 * Target sql script ： <script>DELETE FROM table WHERE id = #{id}</script>
 * Target sql script ： <script>UPDATE table SET column = 1 WHERE id = #{id}</script>
 *
 * @author ly
 * @since 2019-03-01 09:29
 **/
public class DeleteById extends AbstractDeleteMethodResolver {

    @Override
    protected void handlerWhere(SqlScript sqlScript, Table table, SwiftConfiguration configuration) {
        sqlScript.WHERE(String.format("`%s` = #{%s}", table.getKeyColumn(), P_A_ID));
    }

}
