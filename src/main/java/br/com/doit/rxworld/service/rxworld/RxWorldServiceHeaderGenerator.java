package br.com.doit.rxworld.service.rxworld;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.doit.rxworld.model.RxWorldConfig;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RxWorldServiceHeaderGenerator {
	public static String rxWorldServiceCredentials(RxWorldConfig rxWorldConfig) throws JsonProcessingException {
    	return "Bearer " + rxWorldConfig.currentBearerToken;
	}
}