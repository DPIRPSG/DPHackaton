package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Classification;

@Component
@Transactional
public class ClassificationToStringConverter implements Converter<Classification, String> {
	
	@Override
	public String convert(Classification classification) {
		String result;

		if (classification == null)
			result = null;
		else
			result = String.valueOf(classification.getId());

		return result;
	}

}