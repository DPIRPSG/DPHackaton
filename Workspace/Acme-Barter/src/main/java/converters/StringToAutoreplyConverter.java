package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AutoreplyRepository;
import domain.Autoreply;

@Component
@Transactional
public class StringToAutoreplyConverter implements Converter<String, Autoreply> {

	@Autowired
	AutoreplyRepository autoreplyRepository;

	@Override
	public Autoreply convert(String text) {
		Autoreply result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = autoreplyRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
