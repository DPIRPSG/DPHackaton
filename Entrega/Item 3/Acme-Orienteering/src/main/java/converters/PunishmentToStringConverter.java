package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Punishment;

@Component
@Transactional
public class PunishmentToStringConverter implements Converter<Punishment, String> {
	
	@Override
	public String convert(Punishment punishment) {
		String result;

		if (punishment == null)
			result = null;
		else
			result = String.valueOf(punishment.getId());

		return result;
	}

}