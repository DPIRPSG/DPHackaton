package controllers.manager;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.form.FeePaymentFormService;
import controllers.AbstractController;
import domain.form.FeePaymentForm;

@Controller
@RequestMapping("/feePayment/gerente")
public class FeePaymentManagerController extends AbstractController {

	// Services ----------------------------------------------------------
	
	@Autowired
	private FeePaymentFormService feePaymentFormService;
	
	// Constructors --------------------------------------------------------
	
	public FeePaymentManagerController() {
		super();
	}
	
	// Creating ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer leagueId) {
		ModelAndView result;
		FeePaymentForm feePaymentForm;

		feePaymentForm = feePaymentFormService.create(leagueId);
		
		result = createEditModelAndView(feePaymentForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FeePaymentForm feePaymentForm, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(feePaymentForm);
		} else {
			try {
				feePaymentFormService.reconstruct(feePaymentForm);
				result = new ModelAndView("redirect:../../league/list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(feePaymentForm, "feePayment.commit.error");
			}
		}

		return result;
	}
	
	// Ancillary Methods ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(FeePaymentForm feePaymentForm) {
		ModelAndView result;

		result = createEditModelAndView(feePaymentForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FeePaymentForm feePaymentForm, String message) {
		ModelAndView result;

		result = new ModelAndView("feePayment/create");
		result.addObject("feePaymentForm", feePaymentForm);
		result.addObject("message", message);

		return result;
	}
	
}
