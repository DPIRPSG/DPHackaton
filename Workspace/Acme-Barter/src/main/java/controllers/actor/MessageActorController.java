package controllers.actor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import services.MessageService;

import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping(value = "/message/actor")
public class MessageActorController extends AbstractController{

	//Services ----------------------------------------------------------

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	//Constructors ----------------------------------------------------------
	
	public MessageActorController(){
		super();
	}

	//Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int folderId){
        ModelAndView result;
        Collection<Message> messages;
        Folder folder;
        
        folder = folderService.findOne(folderId);
        
        folderService.checkActor(folder);
        
        messages = messageService.findAllByFolder(folder);
        
        result = new ModelAndView("message/list");
        result.addObject("messa", messages);
        result.addObject("folder", folder);
		result.addObject("requestURI", "message/actor/list.do?folderId="
				+ String.valueOf(folderId));
        
        return result;
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int messageId){
		ModelAndView result;
		Message messa;
		Collection<Folder> folders;
		
		messa = messageService.findOne(messageId);		
		folders = folderService.findByMessageAndActualActor(messa);
		
		result = new ModelAndView("message/display");
		result.addObject("messa", messa);
		result.addObject("folders", folders);
		
		return result;
	}
	
	
	// Creation ----------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Message message;
		
		message = messageService.create();
		
		message.setSender(actorService.findByPrincipal());
		
		result = createSendModelAndView(message);
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "send")
	public ModelAndView save(@Valid Message message, BindingResult binding) {
		ModelAndView result;
		int sendId, actId;
		boolean haySubject, hayBody, hayRecipients;
		
		hayBody = true;
		hayRecipients = true;
		haySubject = true;
		
		
		sendId = message.getSender().getUserAccount().getId();
		actId = actorService.findByPrincipal().getUserAccount().getId();
		
		Assert.isTrue(sendId == actId);
		

		if (binding.hasErrors()) {
			if(message.getBody() == "") {
				hayBody = false;
			}
			if(message.getRecipients() == null) {
				hayRecipients = false;
			}
			if(message.getSubject() == "") {
				haySubject = false;
			}
			result = createSendModelAndView(message,null, hayBody, hayRecipients, haySubject);
		} else {
			try {
				messageService.firstSaveNormalSend(message);
				result = new ModelAndView("redirect:../../folder/actor/list.do");
			} catch (Throwable oops) {
				result = createSendModelAndView(message, "message.commit.error", true, true, true);				
			}
		}

		return result;
	}

	// Edition ----------------------------------------------------------
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView removeFromFolder(@RequestParam int messageId, @RequestParam int folderId){
		ModelAndView result;
		Message message;
		Folder folder;
		
		message = messageService.findOne(messageId);
		
		folder = folderService.findOne(folderId);
		
		messageService.deleteMessageFromFolder(message, folder);
		
		result = new ModelAndView("redirect:../../folder/actor/list.do");
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView addFolder(@RequestParam int messageId){
		ModelAndView result;
		Message message;
		
		message = messageService.findOne(messageId);
		
		result = createEditModelAndView(message);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView addFolderSave(@Valid Message message, BindingResult binding){
		ModelAndView result;
		int actId;
		Collection<Folder> folders;
		
		actId = actorService.findByPrincipal().getUserAccount().getId();
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(message);
		} else {
			try {
				folders = message.getFolders();
				for(Folder a:folders){
					if(!a.getMessages().contains(message)){
						Assert.isTrue(a.getActor().getUserAccount().getId() == actId);
						
						a.addMessage(message);
						folderService.saveFromCreateOrEdit(a);
					}
				}
				result = new ModelAndView("redirect:/message/actor/display.do?messageId=" + message.getId());
			} catch (Throwable oops) {
				result = createEditModelAndView(message, "message.commit.error");				
			}
		}
		return result;
	}
	
	
	@RequestMapping(value = "/flag-as-spam", method = RequestMethod.GET)
	public ModelAndView flagAsSpam(@RequestParam(required=true) int messageId
			,@RequestParam(required=false, defaultValue="folder/actor/list.do") String redirectUri
			) {
	
		ModelAndView result;
		result = new ModelAndView("redirect:../../" + redirectUri);
		
		try {
			messageService.flagAsSpam(messageId);
			result.addObject("messageStatus", "message.flagspam.ok");				
		} catch (Throwable oops) {
			result.addObject("messageStatus", "message.flagspam.error");				
		}
		
		return result;
	}

	// Ancillary Methods ----------------------------------------------------------
	
	protected ModelAndView createSendModelAndView(Message input) {
		ModelAndView result;
		
		result = createSendModelAndView(input, null, true, true, true);
		
		return result;
	}
	
	protected ModelAndView createSendModelAndView(Message input, String message, boolean hayBody, boolean hayRecipients, boolean haySubject){
		ModelAndView result;
		Collection<Actor> actors;
		
		actors = actorService.findAll();
		
		result = new ModelAndView("message/create");
		result.addObject("messa", input);
		result.addObject("actors", actors);
		result.addObject("message", message);
		result.addObject("hayBody", hayBody);
		result.addObject("hayRecipients", hayRecipients);
		result.addObject("haySubject", haySubject);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Message input) {
		ModelAndView result;
		
		result = createEditModelAndView(input, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Message input, String message){
		ModelAndView result;
		Collection<Folder> folders;
		
		folders = folderService.findAllByActor();
		
		result = new ModelAndView("message/edit");
		result.addObject("messa", input);
		result.addObject("foldersActor", folders);
		result.addObject("message", message);
		
		return result;
	}
	
}
