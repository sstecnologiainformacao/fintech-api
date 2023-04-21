package br.com.sstecnologiainformacao.msavaliadorcredito.infra;

import br.com.sstecnologiainformacao.msavaliadorcredito.domain.model.Cartao;
import br.com.sstecnologiainformacao.msavaliadorcredito.domain.model.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "cartoes")
public interface CartoesResourceCliente {

    @GetMapping(params = "cpf")
    ResponseEntity<List<CartaoCliente>> getCartoesByCliente(@RequestParam("cpf") final String cpf);

    @GetMapping(params = "renda")
    ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") final Long renda);
}
