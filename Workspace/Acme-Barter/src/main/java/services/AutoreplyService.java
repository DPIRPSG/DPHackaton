package services;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Autoreply;
import repositories.AutoreplyRepository;

@Service
@Transactional 
public class AutoreplyService {
 	//Managed repository -----------------------------------------------------

	@Autowired
	private AutoreplyRepository autoreplyRepository;
	
	//Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	//Constructors -----------------------------------------------------------

	public AutoreplyService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/** 
	 * Devuelve Autoreply preparado para ser modificado. Necesita usar save para que persista en la base de datos
	 */	
	//req: 24.2
	public Autoreply create(){
		Autoreply result;
		Collection<String> keyWords;
		
		result = new Autoreply();
		
		keyWords = new ArrayList<String>();
			
		result.setActor(actorService.findByPrincipal());
		result.setKeyWords(keyWords);
		
		return result;	
	}
	
	/**
	 * Guarda un message creado o modificado
	 */
	private Autoreply save(Autoreply autoreply){
		Assert.notNull(autoreply);
		
		Autoreply result;
		
		Assert.isTrue(
				autoreply.getActor().equals(actorService.findByPrincipal()),
				"autoreply.edit.notProperty");
		
		result = autoreplyRepository.save(autoreply);
		
		return result;
	}
	
	public Autoreply findOne(int autoreplyId){
		Autoreply result;
		
		result = autoreplyRepository.findOne(autoreplyId);
		
		Assert.notNull(result, "autoreply.findOne.UnknownID");
		
		return result;
	}
	
	private void delete(int autoreplyId){
		autoreplyRepository.delete(autoreplyId);
	}

	//Other business methods -------------------------------------------------
	
	public Autoreply saveFromEdit(Autoreply autoreply){
		Assert.notNull(autoreply);
		Assert.isTrue(actorService.checkAuthority("USER"), "autoreply.notUserAuthority");
		
		Autoreply auto, result;
		
		if(autoreply.getId() != 0){
			auto = this.findOne(autoreply.getId());
		
			auto.setText(autoreply.getText());
			auto.setKeyWords(autoreply.getKeyWords());
		}else{
			auto = autoreply;
		}
		Assert.isTrue(auto.getActor().equals(autoreply.getActor()), "autoreply.edit.notSameActor");
		
		result = this.save(auto);
		
		return result;
	}
	
	public Autoreply findToEdit(int autoreplyId){
		Autoreply result;
		
		result = this.findOne(autoreplyId);
		
		checkActor(result);
		
		return result;
	}
	
	public void deleteFromEdit(Autoreply autoreply){
		Assert.notNull(autoreply);
		
		Autoreply auto;
		
		auto = this.findOne(autoreply.getId());
		
		checkActor(auto);
		
		this.delete(autoreply.getId());
	}
	
	
	/**
	 * Devuelve todos los autoreplies del actor actual
	 */
	//req: 24.1
	public Collection<Autoreply> findByPrincipal(){
		Actor actor;
		Collection<Autoreply> result;
		
		actor = actorService.findByPrincipal();
		
		result = this.findByActor(actor);
		
		return result;
	}
	
	/**
	 * Devuelve todos los autoreplies del actor dado
	 */
	public Collection<Autoreply> findByActor(Actor actor){
		Assert.notNull(actorService.findByPrincipal(), "autoreply.NotLoggedActor");
		Collection<Autoreply> result;
		
		Assert.notNull(actor);
		
		result = autoreplyRepository.findByActorId(actor.getId());
		
		return result;
	}
	
	/**
	 * Don't use before save ! !
	 * @param input
	 */
	public void checkActor(Autoreply input){
		
		Assert.isTrue(input.getActor().getUserAccount().getId() == actorService
				.findByPrincipal().getUserAccount().getId(), "autoreply.notOwner");
	}
	
	public void flush(){
		autoreplyRepository.flush();
	}
	
}
