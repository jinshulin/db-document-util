package com.jd.ips.repository;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.jd.ips.entity.TableColumnDesc;
import com.jd.ips.entity.TableDesc;
import com.jd.ips.utils.PatternPredicate;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * @description  
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月15日 18时18分
 */
@Repository
public class SchemaRepository {
    private static final String QUERY_TABLE_SCHEMA_SQL =
        "select TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_SCHEMA='%s';";

    private static final String QUERY_TABLE_COLUMN_DETAILS_SQL =
        "select COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,IS_NULLABLE,COLUMN_COMMENT from information_schema.COLUMNS where TABLE_NAME='%s' ;";

    @Value("${db.schema}")
    private String dbSchema;
    @Value("${table.pattern}")
    private String tablePatterns;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TableDesc> queryTables() {
        String sql = String.format(QUERY_TABLE_SCHEMA_SQL, dbSchema);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        List<TableDesc> result = Lists.newArrayList();
        List<PatternPredicate> predicates = getPatterns();
        mapList.forEach(map -> {
            String tableName = map.get("TABLE_NAME").toString();
            boolean match = false;
            for (PatternPredicate predicate : predicates) {
                if (predicate.test(tableName)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return;
            }
            TableDesc table = new TableDesc(map.get("TABLE_NAME").toString(), (String)map.get("TABLE_COMMENT"));
            loadTableColumns(table);
            result.add(table);
        });
        return result;
    }

    private void loadTableColumns(TableDesc table) {
        String tableName = table.getName();
        String sql = String.format(QUERY_TABLE_COLUMN_DETAILS_SQL, tableName);
        List<Map<String, Object>> columnMapList = jdbcTemplate.queryForList(sql);
        List<TableColumnDesc> list = Lists.newArrayListWithCapacity(columnMapList.size());
        columnMapList.forEach(column -> {
            list.add(new TableColumnDesc(
                column.get("COLUMN_NAME").toString(),
                (String)column.get("DATA_TYPE"),
                object2String(column.get("CHARACTER_MAXIMUM_LENGTH")),
                (String) column.get("IS_NULLABLE"),
                (String) column.get("COLUMN_COMMENT")
            ));
        });
        table.setTableColumnDesc(list);
    }

    private List<PatternPredicate> getPatterns() {
        List<PatternPredicate> result = Lists.newArrayList();
        for (String tablePattern : tablePatterns.split(",")) {
            if (StringUtils.isBlank(tablePattern)) {
                continue;
            }
            result.add(new PatternPredicate(tablePattern.trim()));
        }
        return result;
    }

    private String object2String(Object o) {
        if (Objects.isNull(o)) {
            return null;
        }
        return o.toString();
    }

}
