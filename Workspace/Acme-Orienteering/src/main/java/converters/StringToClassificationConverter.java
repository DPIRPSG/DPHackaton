package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ClassificationRepository;
import domain.Classification;

@Component
@Transactional
public class StringToClassificationConverter implements Converter<String, Classification> {

	@Autowired
	ClassificationRepository classificationRepository;

	@Override
	public Classification convert(String text) {
		Classification result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = classificationRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
