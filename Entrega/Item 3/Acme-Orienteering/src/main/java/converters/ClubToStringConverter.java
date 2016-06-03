package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Club;

@Component
@Transactional
public class ClubToStringConverter implements Converter<Club, String> {
	
	@Override
	public String convert(Club club) {
		String result;

		if (club == null)
			result = null;
		else
			result = String.valueOf(club.getId());

		return result;
	}

}