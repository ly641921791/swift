package com.github.ly641921791.swift.core.mapper.method;

import com.github.ly641921791.swift.core.mapper.AbstractSelectMethodResolver;
import com.github.ly641921791.swift.core.metadata.Table;
import com.github.ly641921791.swift.core.util.StringUtils;
import com.github.ly641921791.swift.session.SwiftConfiguration;

/**
 * @author ly
 * @since 2019-03-11 16:35
 **/
public class FindAllById extends AbstractSelectMethodResolver {

    private static final String SELECT = "<script>SELECT %s FROM %s WHERE id IN <foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>#{item}</foreach></script>";

    @Override
    protected String doBuildSqlScript(Table table, SwiftConfiguration configuration) {
        return String.format(SELECT, columns(table), table.getName());
    }

    @Override
    protected String afterBuildSqlScript(String script, Table table, SwiftConfiguration configuration) {
        if (StringUtils.isEmpty(table.getDeleteColumn())) {
            return script;
        }
        return script.replace("</script>", " AND " + table.getDeleteColumn() + " = 1</script>");
    }

}
