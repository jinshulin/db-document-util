package com.jd.ips.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description 列详细信息.
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月15日 18时21分
 */
@Getter
@Setter
@ToString
public class TableColumnDesc {

    /**
     * 列名称.
     */
    private String name;
    /**
     * 列数据类型.
     */
    private String dataType;
    /**
     * 列数据长度.
     */
    private String dataLen;
    /**
     * 是否可为空，YES/NO.
     */
    private String nullable;
    /**
     * 列描述信息.
     */
    private String desc;

    public TableColumnDesc(String name, String dataType, String dataLen, String nullable,
        String desc) {
        this.name = name;
        this.dataType = dataType;
        this.dataLen = dataLen;
        this.nullable = nullable;
        this.desc = desc;
    }
}
