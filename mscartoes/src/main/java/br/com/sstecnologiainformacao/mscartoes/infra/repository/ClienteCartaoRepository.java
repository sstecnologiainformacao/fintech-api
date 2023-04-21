package br.com.sstecnologiainformacao.mscartoes.infra.repository;

import br.com.sstecnologiainformacao.mscartoes.domain.ClienteCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteCartaoRepository extends JpaRepository<ClienteCartao, Long> {

    List<ClienteCartao> findByCpf(final String cpf);
}
