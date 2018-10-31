package com.web.admin.notifier;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.admin.notifier.dto.NotifierMessage;
import com.web.admin.notifier.service.NotifierService;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import reactor.core.publisher.Mono;

public class CustomNotifier extends AbstractStatusChangeNotifier {

	private Logger logger = LoggerFactory.getLogger(CustomNotifier.class);

	private NotifierService notifierService;

	public CustomNotifier(InstanceRepository repositpry, NotifierService notifierService) {
		super(repositpry);
		this.notifierService = notifierService;
	}

	@Override
	protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
		return Mono.fromRunnable(() -> {

			String message = MessageFormat.format("Instance {0} ({1}) changed status from {2} to {3} , ServiceUrl is {4}", //
					instance.getRegistration().getName(), //
					event.getInstance(), //
					getLastStatus(event.getInstance()), //
					((InstanceStatusChangedEvent) event).getStatusInfo().getStatus(), //
					instance.getRegistration().getServiceUrl());

			logger.info(message);
			notifierService.send(new NotifierMessage(message));
		});
	}

}