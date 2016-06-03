package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.FinancesRepository;
import domain.Finances;

@Component
@Transactional
public class StringToFinancesConverter implements Converter<String, Finances> {

	@Autowired
	FinancesRepository financesRepository;

	@Override
	public Finances convert(String text) {
		Finances result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = financesRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
