package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Participates;

@Component
@Transactional
public class ParticipatesToStringConverter implements Converter<Participates, String> {
	
	@Override
	public String convert(Participates participates) {
		String result;

		if (participates == null)
			result = null;
		else
			result = String.valueOf(participates.getId());

		return result;
	}

}