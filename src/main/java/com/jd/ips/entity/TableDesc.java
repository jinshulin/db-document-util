package com.jd.ips.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description 表信息封装.
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月15日 18时20分
 */
@Getter
@Setter
@ToString
public class TableDesc {

    /**
     * 表名称.
     */
    private String name;
    /**
     * 表类型.
     */
    private String desc;

    /**
     * 表所属列信息封装.
     */
    private List<TableColumnDesc> tableColumnDesc;

    public TableDesc(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
