package br.com.sstecnologiainformacao.mscartoes.application;

import br.com.sstecnologiainformacao.mscartoes.domain.ClienteCartao;
import br.com.sstecnologiainformacao.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listCartoesByCpf(final String cpf) {
        return this.repository.findByCpf(cpf);
    }
}
