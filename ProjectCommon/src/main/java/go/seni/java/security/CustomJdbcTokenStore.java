package go.seni.java.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


public class CustomJdbcTokenStore extends JdbcTokenStore {

    private final JdbcTemplate jdbcTemplate;

    public CustomJdbcTokenStore(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
