package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.EnteredRepository;
import domain.Entered;

@Component
@Transactional
public class StringToEnteredConverter implements Converter<String, Entered> {

	@Autowired
	EnteredRepository enteredRepository;

	@Override
	public Entered convert(String text) {
		Entered result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = enteredRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
