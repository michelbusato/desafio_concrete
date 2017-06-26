package br.com.concrete.desafio.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.concrete.desafio.dto.JwtUserDTO;
import br.com.concrete.desafio.dto.LoginDTO;
import br.com.concrete.desafio.dto.MensagemErroDTO;
import br.com.concrete.desafio.entity.User;
import br.com.concrete.desafio.security.MD5Generator;
import br.com.concrete.desafio.service.JwtService;
import br.com.concrete.desafio.service.PhoneService;
import br.com.concrete.desafio.service.UserService;
import br.com.concrete.desafio.util.DateUtil;

@RestController
public class UserController {
	final String ERRO_EMAIL_CADASTRADO = "E-mail ja existente";
	final String ERRO_LOGIN_INVALIDO = "User e/ou senha invalidos";
	final String ERRO_ID_INVALIDO = "ID de User inválido ou nao encontrado";
	final String ERRO_SEM_ACESSO = "Nao autorizado";
	final String ERRO_TELEFONE_INVALIDO = "Telefone invalido";
	final String ERRO_SESSAO_INVALIDA = "Sessao invalida";

	@Value("${jwt.auth.header}")
	String authHeader;

	@Autowired
	private UserService userService;

	@Autowired
	private PhoneService phoneService;

	@Autowired
	private JwtService jwtService;

	@RequestMapping(value = "/usuarios", produces = "application/JSON")
	public List<User> buscarTodosUsuarios() {
		return userService.buscarTodosUsuarios();
	}

	@RequestMapping(value = "/usuario/{id}", produces = "application/JSON", method = RequestMethod.GET)
	public ResponseEntity<?> buscarUsuarioPorId(@PathVariable("id") String id, HttpServletRequest request) {
		final String authHeaderVal = request.getHeader(authHeader);

		try {
			JwtUserDTO jwtUser = jwtService.getUser(authHeaderVal);
			if (null != jwtUser && !jwtUser.getId().equals(id)) {
				MensagemErroDTO msg = new MensagemErroDTO(ERRO_SEM_ACESSO);
				return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.UNAUTHORIZED);
			} else {
				User user = userService.findByID(id);
				final Date loginTime30m = DateUtil.dataAdiantadaMeiaHora(user.getLast_login());
				if (loginTime30m.before(new Date())) {
					MensagemErroDTO msg = new MensagemErroDTO(ERRO_SESSAO_INVALIDA);
					return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.UNAUTHORIZED);
				} else {
					return new ResponseEntity<User>(user, HttpStatus.OK);
				}

			}

		} catch (Exception e) {
			MensagemErroDTO msg = new MensagemErroDTO(ERRO_SEM_ACESSO);
			return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/usuario", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

		if (userService.userExists(user.getEmail())) {
			MensagemErroDTO msg = new MensagemErroDTO(ERRO_EMAIL_CADASTRADO);
			return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.CONFLICT);
		} else {
			if (phoneService.isTelefoneValido(user)) {
				userService.saveUser(user);
			} else {
				MensagemErroDTO msg = new MensagemErroDTO(ERRO_TELEFONE_INVALIDO);
				return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody LoginDTO login, UriComponentsBuilder ucBuilder) {
		final User user = userService.buscarUsuarioPorEmail((login.getEmail()));
		final String password = MD5Generator.getMd5HashCode(login.getPassword());
		if (null != user && user.getPassword().equals(password)) {
			userService.updateLoginUser(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else if (null != user && !user.getPassword().equals(password)) {
			MensagemErroDTO msg = new MensagemErroDTO(ERRO_LOGIN_INVALIDO);
			return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.UNAUTHORIZED);
		} else {
			MensagemErroDTO msg = new MensagemErroDTO(ERRO_LOGIN_INVALIDO);
			return new ResponseEntity<MensagemErroDTO>(msg, HttpStatus.FORBIDDEN);
		}

	}

}
