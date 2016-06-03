package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Entered;

@Component
@Transactional
public class EnteredToStringConverter implements Converter<Entered, String> {
	
	@Override
	public String convert(Entered entered) {
		String result;

		if (entered == null)
			result = null;
		else
			result = String.valueOf(entered.getId());

		return result;
	}

}