package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RunnerRepository;
import domain.Runner;

@Component
@Transactional
public class StringToRunnerConverter implements Converter<String, Runner> {

	@Autowired
	RunnerRepository runnerRepository;

	@Override
	public Runner convert(String text) {
		Runner result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = runnerRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
