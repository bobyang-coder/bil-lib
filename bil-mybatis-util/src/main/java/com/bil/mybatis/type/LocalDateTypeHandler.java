package com.bil.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalDate;

import java.sql.*;

/**
 * org.joda.time.LocalDate类型处理器
 *
 * @author haibo.yang
 * @since 31 五月 2018
 */
@MappedTypes({LocalDate.class})
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, new Date(parameter.toDateTimeAtStartOfDay().toDate().getTime()));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return date != null ? new LocalDate(date.getTime()) : null;
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return date != null ? new LocalDate(date.getTime()) : null;
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        return date != null ? new LocalDate(date.getTime()) : null;
    }
}
