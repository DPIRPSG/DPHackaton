package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Folder;
import domain.MessageEntity;

import repositories.MessageRepository;

@Service
@Transactional
public class MessageService {
	// Managed repository -----------------------------------------------------

	@Autowired
	private MessageRepository messageRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private FolderService folderService;

	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public MessageService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	/**
	 * Devuelve Message preparado para ser modificado. Necesita usar save para
	 * que persista en la base de datos
	 */
	public MessageEntity create() {
		MessageEntity result;
		Collection<Folder> folders;
		Collection<Actor> recipients;

		folders = new ArrayList<Folder>();
		recipients = new ArrayList<Actor>();
		result = new MessageEntity();

		result.setFolders(folders);
		result.setRecipients(recipients);
		result.setSender(actorService.findByPrincipal());
		result.setSentMoment(new Date());

		return result;
	}

	/**
	 * Guarda un message creado o modificado
	 */
	private MessageEntity save(MessageEntity message) {
		Assert.notNull(message);
		// Assert.isTrue(message.getSender().equals(actorService.findByPrincipal()),
		// "Only the sender can save the message");

		message.setSentMoment(new Date());

		MessageEntity result;

		result = messageRepository.save(message);

		return result;
	}

	public MessageEntity findOne(int messageId) {
		MessageEntity result;

		result = messageRepository.findOne(messageId);

		Assert.notNull(result, "message.findOne.UnknownID");

		this.checkActor(result);

		return result;
	}

	// Other business methods -------------------------------------------------

	/**
	 * Guarda la primera vez desde enviar !
	 */
	public MessageEntity firstSaveNormalSend(MessageEntity message) {
		Assert.notNull(message);

		int sendId, actId;
		MessageEntity result;

		sendId = message.getSender().getUserAccount().getId();
		actId = actorService.findByPrincipal().getUserAccount().getId();

		Assert.isTrue(sendId == actId);
		Assert.isTrue(message.getSender()
				.equals(actorService.findByPrincipal()),
				"Only the sender can save the message");

		result = this.firstSave(message);

		return result;
	}
	
	public MessageEntity saveFromFolderEdit(MessageEntity input){
		int actId;
		Collection<Folder> folders;
		
		actId = actorService.findByPrincipal().getUserAccount().getId();
		
		folders = input.getFolders();
		for(Folder a:folders){
			if(!a.getMessages().contains(input)){
				Assert.isTrue(a.getActor().getUserAccount().getId() == actId);
				input = this.findOne(input.getId());
				
				folderService.addMessage(a, input);
			}
		}
		
		return input;
	}

	/**
	 * Guarda la primera vez
	 */
	private MessageEntity firstSave(MessageEntity message) {
		Assert.notNull(message);

		MessageEntity result;

		result = this.save(message);
		this.addMessageToFolderFirst(result);

		return result;
	}

	/**
	 * Añade a las respectivas carpetas la primera vez que un mensaje es creado
	 */
	private void addMessageToFolderFirst(MessageEntity message) {

		for (Folder f : message.getSender().getFolders()) {
			if (f.getIsSystem() && f.getName().equals("OutBox"))
				folderService.addMessage(f, message);
		}

		for (Actor recipient : message.getRecipients()) {
			for (Folder f : recipient.getFolders()) {
				boolean toInBox;

				toInBox = f.getName().equals("InBox");

				if (toInBox && f.getIsSystem())
					folderService.addMessage(f, message);
			}
		}
	}

	/**
	 * Devuelve todos los mensajes contenidos en una determinada carpeta
	 */
	public Collection<MessageEntity> findAllByFolder(Folder folder) {
		Assert.notNull(folder);
		Assert.isTrue(folder.getActor().equals(actorService.findByPrincipal()),
				"Only the owner of the folder can display them");

		Collection<MessageEntity> result;

		result = messageRepository.findAllByFolderId(folder.getId());

		return result;
	}

	/**
	 * Borra un mensaje de una carpeta
	 */
	public void deleteMessageFromFolder(MessageEntity message, Folder folder) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.notNull(folder);
		Assert.isTrue(folder.getId() != 0);

		Assert.isTrue(folder.getActor().equals(actorService.findByPrincipal()),
				"Only the owner of the folder can delete a message");

		folderService.removeMessage(folder, message);
	}

	public void deleteDefinitely(int messaId) {
		MessageEntity messa;

		messa = this.findOne(messaId);

		this.checkActor(messa);
		// Assert.isTrue(messa.getFolders().size()==0,
		// "message.delete.useByOtherFolder");
		if (messa.getFolders().size() == 0)
			messageRepository.delete(messaId);
	}

	public void checkActor(MessageEntity input) {
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

	public void flush() {
		messageRepository.flush();
	}

}
