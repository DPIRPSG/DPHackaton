package services.form;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Folder;
import domain.Manager;
import domain.Referee;
import domain.Runner;
import domain.form.ActorForm;

import security.TypeOfAuthority;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.RunnerService;

@Service
@Transactional
public class ActorFormService {
	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private RunnerService customerService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private RefereeService refereeService;

	@Autowired
	private UserAccountService userAccountService;

	// Constructors -----------------------------------------------------------

	public ActorFormService() {
		super();
	}

	// Other business methods -------------------------------------------------

	private ActorForm create(TypeOfAuthority authority) {
		ActorForm result;

		result = new ActorForm();
		result.setAuthority(authority);

		return result;
	}

	/**
	 * 
	 * @param typeNewActor
	 *            (poner a null para edición ! ! )
	 * @return
	 */
	public ActorForm createForm(TypeOfAuthority typeNewActor) {
		ActorForm result;

		if (actorService.checkLogin() && typeNewActor == null) {
			result = this.createFormActor(actorService.findByPrincipal());
		} else { // Creandose un actor
			result = this.create(typeNewActor);
		}

		return result;
	}

	private ActorForm createFormActor(Actor actor) {
		ActorForm result;

		result = this.create(TypeOfAuthority.transformAuthority(actor
				.getUserAccount().getAuthorities()));

		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setPhone(actor.getPhone());
		result.setUsername(actor.getUserAccount().getUsername());

		return result;
	}

	public void saveForm(ActorForm input) {
		if (input.getPassword() != null)
			Assert.isTrue(
					input.getPassword().equals(input.getRepeatedPassword()),
					"actorForm.error.passwordMismatch");

		if (actorService.checkLogin() && input.getAuthority() == null) {
			this.saveActor(input);
		} else {
			this.saveRegistration(input);
		}
	}

	private void saveActor(ActorForm input) {
		UserAccount acount;
		String pass;

		acount = actorService.findByPrincipal().getUserAccount();
		pass = input.getPassword();

		acount.setUsername(input.getUsername());
		if (pass != null) {
			if (!(pass.isEmpty() || pass.equals(""))) {
				acount.setPassword(pass);
				acount = userAccountService.modifyPassword(acount);
			}
		}

		// Check postHacking authority
		Assert.isTrue(
				TypeOfAuthority.transformAuthority(acount.getAuthorities())
						.equals(input.getAuthority()),
				"actorForm.error.saveActor.authorityModified");

		switch (input.getAuthority()) {
		case RUNNER:
			Runner result;

			result = customerService.findByPrincipal();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getPhone());
			result.setUserAccount(acount);

			customerService.saveFromEdit(result);
			break;

		case ADMIN:
			Administrator result1;

			result1 = administratorService.findByPrincipal();

			result1.setName(input.getName());
			result1.setSurname(input.getSurname());
			result1.setPhone(input.getPhone());
			result1.setUserAccount(acount);

			administratorService.saveFromEdit(result1);
			break;

		case MANAGER:
			Manager result11;

			result11 = managerService.findByPrincipal();

			result11.setName(input.getName());
			result11.setSurname(input.getSurname());
			result11.setPhone(input.getPhone());
			result11.setUserAccount(acount);

			managerService.saveFromEdit(result11);
			break;

		case REFEREE:
			Referee result111;

			result111 = refereeService.findByPrincipal();

			result111.setName(input.getName());
			result111.setSurname(input.getSurname());
			result111.setPhone(input.getPhone());
			result111.setUserAccount(acount);

			refereeService.saveFromEdit(result111);
			break;
		}
	}

	/**
	 * Guarda todo tipo de registros
	 * 
	 * @param input
	 */
	private void saveRegistration(ActorForm input) {
		UserAccount acount;
		Collection<Folder> folders;

		acount = userAccountService.createComplete(input.getUsername(),
				input.getPassword(), "CUSTOMER");

		switch (input.getAuthority()) {
		case RUNNER:
			Assert.isTrue(input.getAcceptTerm(), "actorForm.error.termsDenied");
			Assert.isTrue(!actorService.checkLogin());

			Runner result;
			result = customerService.create();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);

			customerService.saveFromEdit(result);
			break;

		case MANAGER:
			Assert.isTrue(actorService.checkAuthority("ADMIN"));

			Manager result;
			result = managerService.create();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);

			managerService.saveFromEdit(result);
			break;

		case REFEREE:
			Assert.isTrue(actorService.checkAuthority("ADMIN"));

			Referee result;
			result = refereeService.create();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);

			refereeService.saveFromEdit(result);
			break;
		}
	}

}
