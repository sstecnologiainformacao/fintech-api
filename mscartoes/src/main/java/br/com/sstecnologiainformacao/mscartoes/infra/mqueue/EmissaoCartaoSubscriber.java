package br.com.sstecnologiainformacao.mscartoes.infra.mqueue;

import br.com.sstecnologiainformacao.mscartoes.domain.Cartao;
import br.com.sstecnologiainformacao.mscartoes.domain.ClienteCartao;
import br.com.sstecnologiainformacao.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import br.com.sstecnologiainformacao.mscartoes.infra.repository.CartaoRepository;
import br.com.sstecnologiainformacao.mscartoes.infra.repository.ClienteCartaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload final String payload) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            final Cartao cartao = this.cartaoRepository.findById(dados.getIdCartao()).orElseThrow();
            final ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());
            clienteCartaoRepository.save(clienteCartao);
        } catch (final Throwable e) {
            log.error("Falha ao receber a requisição de um cartão", e);
        }

    }
}