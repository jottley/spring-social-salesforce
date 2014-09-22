package org.springframework.boot.autoconfigure.social;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.impl.SalesforceTemplate;
import org.springframework.social.salesforce.connect.SalesforceConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * Configures Salesforce for Spring Boot.
 * <p>
 * Based on {@link FacebookAutoConfiguration}.
 * </p>
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, SalesforceConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.salesforce.", value = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class SalesforceAutoConfiguration {

	@Configuration
	@EnableSocial
	@ConditionalOnWebApplication
	protected static class SalesforceAutoConfigurationAdapter extends SocialAutoConfigurerAdapter {

		@Override
		protected String getPropertyPrefix() {
			return "spring.social.salesforce.";
		}

		@Override
		protected ConnectionFactory<?> createConnectionFactory(final RelaxedPropertyResolver properties) {
			return new SalesforceConnectionFactory(properties.getRequiredProperty("app-id"), properties.getRequiredProperty("app-secret"));
		}

		@Bean
		@ConditionalOnMissingBean(Salesforce.class)
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		public Salesforce salesforce(final ConnectionRepository repository) {
			final Connection<Salesforce> connection = repository.findPrimaryConnection(Salesforce.class);
			return connection != null ? connection.getApi() : new SalesforceTemplate();
		}

		@Bean(name = { "connect/salesforceConnect", "connect/salesforceConnected" })
		@ConditionalOnProperty(prefix = "spring.social.", value = "auto-connection-views")
		public View salesforceConnectView() {
			return new GenericConnectionStatusView("salesforce", "Salesforce");
		}

	}

}
