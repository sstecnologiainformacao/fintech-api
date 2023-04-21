package br.com.sstecnologiainformacao.msavaliadorcredito.application;

import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.ErroComunicacaoMicroserviceException;
import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.ErrorSolicitacaoCartaoException;
import br.com.sstecnologiainformacao.msavaliadorcredito.domain.model.*;
import br.com.sstecnologiainformacao.msavaliadorcredito.infra.CartoesResourceCliente;
import br.com.sstecnologiainformacao.msavaliadorcredito.infra.ClienteResourceClient;
import br.com.sstecnologiainformacao.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clients;
    private final CartoesResourceCliente cartoes;

    private final SolicitacaoEmissaoCartaoPublisher publisher;

    public SituacaoCliente obterSituacaoCliente(final String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {
        try {
            final ResponseEntity<DadosCliente> response = this.clients.dadosCliente(cpf);
            final ResponseEntity<List<CartaoCliente>> cartoes = this.cartoes.getCartoesByCliente(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(response.getBody())
                    .cartoes(cartoes.getBody())
                    .build();
        } catch (final FeignException.FeignClientException exception) {
            int status = exception.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(exception.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(final String cpf, final Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {
        try {
            final ResponseEntity<DadosCliente> response = this.clients.dadosCliente(cpf);
            final ResponseEntity<List<Cartao>> cartoesRendaAte = cartoes.getCartoesRendaAte(renda);

            final List<Cartao> cartoes = cartoesRendaAte.getBody();
            final List<CartaoAprovado> aprovados = cartoes.stream().map((cartao) -> {
               final DadosCliente dadosCliente = response.getBody();

               final BigDecimal limiteBasico = cartao.getLimiteBasico();
               final BigDecimal idade = BigDecimal.valueOf(dadosCliente.getIdade());
               final BigDecimal fator = idade.divide(BigDecimal.valueOf(10));
               final BigDecimal limiteAprovado = fator.multiply(limiteBasico);

               final CartaoAprovado aprovado = new CartaoAprovado();
               aprovado.setCartao(cartao.getName());
               aprovado.setBandeira(cartao.getBandeira());
               aprovado.setLimiteAprovado(limiteAprovado);

               return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(aprovados);

        } catch (final FeignException.FeignClientException exception) {
            int status = exception.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(exception.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarCriacaoCartao(final DadosSolicitacaoEmissaoCartao dados) {
        try {
            this.publisher.solicitarCartao(dados);
            final String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Throwable error) {
            throw new ErrorSolicitacaoCartaoException(error.getMessage());
        }
    }

}
