package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Runner;

@Component
@Transactional
public class RunnerToStringConverter implements Converter<Runner, String> {
	
	@Override
	public String convert(Runner runner) {
		String result;

		if (runner == null)
			result = null;
		else
			result = String.valueOf(runner.getId());

		return result;
	}

}