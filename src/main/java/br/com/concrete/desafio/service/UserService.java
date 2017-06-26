package br.com.concrete.desafio.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.concrete.desafio.dao.UserRepository;
import br.com.concrete.desafio.dto.JwtUserDTO;
import br.com.concrete.desafio.entity.Phone;
import br.com.concrete.desafio.entity.User;
import br.com.concrete.desafio.security.MD5Generator;
import br.com.concrete.desafio.util.DateUtil;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;

	/**
	 * Método responsavel por listar todos os Usuarios no Banco
	 * 
	 * @return usuarios persistidos no banco
	 */
	public List<User> buscarTodosUsuarios() {
		return (List<User>) userRepository.findAll();
	}

	/**
	 * Método responsavel por buscar usuario por email;
	 * 
	 * @param email
	 * @return Usuario encontado
	 */
	public User buscarUsuarioPorEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Método responsavel por buscar usuario por email;
	 * 
	 * @param email
	 * @return Usuario encontado
	 */
	public User findByID(final String id) {
		return userRepository.findById(id);
	}

	/**
	 * Método responsavel por verificar se usuario ja existe
	 * 
	 * @param email
	 * @return Boolean
	 */
	public Boolean userExists(final String email) {
		return buscarUsuarioPorEmail(email) != null;
	}

	/**
	 * Método Responsavel por cadastrar novo usuario
	 * 
	 * @param user
	 */
	public void saveUser(final User user) {
		final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		user.setId(uuid);
		user.setPassword(MD5Generator.getMd5HashCode(user.getPassword()));
		user.setCreated(DateUtil.dataAtual());
		user.setModified(null);
		user.setLast_login(null);
		JwtUserDTO jwtUser = new JwtUserDTO(user.getId(), user.getEmail());
		String token = jwtService.getToken(jwtUser);
		user.setToken(token);
		if (user.getPhones() != null && !user.getPhones().isEmpty()) {
			for (Phone item : user.getPhones()) {
				item.setUser(user);
			}
		}
		userRepository.save(user);
	}

	/**
	 * Método responsável por atualizar os dados do usuario a cada Login
	 * 
	 * @param user
	 */
	public void updateLoginUser(final User user) {
		user.setLast_login(new Date());
		user.setModified(new Date());
		userRepository.save(user);
	}


}
