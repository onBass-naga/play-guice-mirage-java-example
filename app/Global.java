import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import jp.sf.amateras.mirage.SqlManager;
import jp.sf.amateras.mirage.SqlManagerImpl;
import jp.sf.amateras.mirage.dialect.MySQLDialect;
import jp.sf.amateras.mirage.integration.spring.SpringConnectionProvider;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.db.DB;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Global extends GlobalSettings {

    @SuppressWarnings("unused")
	private static final ALogger logger = Logger.of(Application.class);

    private static Injector INJECTOR;

    @Override
    public void onStart(Application app) {
        super.onStart(app);

        // DI初期化
        List<Module> moduleList = new ArrayList<Module>();

        DataSource dataSource = DB.getDataSource();
        moduleList.add(new MirageCustomModule(dataSource));
        moduleList.add(new TransactionModule(dataSource));

        // Create Injector
        if (app.isProd())
        	INJECTOR = Guice.createInjector(Stage.PRODUCTION, moduleList.toArray(new Module[] {}));
        else
        	INJECTOR = Guice.createInjector(Stage.DEVELOPMENT, moduleList.toArray(new Module[] {}));
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        A controller = INJECTOR.getInstance(controllerClass);
        return controller;
    }


    protected static class MirageCustomModule extends AbstractModule {

        protected SqlManager sqlManager;

        public MirageCustomModule(DataSource dataSource) {
            if (dataSource == null) {
                String msg = "The argument 'dataSource' should not be null!";
                throw new IllegalArgumentException(msg);
            }

            DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
            SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
            springConnectionProvider.setTransactionManager(transactionManager);
            SqlManager sqlManager = new SqlManagerImpl();
            sqlManager.setConnectionProvider(springConnectionProvider);
            sqlManager.setDialect(new MySQLDialect());
            this.sqlManager = sqlManager;
        }

        @Override
        protected void configure() {
        }

        @Provides
        @Singleton
        public SqlManager getSqlManager(){
            return this.sqlManager;
        }
    }

    /**
     * トランザクション制御用のGuiceモジュール
     */
    protected static class TransactionModule extends AbstractModule {

        protected DataSource dataSource;

        public TransactionModule(DataSource dataSource) {
            if (dataSource == null) {
                String msg = "The argument 'dataSource' should not be null!";
                throw new IllegalArgumentException(msg);
            }
            this.dataSource = dataSource;
        }

        @Override
        protected void configure() {
            // TransactionManager
            PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

            // TransactionInterceptor
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager, new AnnotationTransactionAttributeSource(false));

            bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionInterceptor);

            bind(PlatformTransactionManager.class).toInstance(transactionManager);
        }
    }
}
