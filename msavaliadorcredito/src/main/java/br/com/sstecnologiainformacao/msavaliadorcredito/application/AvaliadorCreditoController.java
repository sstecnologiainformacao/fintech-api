package br.com.sstecnologiainformacao.msavaliadorcredito.application;

import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.ErroComunicacaoMicroserviceException;
import br.com.sstecnologiainformacao.msavaliadorcredito.application.exception.ErrorSolicitacaoCartaoException;
import br.com.sstecnologiainformacao.msavaliadorcredito.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "/situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") final String cpf) {
        final SituacaoCliente situacao;
        try {
            situacao = this.avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacao);
        } catch (DadosClienteNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroserviceException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).build();
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao){
        final SituacaoCliente situacao;
        try {
            final RetornoAvaliacaoCliente retorno = this.avaliadorCreditoService.realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
            return ResponseEntity.ok(retorno);
        } catch (DadosClienteNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroserviceException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).build();
        }
    }

    @PostMapping("/solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try {
            final ProtocoloSolicitacaoCartao protocolo = this.avaliadorCreditoService.solicitarCriacaoCartao(dados);
            return ResponseEntity.ok(protocolo);
        } catch (final ErrorSolicitacaoCartaoException error) {
            return ResponseEntity.internalServerError().body(error.getMessage());
        }
    }
}
