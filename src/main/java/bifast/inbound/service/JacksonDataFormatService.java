package bifast.inbound.service;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

@Service
public class JacksonDataFormatService {

	public JacksonDataFormat wrapUnwrapRoot (Class<?> unmarshalType) {
		
		JacksonDataFormat jdf = new JacksonDataFormat(unmarshalType);
		jdf.addModule(new JaxbAnnotationModule());  //supaya nama element pake annot JAXB (uppercasecamel)
		jdf.setInclude("NON_NULL");
		jdf.setInclude("NON_EMPTY");
		jdf.enableFeature(SerializationFeature.WRAP_ROOT_VALUE);
		jdf.enableFeature(DeserializationFeature.UNWRAP_ROOT_VALUE);
		jdf.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return jdf;
	}
	
	public JacksonDataFormat wrapRoot (Class<?> unmarshalType) {
		
		JacksonDataFormat jdf = new JacksonDataFormat(unmarshalType);
		jdf.addModule(new JaxbAnnotationModule());  //supaya nama element pake annot JAXB (uppercasecamel)
		jdf.setInclude("NON_NULL");
		jdf.setInclude("NON_EMPTY");
		jdf.enableFeature(SerializationFeature.WRAP_ROOT_VALUE);
		jdf.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return jdf;
	}
	
	public JacksonDataFormat unwrapRoot (Class<?> unmarshalType) {
		
		JacksonDataFormat jdf = new JacksonDataFormat(unmarshalType);
		jdf.addModule(new JaxbAnnotationModule());  //supaya nama element pake annot JAXB (uppercasecamel)
		jdf.setInclude("NON_NULL");
		jdf.setInclude("NON_EMPTY");
		jdf.enableFeature(DeserializationFeature.UNWRAP_ROOT_VALUE);
		jdf.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return jdf;
	}
	
	public JacksonDataFormat basic (Class<?> unmarshalType) {
		JacksonDataFormat jdf = new JacksonDataFormat(unmarshalType);
		jdf.setInclude("NON_NULL");
		jdf.setInclude("NON_EMPTY");
		jdf.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return jdf;
	}

	public JacksonDataFormat basicPrettyPrint (Class<?> unmarshalType) {
		JacksonDataFormat jdf = new JacksonDataFormat(unmarshalType);
		jdf.setInclude("NON_NULL");
		jdf.setInclude("NON_EMPTY");
		jdf.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		jdf.setPrettyPrint(true);
		return jdf;
	}

}
