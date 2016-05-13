package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.League;

@Component
@Transactional
public class LeagueToStringConverter implements Converter<League, String> {
	
	@Override
	public String convert(League league) {
		String result;

		if (league == null)
			result = null;
		else
			result = String.valueOf(league.getId());

		return result;
	}

}