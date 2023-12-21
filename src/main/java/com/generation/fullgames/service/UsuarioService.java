package com.generation.fullgames.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fullgames.model.Usuario;
import com.generation.fullgames.model.UsuarioLogin;
import com.generation.fullgames.repository.UsuarioRepository;
import com.generation.fullgames.security.JwtService;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	public Optional<Usuario> cadastrarUsuario(Usuario user){
		
		if(usuarioRepository.findByUsuario(user.getUsuario()).isPresent())//aqui eu face a buscar por usuario e caso esteja presente eu retorno vazio
			return Optional.empty();//ou seja se existir usuario eu retorno um erro
		

		if(checarIdade(user.getDt_nascimento()) <= 18)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário menor de idade", null);
		
		user.setSenha(criptografarSenha(user.getSenha())); // aqui eu chamo a funcao de criptografar
		
		return Optional.of(usuarioRepository.save(user));//aqui eu cadastro o novo usuario
		
	}
	
	
	public Optional<Usuario> atualizarUsuario(Usuario user){
		
		
		if(usuarioRepository.findById(user.getId()).isPresent()) {//para atualizar e necessario que o usuario exista
			
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(user.getUsuario());//ele cria um objeto do usuario enviado
			
			if((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != user.getId()))//ele verifica se o id cadastrado anteriormente do usuario e o mesmo passado
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);//caso nao seja devolve essa mensagem de erro
				
			
			user.setSenha(criptografarSenha(user.getSenha())); // aqui eu chamo a funcao de criptografar
			
			return Optional.ofNullable(usuarioRepository.save(user));//pega as informacoes novas e cadastra novamente
			
		}
		
		return Optional.empty();//retorna vazio caso nao existe um usuario semelhante ao passado
		
	}
	
	
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> userLogin) {
		
		  // Gera o Objeto de autenticação
				var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getUsuario(), userLogin.get().getSenha());//aqui pega o usuario e a senha que estao sendo passados pelo body
				
		        // Autentica o Usuario
				Authentication authentication = authenticationManager.authenticate(credenciais);//verifica a athenticacao
		        
		        // Se a autenticação foi efetuada com sucesso
				if (authentication.isAuthenticated()) {//compara as duas autheticacaoes

		            // Busca os dados do usuário
					Optional<Usuario> usuario = usuarioRepository.findByUsuario(userLogin.get().getUsuario());//cria um novo objeto com o usuario cadastrado

		            // Se o usuário foi encontrado
					if (usuario.isPresent()) {//verifica se o usuario existe

		                // Preenche o Objeto usuarioLogin com os dados encontrados 
						userLogin.get().setId(usuario.get().getId());//pegar o usuario cadastrado e coloco no usuario passados pelo login
						userLogin.get().setNome(usuario.get().getNome());
						userLogin.get().setFoto(usuario.get().getFoto());
						userLogin.get().setToken(gerarToken(userLogin.get().getUsuario()));
						userLogin.get().setSenha("");
						
		                 // Retorna o Objeto preenchido
					   return userLogin;
					
					}

		        } 
		            
				return Optional.empty();

	}
	
	
	private String criptografarSenha(String senha) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();//cria um objeto que possui uma funcao de criptografia
		
		return encoder.encode(senha); // nesse momento ele vai criptografar a senha
	}
	
	private int checarIdade(LocalDate dataNascismento) {
		
		return Period.between(dataNascismento, LocalDate.now()).getYears();
	}
	
	private String gerarToken(String user) {
		
		return "Bearer " + jwtService.generateToken(user);//cria o token com o nome bearer e com a chave dentro do jwt
	}
}
