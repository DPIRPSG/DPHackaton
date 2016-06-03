package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PunishmentRepository;
import domain.Punishment;

@Component
@Transactional
public class StringToPunishmentConverter implements Converter<String, Punishment> {

	@Autowired
	PunishmentRepository punishmentRepository;

	@Override
	public Punishment convert(String text) {
		Punishment result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = punishmentRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
