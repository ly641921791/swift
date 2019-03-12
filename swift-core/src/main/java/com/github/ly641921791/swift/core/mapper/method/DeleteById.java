package com.github.ly641921791.swift.core.mapper.method;

import com.github.ly641921791.swift.core.mapper.AbstractDeleteMethodResolver;
import com.github.ly641921791.swift.core.metadata.Table;
import com.github.ly641921791.swift.core.util.StringUtils;
import com.github.ly641921791.swift.session.SwiftConfiguration;

/**
 * @author ly
 * @since 2019-03-01 09:29
 **/
public class DeleteById extends AbstractDeleteMethodResolver {

    private static final String DELETE_BY_ID = "<script>DELETE FROM %s WHERE %s = #{%s}</script>";

    private static final String UPDATE_BY_ID = "<script>UPDATE %s SET %s = 0 WHERE %s = #{%s}</script>";

    @Override
    public String buildSqlScript(Table table, SwiftConfiguration configuration) {
        if (StringUtils.isEmpty(table.getDeleteColumn())) {
            return String.format(DELETE_BY_ID, table.getName(), Table.DEFAULT_KEY_COLUMN, Table.DEFAULT_KEY_COLUMN);
        }
        return String.format(UPDATE_BY_ID, table.getName(), table.getDeleteColumn(), Table.DEFAULT_KEY_COLUMN, Table.DEFAULT_KEY_COLUMN);
    }

}