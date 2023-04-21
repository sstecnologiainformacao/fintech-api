package br.com.sstecnologiainformacao.mscartoes.application;

import br.com.sstecnologiainformacao.mscartoes.application.representation.CartaoSaveRequest;
import br.com.sstecnologiainformacao.mscartoes.application.representation.CartoesPorClienteResponse;
import br.com.sstecnologiainformacao.mscartoes.domain.Cartao;
import br.com.sstecnologiainformacao.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService service;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        final Cartao cartao = request.toModel();
        service.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") final Long renda) {
        final List<Cartao> cartoes = this.service.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") final String cpf) {
        final List<ClienteCartao> cartoes = this.clienteCartaoService.listCartoesByCpf(cpf);
        final List<CartoesPorClienteResponse> result = cartoes
                .stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
