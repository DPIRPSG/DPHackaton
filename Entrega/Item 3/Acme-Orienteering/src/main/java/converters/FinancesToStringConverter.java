package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Finances;

@Component
@Transactional
public class FinancesToStringConverter implements Converter<Finances, String> {
	
	@Override
	public String convert(Finances input) {
		String result;

		if (input == null)
			result = null;
		else
			result = String.valueOf(input.getId());

		return result;
	}

}