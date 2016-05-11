package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BarterService;
import services.UserService;
import domain.Barter;

@Controller
@RequestMapping("/barter")
public class BarterController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private BarterService barterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public BarterController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="") String keyword) {
		ModelAndView result;
		Collection<Barter> barters;
		String keywordToFind;

		barters = barterService.findAllNotCancelled();
		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					barters = barterService.findBySingleKeywordNotCancelled(keywordToFind);
					break;
				}
			}
		}
		
		result = new ModelAndView("barter/list");
		result.addObject("requestURI", "barter/list.do");
		result.addObject("barters", barters);
		if(actorService.checkAuthority("USER"))
			result.addObject("userId", userService.findByPrincipal().getId());


		return result;
	}
	
	@RequestMapping(value = "/list2", method = RequestMethod.GET)
	public ModelAndView list2(@RequestParam(required=false, defaultValue="") String keyword, int barterId) {
		ModelAndView result;
		Collection<Barter> barters;
		String keywordToFind;

		barters = barterService.getRelatedBarters(barterId);		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					barters = barterService.findBySingleKeyword(keywordToFind);
					break;
				}
			}
		}
		if(!actorService.checkAuthority("ADMIN")){
			Collection<Barter> toRemove;
			toRemove = new ArrayList<Barter>();
			for(Barter b:barters){
				if(b.isCancelled())
					toRemove.add(b);
			}
			barters.removeAll(toRemove);
		}
		result = new ModelAndView("barter/list");
		result.addObject("requestURI", "barter/list2.do");
		result.addObject("barters", barters);
		if(actorService.checkAuthority("USER"))
			result.addObject("userId", userService.findByPrincipal().getId());

		return result;
	}
	
	@RequestMapping(value = "/listByUser", method = RequestMethod.GET)
	public ModelAndView listByUser(@RequestParam(required=true) String userId) {
		ModelAndView result;
		Collection<Barter> barters;
		int user_id;
		
		user_id = Integer.valueOf(userId).intValue();

		barters = barterService.findByUserNotCancelled(user_id);
		
		result = new ModelAndView("barter/list");
		result.addObject("requestURI", "barter/listByUser.do?userId" + userId);
		result.addObject("barters", barters);
		result.addObject("userIdListing", userId);
		if(actorService.checkAuthority("USER"))
			result.addObject("userId", userService.findByPrincipal().getId());

		return result;
	}
}
