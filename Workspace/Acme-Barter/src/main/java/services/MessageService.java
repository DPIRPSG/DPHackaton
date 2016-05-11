package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Autoreply;
import domain.Folder;
import domain.Message;

import repositories.MessageRepository;

@Service
@Transactional 
public class MessageService {
 	//Managed repository -----------------------------------------------------

	@Autowired
	private MessageRepository messageRepository;
	
	//Supporting services ----------------------------------------------------

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AutoreplyService autoreplyService;
	
	//Constructors -----------------------------------------------------------

	public MessageService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/** 
	 * Devuelve Message preparado para ser modificado. Necesita usar save para que persista en la base de datos
	 */	
	//req: 24.2
	public Message create(){
		Message result;
		Collection<Folder> folders;
		Collection<Actor> recipients;
		
		folders = new ArrayList<Folder>();
		recipients = new ArrayList<Actor>();
		result = new Message();
			
		result.setFolders(folders);
		result.setRecipients(recipients);
		result.setSender(actorService.findByPrincipal());
		result.setSentMoment(new Date());
		
		return result;	
	}
	
	/**
	 * Guarda un message creado o modificado
	 */
	//req: 24.2
	private Message save(Message message){
		Assert.notNull(message);
//		Assert.isTrue(message.getSender().equals(actorService.findByPrincipal()), "Only the sender can save the message");
		
		message.setSentMoment(new Date());
		
		Message result;
		
		result = messageRepository.save(message);
		
		return result;
	}
	
	public Message findOne(int messageId){
		Message result;
		
		result = messageRepository.findOne(messageId);
		
		Assert.notNull(result, "message.findOne.UnknownID");
		
		checkActor(result);		
		
		return result;
	}

	//Other business methods -------------------------------------------------
	
	/**
	 * Guarda la primera vez desde enviar !
	 */
	public Message firstSaveNormalSend(Message message){
		Assert.notNull(message);
		
		int sendId, actId;
		Message result;
		
		sendId = message.getSender().getUserAccount().getId();
		actId = actorService.findByPrincipal().getUserAccount().getId();
		
		Assert.isTrue(sendId == actId);
		Assert.isTrue(message.getSender().equals(actorService.findByPrincipal()), "Only the sender can save the message");

				
		result = this.firstSave(message, false);
		
		return result;
	}
	
	/**
	 * Guarda la primera vez
	 */
	private Message firstSave(Message message, boolean isAutoreply){
		Assert.notNull(message);
		
		Message result;
		
		result = this.save(message);
		this.addMessageToFolderFirst(result, isAutoreply);
		
		return result;
	}
	
	/**
	 * Añade a las respectivas carpetas la primera vez que un mensaje es creado
	 */
	private void addMessageToFolderFirst(Message message, boolean isAutoreply){

		for (Folder f:message.getSender().getMessageBoxes()){
			if (f.getIsSystem() && f.getName().equals("OutBox") && !isAutoreply){
				folderService.addMessage(f, message);
			}else if(f.getIsSystem() && f.getName().equals("Auto-reply box") && isAutoreply){
				folderService.addMessage(f, message);				
			}
		}
		
		for (Actor recipient: message.getRecipients()){
			if(!isAutoreply)
				this.isResponseByAutoreply(message, recipient);
			
			for (Folder f:recipient.getMessageBoxes()){
				boolean toInBox; //, toAutoreply;
				
				toInBox = f.getName().equals("InBox"); // && !autoreply;
				// toAutoreply = f.getName().equals("Auto-reply box") && autoreply;
				
				if (toInBox
						//(toInBox|| toAutoreply)
						&& f.getIsSystem()){
					folderService.addMessage(f, message);
				}
			}
		}
	}
	
	
	
