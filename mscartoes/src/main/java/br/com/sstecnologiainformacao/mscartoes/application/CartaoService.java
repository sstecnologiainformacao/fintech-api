package br.com.sstecnologiainformacao.mscartoes.application;

import br.com.sstecnologiainformacao.mscartoes.domain.Cartao;
import br.com.sstecnologiainformacao.mscartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository repository;

    @Transactional
    public Cartao save(final Cartao cartao) {
        return this.repository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(final Long renda) {
        final BigDecimal rendaBigDecimal = BigDecimal.valueOf(renda);
        return this.repository.findByRendaLessThanEqual(rendaBigDecimal);
    }

}
