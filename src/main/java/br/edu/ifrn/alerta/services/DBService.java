package br.edu.ifrn.alerta.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifrn.alerta.model.Alerta;
import br.edu.ifrn.alerta.model.Criticidade;
import br.edu.ifrn.alerta.model.Usuario;
import br.edu.ifrn.alerta.model.enums.Perfil;
import br.edu.ifrn.alerta.repositories.AlertaRepository;
import br.edu.ifrn.alerta.repositories.UsuarioRepository;

@Service
public class DBService {

	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AlertaRepository alertaRepository;
	
	public void instantiateTestDatabase() throws ParseException {
		
		Usuario usuario1 = new Usuario(null, "Aluno", "aluno@ifrn.edu.br", pe.encode("123"));
		usuario1.addPerfil(Perfil.ALUNO);
		
		Usuario usuario2 = new Usuario(null, "Administrador", "admin@ifrn.edu.br", pe.encode("456"));
		usuario2.addPerfil(Perfil.ADMIN);
		
		usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		
		Criticidade criticidade1 = new Criticidade(null,"Alta");
		Criticidade criticidade2 = new Criticidade(null,"Média");
		Criticidade criticidade3 = new Criticidade(null,"Baixa");
		Criticidade criticidade4 = new Criticidade(null,"Informativo");
		
		Alerta alerta1 = new Alerta(null,"Alerta Perigo. Keep Out NOW", usuario2, sdf.parse("30/09/2019 10:32"), criticidade1);
		Alerta alerta2 = new Alerta(null,"Alerta 02", usuario2, sdf.parse("10/10/2019 19:35"), criticidade2);
		Alerta alerta3 = new Alerta(null,"Alerta 03", usuario2, sdf.parse("05/05/2019 13:00"), criticidade3);
		Alerta alerta4 = new Alerta(null,"Alerta 04", usuario2, sdf.parse("10/10/2019 09:20"), criticidade4);
		
		alertaRepository.saveAll(Arrays.asList(alerta1, alerta2, alerta3, alerta4));
		
	}
}
