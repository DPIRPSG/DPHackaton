package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Attribute;

@Component
@Transactional
public class AttributeToStringConverter implements Converter<Attribute, String> {
	
	@Override
	public String convert(Attribute attribute) {
		String result;

		if (attribute == null)
			result = null;
		else
			result = String.valueOf(attribute.getId());

		return result;
	}

}