package br.com.concrete.desafio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.concrete.desafio.dao.PhoneRepository;
import br.com.concrete.desafio.entity.Phone;
import br.com.concrete.desafio.entity.User;

@Component
public class PhoneService {
	final Integer TAMANHO_DDD = 2;
	final Integer TAMANHO_MAXIMO_TELEFONE = 9;
	final Integer TAMANHO_MINIMO_TELEFONE = 8;

	@Autowired
	private PhoneRepository phoneRepository;

	public List<Phone> buscarTodos() {
		return (List<Phone>) phoneRepository.findAll();
	}
	
	public Boolean isTelefoneValido(final User user) {

		for (Phone telefone : user.getPhones()) {
			final Integer tamanhoDDD = telefone.getDdd().toString().length();
			final Integer tamanhoTelefone = telefone.getNumber().toString().length();
			if (!tamanhoDDD.equals(TAMANHO_DDD)) {
				return Boolean.FALSE;
			} else if (tamanhoTelefone > TAMANHO_MAXIMO_TELEFONE || tamanhoTelefone < TAMANHO_MINIMO_TELEFONE) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

}
