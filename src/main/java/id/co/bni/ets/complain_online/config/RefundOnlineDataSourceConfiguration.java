package id.co.bni.ets.complain_online.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "id.co.bni.ets.complain_online.repository",
        entityManagerFactoryRef = "refundOnlineManagerFactory",
        transactionManagerRef = "refundOnlineTransactionManager")
public class RefundOnlineDataSourceConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource refundOnlineDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean refundOnlineManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(refundOnlineDataSource())
                .packages("id.co.bni.ets.complain_online.model")
                .persistenceUnit("complain-online")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager refundOnlineTransactionManager(
            @Qualifier("refundOnlineManagerFactory") EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
