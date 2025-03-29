package team.project.module.club.report.internal.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@MappedTypes(Map.class)          // 处理的Java类型
@MappedJdbcTypes(JdbcType.VARCHAR) // 处理的JDBC类型
public class JsonTypeHandler extends BaseTypeHandler<Map<String, Map<String, String>>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Map<String, Map<String, String>> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("JSON序列化失败", e);
        }
    }

    @Override
    public Map<String, Map<String, String>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public Map<String, Map<String, String>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public Map<String, Map<String, String>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private Map<String, Map<String, String>> parseJson(String json) {
        try {
            if (json == null || json.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(json,
                    new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败: " + json, e);
        }
    }
}

