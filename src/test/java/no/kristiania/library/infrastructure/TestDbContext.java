package no.kristiania.library.infrastructure;

import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(TestDbContext.Extension.class)
public @interface TestDbContext {
    class Extension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {
        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == DbContext.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            var dbContext = new DbContext();
            extensionContext.getStore(GLOBAL).put(DbContext.class, dbContext);
            return dbContext;
        }


        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            context.getStore(GLOBAL)
                    .get(DbContextConnection.class, DbContextConnection.class)
                    .close();
        }

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            var connection = context.getStore(GLOBAL)
                    .get(DbContext.class, DbContext.class)
                    .startConnection(getDataSource());
            context.getStore(GLOBAL).put(DbContextConnection.class, connection);
        }

        private DataSource getDataSource() {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:test");
            return dataSource;
        }
    }
}
