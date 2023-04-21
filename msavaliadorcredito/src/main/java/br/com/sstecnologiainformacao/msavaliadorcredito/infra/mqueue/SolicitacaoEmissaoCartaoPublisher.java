package br.com.sstecnologiainformacao.msavaliadorcredito.infra.mqueue;

import br.com.sstecnologiainformacao.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissoaCartoes;

    public void solicitarCartao(final DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        final String json = this.convertToJson(dados);
        rabbitTemplate.convertAndSend(queueEmissoaCartoes.getName(), json);
    }

    private String convertToJson(final DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dados);
    }
}
