package br.com.palo.ti.candidate.consumer;

import br.com.palo.ti.candidate.dtos.statusDto;
import br.com.palo.ti.candidate.services.candidateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class proposalConsumer {

    @Autowired
    private candidateService service;

    @Value("${broker.queue.proposal.status}")
    private String queue;

    @RabbitListener(queues = "${broker.queue.proposal.status}")
    public void proposalConsumer(@Payload statusDto statusDto) {
        service.updateStatusToCandidate(statusDto);
    }
}