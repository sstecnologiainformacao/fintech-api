package br.com.sstecnologiainformacao.msclientes.application;

import br.com.sstecnologiainformacao.msclientes.domain.Cliente;
import br.com.sstecnologiainformacao.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente save(final Cliente cliente) {
        return this.repository.save(cliente);
    }

    public Optional<Cliente> getByCPF(final String cpf) {
        return this.repository.findByCpf(cpf);
    }
}
