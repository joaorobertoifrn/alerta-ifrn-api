package br.edu.ifrn.alerta.resources;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifrn.alerta.model.Alerta;
import br.edu.ifrn.alerta.mqtt.ClienteMQTT;
import br.edu.ifrn.alerta.services.AlertaService;

@RestController
@RequestMapping(value="/alertas")
public class AlertaResource {
	
	@Autowired
	private AlertaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Alerta> find(@PathVariable Integer id) {
		Alerta obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(value="enviarAlerta/{id}", method=RequestMethod.GET)
	public ResponseEntity<Alerta> enviarAlerta(@PathVariable Integer id) {
		Alerta obj = service.find(id);
        
		ClienteMQTT clienteMQTT = new ClienteMQTT("tcp://m14.cloudmqtt.com:10780", "wkhbfymo", "Aw6lLr0bZgA4");
        clienteMQTT.iniciar();
        String mensagem ="Tipo: " + obj.getCriticidade().getDescricao() + "/" + obj.getTexto() + "/" + "Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(obj.getInstante()) + "/" + "Nome: " + obj.getUsuario().getNome() ;
        //Gson gson = new Gson();
        //String json = gson.toJson(obj);
        clienteMQTT.publicar("alerta/IFRN", mensagem.getBytes(), 0);
        clienteMQTT.finalizar();
		
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Alerta obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Alerta>> findAll() {
		List<Alerta> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<Alerta>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Alerta> list = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}

}
