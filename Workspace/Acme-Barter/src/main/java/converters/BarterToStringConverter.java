package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Barter;

@Component
@Transactional
public class BarterToStringConverter implements Converter<Barter, String> {
	
	@Override
	public String convert(Barter barter) {
		String result;

		if (barter == null)
			result = null;
		else
			result = String.valueOf(barter.getId());

		return result;
	}

}