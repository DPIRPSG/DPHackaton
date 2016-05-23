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
import services.FolderService;
import services.ManagerService;
import services.RefereeService;
import services.RunnerService;

@Service
@Transactional
public class ActorFormService {
	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private RunnerService runnerService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private FolderService folderService;

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
		result.setNif(actor.getNif());

		return result;
	}

	public Actor saveForm(ActorForm input) {
		if (input.getPassword() != null)
			Assert.isTrue(
					input.getPassword().equals(input.getRepeatedPassword()),
					"actorForm.error.passwordMismatch");
		
		if (actorService.checkLogin() && input.getAuthority() == null) {
			return this.saveActor(input);
		} else {
			return this.saveRegistration(input);
		}
	}

	private Actor saveActor(ActorForm input) {
		UserAccount acount;
		String pass;
		Actor saved;
		int actorId;

		acount = actorService.findByPrincipal().getUserAccount();
		pass = input.getPassword();

		acount.setUsername(input.getUsername());
		if (pass != null) {
			if (!(pass.isEmpty() || pass.equals(""))) {
				acount.setPassword(pass);
				acount = userAccountService.modifyPassword(acount);
			}
		}

		switch (TypeOfAuthority.transformAuthority(acount.getAuthorities())) {
		case RUNNER:
			Runner result;

			result = runnerService.findByPrincipal();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getPhone());
			result.setUserAccount(acount);
			result.setNif(input.getNif());

			actorId = runnerService.saveFromEdit(result).getId();
			break;

		case ADMIN:
			Administrator result1;

			result1 = administratorService.findByPrincipal();

			result1.setName(input.getName());
			result1.setSurname(input.getSurname());
			result1.setPhone(input.getPhone());
			result1.setUserAccount(acount);
			result1.setNif(input.getNif());

			actorId = administratorService.saveFromEdit(result1).getId();
			break;

		case MANAGER:
			Manager result11;

			result11 = managerService.findByPrincipal();

			result11.setName(input.getName());
			result11.setSurname(input.getSurname());
			result11.setPhone(input.getPhone());
			result11.setUserAccount(acount);
			result11.setNif(input.getNif());

			actorId = managerService.saveFromEdit(result11).getId();
			break;

		case REFEREE:
			Referee result111;

			result111 = refereeService.findByPrincipal();

			result111.setName(input.getName());
			result111.setSurname(input.getSurname());
			result111.setPhone(input.getPhone());
			result111.setUserAccount(acount);
			result111.setNif(input.getNif());

			actorId = refereeService.saveFromEdit(result111).getId();
			break;
		default:
			actorId = 0;
		}
		saved = actorService.findOne(actorId);
		
		return saved;
	}

	/**
	 * Guarda todo tipo de registros
	 * 
	 * @param input
	 */
	private Actor saveRegistration(ActorForm input) {
		UserAccount acount;
		@SuppressWarnings("unused")
		Collection<Folder> folders;
		int actorId;
		Actor saved;

		acount = userAccountService.createComplete(input.getUsername(),
					input.getPassword(), input.getAuthority().toString());

		//Encoding password
		acount = userAccountService.modifyPassword(acount);

		switch (input.getAuthority()) {
		case RUNNER:
			Assert.isTrue(input.getAcceptTerm(), "actorForm.error.termsDenied");
			Assert.isTrue(!actorService.checkLogin());
			Runner result;
			result = runnerService.create();

			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);
			result.setNif(input.getNif());
			
			folders = folderService.initializeSystemFolder(result);

			actorId = runnerService.saveFromEdit(result).getId();
			break;

		case MANAGER:
			Assert.isTrue(actorService.checkAuthority("ADMIN"));

			Manager result1;
			result1 = managerService.create();

			result1.setName(input.getName());
			result1.setSurname(input.getSurname());
			result1.setPhone(input.getUsername());
			result1.setUserAccount(acount);
			result1.setNif(input.getNif());
			
			folders = folderService.initializeSystemFolder(result1);

			actorId = managerService.saveFromEdit(result1).getId();
			break;

		case REFEREE:
			Assert.isTrue(actorService.checkAuthority("ADMIN"));

			Referee result11;
			result11 = refereeService.create();

			result11.setName(input.getName());
			result11.setSurname(input.getSurname());
			result11.setPhone(input.getUsername());
			result11.setUserAccount(acount);
			result11.setNif(input.getNif());
			
			folders = folderService.initializeSystemFolder(result11);

			actorId = refereeService.saveFromEdit(result11).getId();
			break;
		default:
			actorId = 0;
		}
		saved = actorService.findOne(actorId);

		folderService.save(folderService.initializeSystemFolder(saved));
		
		return saved;
		
	}

}
