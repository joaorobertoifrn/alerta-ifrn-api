package br.edu.ifrn.alerta.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.edu.ifrn.alerta.model.Alerta;
import br.edu.ifrn.alerta.model.enums.Perfil;
import br.edu.ifrn.alerta.repositories.AlertaRepository;
import br.edu.ifrn.alerta.security.UserSS;
import br.edu.ifrn.alerta.services.exceptions.AuthorizationException;
import br.edu.ifrn.alerta.services.exceptions.ObjectNotFoundException;

@Service
public class AlertaService {

	@Autowired
	private AlertaRepository repo;

	public Alerta find(Integer id) {

		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

		Alerta obj = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Alerta.class.getName()));
		return obj;
	}

	public Alerta insert(Alerta obj) {
		obj.setId(null);
		obj = repo.save(obj);
		return obj;
	}

	public List<Alerta> findAll() {
		return repo.findAll();
	}

	public Page<Alerta> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

}
