package br.com.sstecnologiainformacao.msavaliadorcredito.application.exception;

import lombok.Getter;

public class ErroComunicacaoMicroserviceException extends Exception {

    @Getter
    private Integer status;

    public ErroComunicacaoMicroserviceException(final String mensagem, final Integer status) {
        super(mensagem);
        this.status = status;
    }
}
