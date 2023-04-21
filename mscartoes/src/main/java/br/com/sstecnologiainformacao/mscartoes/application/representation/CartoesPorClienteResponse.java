package br.com.sstecnologiainformacao.mscartoes.application.representation;

import br.com.sstecnologiainformacao.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {
    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromModel(final ClienteCartao model) {
        return new CartoesPorClienteResponse(
                model.getCartao().getName(),
                model.getCartao().getBandeira().toString(),
                model.getLimite()
        );
    }

}