	/**
	 * Devuelve true en caso de que el mensaje tenga una respuesta automática
	 * activada y, si es el caso, la envía automáticamente
	 */
	private boolean isResponseByAutoreply(Message m, Actor actActorToSend){
		boolean isResponse, tempResponse;
		Collection<Autoreply> autoreplies;
		
		autoreplies = autoreplyService.findByActor(actActorToSend);
		isResponse = false;
		
		for(Autoreply auto:autoreplies){
			tempResponse = true;
			for(String s:auto.getKeyWords()){
				if(!m.getBody().toLowerCase().contains(s.toLowerCase())){
					tempResponse = false;
					break;
				}
			}
			if(tempResponse){
				sendResponseAutoreply(actActorToSend, m.getSender(), m.getSubject(), auto.getText());
				isResponse = true;
			}
		}
		
		
		return isResponse;
	}
	
	private void sendResponseAutoreply(Actor newSender, Actor newReceiver, String oldSubject, String textToSend){
		Message toSend;
		Collection<Actor> recipients;
		
		toSend = this.create();
		recipients = toSend.getRecipients();
		recipients.add(newReceiver);
		
		toSend.setSender(newSender);
		toSend.setRecipients(recipients);
		toSend.setBody(textToSend);
		toSend.setSubject("AUTO-REPLY -- " + oldSubject);
		
		this.firstSave(toSend, true);
	}
	
	
	/**
	 * Devuelve todos los mensajes contenidos en una determinada carpeta
	 */
	//req: 24.1
	public Collection<Message> findAllByFolder(Folder folder){
		Assert.notNull(folder);
		Assert.isTrue(folder.getActor().equals(actorService.findByPrincipal()), "Only the owner of the folder can display them");
		
		Collection<Message> result;
		
		result = messageRepository.findAllByFolderId(folder.getId());
		
		return result;
	}
	
	/**
	 * Borra un mensaje de una carpeta
	 */
	//req: 24.2
	public void deleteMessageFromFolder(Message message, Folder folder){
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.notNull(folder);
		Assert.isTrue(folder.getId() != 0);
		
		Assert.isTrue(folder.getActor().equals(actorService.findByPrincipal()), "Only the owner of the folder can delete a message");		
		
		folderService.removeMessage(folder, message);
	}
	
	/**
	 *  Marca un mensaje como Spam para el usuario actual
	 * @param input
	 */
	public void flagAsSpam(int messaId){
		Message actMessage;
		Actor actActor;
		
		actMessage = this.findOne(messaId);
		actActor = actorService.findByPrincipal();
		
		Assert.notNull(actMessage);
		checkActor(actMessage);
		
		// Add to SpamBox
		for (Folder b:actActor.getMessageBoxes()){
			if(b.getIsSystem()==true && b.getName().equals("SpamBox")){
				folderService.addMessage(b, actMessage);
				break;
			}
		}
		actMessage = this.findOne(messaId);
		actActor = actorService.findByPrincipal();
		
		// Remove from folders
		for (Folder a : actMessage.getFolders()) {
			if (a.getActor().equals(actActor)
					&& !(a.getIsSystem() == true && a.getName().equals("SpamBox"))
					//&& !(a.getIsSystem() == true && a.getName().equals("TrashBox"))
					)
				folderService.removeMessage(a, actMessage);
		}
	}
	
	public void deleteDefinitely(int messaId){
		Message messa;
		
		messa = this.findOne(messaId);
		
		this.checkActor(messa);
//		Assert.isTrue(messa.getFolders().size()==0, "message.delete.useByOtherFolder");
		if(messa.getFolders().size() == 0)
			messageRepository.delete(messaId);
	}
	
	
	public void checkActor(Message input){
		int actId;
		int inputId;

		boolean res;
		
		actId = actorService.findByPrincipal().getUserAccount().getId();
		inputId = input.getSender().getUserAccount().getId();
		res = false;
		
		if (actId == inputId) {
			res = true;
		} else {
			for (Actor a : input.getRecipients()) {
				inputId = a.getUserAccount().getId();

				if (actId == inputId) {
					res = true;
					break;
				}
			}
		}
		
		Assert.isTrue(res, "message.consult.notOwner");
	}
	
	public void flush(){
		messageRepository.flush();
	}
	
}
