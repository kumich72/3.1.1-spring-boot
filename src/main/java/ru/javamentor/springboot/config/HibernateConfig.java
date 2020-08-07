package ru.javamentor.springboot.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;


@Configuration
public class HibernateConfig {
    @Autowired
    private Environment env;

    private org.hibernate.cfg.Configuration getConfiguration() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Role.class);

//        dataSource.setDriverClassName(env.getProperty("datasource.driver"));
//        dataSource.setUsername(env.getProperty("datasource.username"));
//        dataSource.setPassword(env.getProperty("datasource.password"));
//        dataSource.setUrl( env.getProperty("datasource.url"));

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false");

        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", "true");
//            configuration.setProperty("hibernate.hbm2ddl.auto", "create");
//        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        return configuration;
    }

//    @Bean(name = "getSessionHibernateFactory")
//    public SessionFactory getSessionHibernateFactory() {
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
//        org.hibernate.cfg.Configuration configuration = getConfiguration();
//        builder.applySettings(configuration.getProperties());
//        ServiceRegistry serviceRegistry = builder.build();
//        return configuration.buildSessionFactory(serviceRegistry);
//    }
}