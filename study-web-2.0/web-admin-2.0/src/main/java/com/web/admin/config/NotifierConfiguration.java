package com.web.admin.config;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import com.web.admin.notifier.CustomNotifier;
import com.web.admin.notifier.service.NotifierService;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.CompositeNotifier;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.boot.admin.server.notify.RemindingNotifier;
import de.codecentric.boot.admin.server.notify.filter.FilteringNotifier;

@Configuration
public class NotifierConfiguration {
	
	// 自定义通知方式
	@Bean
	@ConfigurationProperties("spring.boot.admin.notify.custom")
	public CustomNotifier customNotifier(InstanceRepository repository,@Lazy NotifierService notifierService) {
		return new CustomNotifier(repository,notifierService);
	}
	
	@Configuration
	public static class NotifierConfig {
		private final InstanceRepository repository;
		private final ObjectProvider<List<Notifier>> otherNotifiers;

		public NotifierConfig(InstanceRepository repository, ObjectProvider<List<Notifier>> otherNotifiers) {
			this.repository = repository;
			this.otherNotifiers = otherNotifiers;
		}

		@Bean
		public FilteringNotifier filteringNotifier() {
			CompositeNotifier delegate = new CompositeNotifier(otherNotifiers.getIfAvailable(Collections::emptyList));
			return new FilteringNotifier(delegate, repository);
		}

		@Primary
		@Bean(initMethod = "start", destroyMethod = "stop")
		public RemindingNotifier remindingNotifier(FilteringNotifier filteringNotifier) {
			RemindingNotifier notifier = new RemindingNotifier(filteringNotifier, repository);
			notifier.setReminderPeriod(Duration.ofMinutes(10));
			notifier.setCheckReminderInverval(Duration.ofSeconds(10));
			return notifier;
		}
	}
}