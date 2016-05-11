package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.LegalTextRepository;
import domain.LegalText;

@Component
@Transactional
public class StringToLegalTextConverter implements Converter<String, LegalText> {

	@Autowired
	LegalTextRepository legalTextRepository;

	@Override
	public LegalText convert(String text) {
		LegalText result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = legalTextRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
