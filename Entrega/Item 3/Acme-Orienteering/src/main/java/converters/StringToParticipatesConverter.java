package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ParticipatesRepository;
import domain.Participates;

@Component
@Transactional
public class StringToParticipatesConverter implements Converter<String, Participates> {

	@Autowired
	ParticipatesRepository participatesRepository;

	@Override
	public Participates convert(String text) {
		Participates result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = participatesRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
