package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.AttributeDescription;

@Component
@Transactional
public class AttributeDescriptionToStringConverter implements Converter<AttributeDescription, String> {
	
	@Override
	public String convert(AttributeDescription attributeDescription) {
		String result;

		if (attributeDescription == null)
			result = null;
		else
			result = String.valueOf(attributeDescription.getId());

		return result;
	}

}