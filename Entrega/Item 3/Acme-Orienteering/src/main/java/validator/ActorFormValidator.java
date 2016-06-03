package validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import security.TypeOfAuthority;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;

import domain.Actor;
import domain.form.ActorForm;

@Component
public class ActorFormValidator implements Validator{

	@Autowired
	private UserAccountService uAccountService;
	
	@Autowired
	private ActorService actorService;
	
	
	@Override
	public void validate(Object target, Errors errors) {

		ActorForm actor = (ActorForm) target;
		Actor actActor;
		UserAccount uAccountUsername;
		UserAccount actUAccount;
		
		try{
			actActor = actorService.findByPrincipal();
		}catch (Exception e) {
			actActor = null;
			
		}
		
		if(actActor == null || adminRegisterOtherUser(actActor, actor)){ // Creation moment
			actUAccount = null;
			if(actor.getPassword() == null){
				errors.rejectValue("password", "javax.validation.constraints.NotNull.message");
			}
			if(actor.getRepeatedPassword() == null){
				errors.rejectValue("repeatedPassword", "javax.validation.constraints.NotNull.message");
			}
			if(!adminRegisterOtherUser(actActor, actor) ){
				if(actor.getAcceptTerm() != true)
					errors.rejectValue("acceptTerm", "acme.validation.notSelected");
			}	
		}else{
			actUAccount = actActor.getUserAccount();
		}
		
		// Match passwords
		if(actor.getPassword() != null && actor.getRepeatedPassword() != null){
			if(actor.getPassword().equals("") &&
					! (actActor == null || adminRegisterOtherUser(actActor, actor))
					&& actor.getPassword().equals(actor.getRepeatedPassword())){
				// No error
			}else if (actor.getPassword().length() < 5 || actor.getPassword().length() > 32) {
				errors.rejectValue("password", "acme.validation.sizeNotMatch.standard");
			}else if(! actor.getPassword().equals(actor.getRepeatedPassword())){
				errors.rejectValue("password", "acme.validation.notMatch");
				errors.rejectValue("repeatedPassword", "acme.validation.notMatch");				
			}
		}
		
		uAccountUsername = uAccountService.findByUsername(actor.getUsername());
		
		// Username availability
		if(uAccountUsername != null){
			if(!uAccountUsername.equals(actUAccount))
				errors.rejectValue("username", "acme.validation.usernameInUse");
		}

	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ActorForm.class.isAssignableFrom(clazz);
	}
	
	private boolean adminRegisterOtherUser(Actor actActor, ActorForm actor) {
		boolean result;

		try {
			result = TypeOfAuthority.transformAuthority(
					actActor.getUserAccount().getAuthorities()).equals(
					TypeOfAuthority.ADMIN);
			result = result
					&& !actor.getAuthority().equals(TypeOfAuthority.ADMIN);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}


}
