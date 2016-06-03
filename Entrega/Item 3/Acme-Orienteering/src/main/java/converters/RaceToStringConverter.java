package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Race;

@Component
@Transactional
public class RaceToStringConverter implements Converter<Race, String> {
	
	@Override
	public String convert(Race race) {
		String result;

		if (race == null)
			result = null;
		else
			result = String.valueOf(race.getId());

		return result;
	}

}