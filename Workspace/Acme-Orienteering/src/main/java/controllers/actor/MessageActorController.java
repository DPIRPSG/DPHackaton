//package controllers.actor;
//
//import java.util.Collection;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.Assert;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import services.ActorService;
//import services.FolderService;
//import services.MessageService;
//
//import controllers.AbstractController;
//import domain.Actor;
//import domain.Folder;
//import domain.MessageEntity;
//
//@Controller
//@RequestMapping(value = "/message/actor")
//public class MessageActorController extends AbstractController {
//
//	// Services ----------------------------------------------------------
//
//	@Autowired
//	private MessageService messageService;
//
//	@Autowired
//	private FolderService folderService;
//
//	@Autowired
//	private ActorService actorService;
//
//	// Constructors ----------------------------------------------------------
//
//	public MessageActorController() {
//		super();
//	}
//
//	// Listing ----------------------------------------------------------
//
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public ModelAndView list(@RequestParam int folderId) {
//		ModelAndView result;
//		Collection<MessageEntity> messages;
//		Folder folder;
//
//		folder = folderService.findOne(folderId);
//
//		messages = messageService.findAllByFolder(folder);
//
//		result = new ModelAndView("message/list");
//		result.addObject("messa", messages);
//		result.addObject("folder", folder);
//		result.addObject("requestURI", "message/actor/list.do?folderId="
//				+ String.valueOf(folderId));
//
//		return result;
//	}
//
//	@RequestMapping(value = "/display", method = RequestMethod.GET)
//	public ModelAndView display(@RequestParam int messageId) {
//		ModelAndView result;
//		MessageEntity messa;
//		Collection<Folder> folders;
//
//		messa = messageService.findOne(messageId);
//		folders = folderService.findByMessageAndActualActor(messa);
//
//		result = new ModelAndView("message/display");
//		result.addObject("messa", messa);
//		result.addObject("folders", folders);
//
//		return result;
//	}
//
//	// Creation ----------------------------------------------------------
//
//	@RequestMapping(value = "/create", method = RequestMethod.GET)
//	public ModelAndView create() {
//		ModelAndView result;
//		MessageEntity message;
//
//		message = messageService.create();
//
//		result = createSendModelAndView(message);
//
//		return result;
//	}
//
//	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "send")
//	public ModelAndView save(@Valid MessageEntity message, BindingResult binding) {
//		ModelAndView result;
//		int sendId, actId;
//
//		sendId = message.getSender().getUserAccount().getId();
//		actId = actorService.findByPrincipal().getUserAccount().getId();
//
//		Assert.isTrue(sendId == actId);
//
//		if (binding.hasErrors()) {
//			result = createSendModelAndView(message);
//		} else {
//			try {
//				messageService.firstSaveNormalSend(message);
//				result = new ModelAndView("redirect:../../folder/actor/list.do");
//			} catch (Throwable oops) {
//				result = createSendModelAndView(message, "message.commit.error");
//			}
//		}
//
//		return result;
//	}
//
//	// Edition ----------------------------------------------------------
//
//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
//	public ModelAndView removeFromFolder(@RequestParam int messageId,
//			@RequestParam int folderId) {
//		ModelAndView result;
//		MessageEntity message;
//		Folder folder;
//
//		message = messageService.findOne(messageId);
//
//		folder = folderService.findOne(folderId);
//
//		messageService.deleteMessageFromFolder(message, folder);
//
//		result = new ModelAndView("redirect:../../folder/actor/list.do");
//
//		return result;
//	}
//
//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
//	public ModelAndView addFolder(@RequestParam int messageId) {
//		ModelAndView result;
//		MessageEntity message;
//
//		message = messageService.findOne(messageId);
//
//		result = createEditModelAndView(message);
//
//		return result;
//	}
//
//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
//	public ModelAndView addFolderSave(@Valid MessageEntity message,
//			BindingResult binding) {
//		ModelAndView result;
//
//		if (binding.hasErrors()) {
//			result = createEditModelAndView(message);
//		} else {
//			try {
//				message = messageService.saveFromFolderEdit(message);
//				result = new ModelAndView(
//						"redirect:/message/actor/display.do?messageId="
//								+ message.getId());
//			} catch (Throwable oops) {
//				result = createEditModelAndView(message, "message.commit.error");
//			}
//		}
//		return result;
//	}
//
//	// Ancillary Methods
//	// ----------------------------------------------------------
//
//	protected ModelAndView createSendModelAndView(MessageEntity input) {
//		ModelAndView result;
//
//		result = createSendModelAndView(input, null);
//
//		return result;
//	}
//
//	protected ModelAndView createSendModelAndView(MessageEntity input,
//			String message) {
//		ModelAndView result;
//		Collection<Actor> actors;
//
//		actors = actorService.findAll();
//
//		result = new ModelAndView("message/create");
//		result.addObject("messageEntity", input);
//		result.addObject("actors", actors);
//		result.addObject("message", message);
//
//		return result;
//	}
//
//	protected ModelAndView createEditModelAndView(MessageEntity input) {
//		ModelAndView result;
//
//		result = createEditModelAndView(input, null);
//
//		return result;
//	}
//
//	protected ModelAndView createEditModelAndView(MessageEntity input,
//			String message) {
//		ModelAndView result;
//		Collection<Folder> folders;
//
//		folders = folderService.findAllByActor();
//
//		result = new ModelAndView("message/edit");
//		result.addObject("messageEntity", input);
//		result.addObject("foldersActor", folders);
//		result.addObject("message", message);
//
//		return result;
//	}
//
//}
