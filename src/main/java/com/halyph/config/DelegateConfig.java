package com.halyph.config;

import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.RuleRegistry;
import org.hdiv.config.annotation.ValidationConfigurer;
import org.springframework.context.annotation.Configuration;

import com.hdivsecurity.services.config.EnableHdiv4ServicesSecurityConfiguration;
import com.hdivsecurity.services.config.HdivServicesSecurityConfigurerAdapter;
import com.hdivsecurity.services.config.ServicesConfig.IdProtectionType;
import com.hdivsecurity.services.config.ServicesSecurityConfigBuilder;

@Configuration
@EnableHdiv4ServicesSecurityConfiguration
public class DelegateConfig extends HdivServicesSecurityConfigurerAdapter {

	@Override
	public void configure(final ServicesSecurityConfigBuilder builder) {
		builder.confidentiality(false).sessionExpired().homePage("/");
		builder.showErrorPageOnEditableValidation(true).debugMode(false);

		builder.createARegionPerControllerMapping(false);
		builder.reuseExistingPageInAjaxRequest(!Boolean.getBoolean("hdiv.dont.reuse"));
		builder.baseAPIPath("/api").idProtection(IdProtectionType.PLAINTEXT_HID);

		builder.hypermediaSupport(false).csrfHeader(false);
	}

	@Override
	public void addExclusions(final ExclusionRegistry registry) {
		registry.addUrlExclusions("/", "/metrics.*", "/scripts/.*", "/bootstrap/.*", "/images/.*", "/fonts/.*", "/angular-ui-router/.*",
				"/angular/.*", "/angular-cookies/.*", "/jquery/.*", "/css/.*", "/favicon.ico");
	}

	@Override
	public void addRules(final RuleRegistry registry) {
		registry.addRule("safeText").acceptedPattern("^[a-zA-Z0-9 :@.\\-_+#]*$").rejectedPattern("(\\s|\\S)*(--)(\\s|\\S)*]");
		registry.addRule("numbers").acceptedPattern("^[1-9]\\d*$");
	}

	@Override
	public void configureEditableValidation(final ValidationConfigurer validationConfigurer) {
		validationConfigurer.addValidation("/.*").forParameters("amount").rules("numbers").disableDefaults();
		validationConfigurer.addValidation("/.*").rules("safeText").disableDefaults();
	}

}
