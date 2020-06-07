package com.bil.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.sql.*;

/**
 * org.joda.time.LocalTime类型处理器
 *
 * @author haibo.yang
 * @since 31 五月 2018
 */
@MappedTypes({LocalTime.class})
public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime time, JdbcType jdbcType) throws SQLException {
        DateTime datetime = new DateTime(1970, 1, 1, time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), 0);
        ps.setTime(i, new Time(datetime.getMillis()));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Time time = rs.getTime(columnName);
        return time != null ? new LocalTime(time.getTime()) : null;
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Time time = rs.getTime(columnIndex);
        return time != null ? new LocalTime(time.getTime()) : null;
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Time time = cs.getTime(columnIndex);
        return time != null ? new LocalTime(time.getTime()) : null;
    }
}
