package security;

import java.util.Collection;

import org.springframework.util.Assert;

public enum TypeOfAuthority {
	RUNNER,
	ADMIN,
	MANAGER,
	REFEREE;
	
	/**
	 * Convierte la colección de authorities al enum TypeOfAuthority
	 * @param input
	 * @return
	 */
	public static TypeOfAuthority transformAuthority(Collection<Authority> input){
		TypeOfAuthority result;
		
		result = null;
		for (Authority au : input) {
			switch (au.getAuthority()) {
			case "RUNNER":
				result = TypeOfAuthority.RUNNER;
				break;
			case "ADMIN":
				result = TypeOfAuthority.ADMIN;
				break;
			case "MANAGER":
				result = TypeOfAuthority.MANAGER;
				break;
			case "REFEREE":
				result = TypeOfAuthority.REFEREE;
				break;
			default:
				break;
			}
		}
		Assert.isNull(result);
		
		return result;
	};
	
//	public TypeOfAuthority toAuthorities(){
//		// String result;
//		Collection<Authority> result;
//		Authority au;
//
//		result = new ArrayList<Authority>();
//		au = new Authority();
//		result.add(e)
//		
//		return result;
//	}
	
	@Override
	public String toString(){
		String result;
		switch (this) {
		case RUNNER:
			result = "RUNNER";
			break;
		case ADMIN:
			result = "ADMIN";
			break;
		case MANAGER:
			result = "MANAGER";
			break;
		case REFEREE:
			result = "REFEREE";
			break;
		default:
			result = "";
			break;
		}
		return result;
	};
};
