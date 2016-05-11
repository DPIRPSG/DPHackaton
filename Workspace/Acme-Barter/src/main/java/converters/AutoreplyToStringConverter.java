package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Autoreply;

@Component
@Transactional
public class AutoreplyToStringConverter implements Converter<Autoreply, String> {
	
	@Override
	public String convert(Autoreply autoreply) {
		String result;

		if (autoreply == null)
			result = null;
		else
			result = String.valueOf(autoreply.getId());

		return result;
	}

}