package br.com.doit.rxworld.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class RxWorldConfigTest {
private RxWorldConfig rxWorldConfig;
	
	@BeforeEach
	public void setup() {
		rxWorldConfig = new RxWorldConfig();
		rxWorldConfig.clientId = "clientId";
		rxWorldConfig.clientSecret = "clientSecret";
		rxWorldConfig.currentBearerToken = "token";
		rxWorldConfig.tokenExpiresAt = LocalDateTime.MAX;
	}
	
	@Test
	public void tokenIsValidBeforeExpiring(){
		assertTrue(rxWorldConfig.isTokenValid());
	}
	
	@Test
	public void tokenIsNotValidAfterExpiring(){
		rxWorldConfig.tokenExpiresAt = LocalDateTime.MIN;
		
		assertFalse(rxWorldConfig.isTokenValid());
	}
	
	@Test
	public void tokenIsNotValidBeforeBeingGenerated(){
		rxWorldConfig.tokenExpiresAt = null;
		
		assertFalse(rxWorldConfig.isTokenValid());
	}
	
	@Test
	public void credentialsAreMatching() {
		rxWorldConfig.clientId = "U5z9Fy0f6zjCLim";
		rxWorldConfig.clientSecret = "cCakpaFxTiQIS9N";
		
		assertTrue(rxWorldConfig.areCredentialsMatching("3837da76f818af376286bc25f21a2040"));
	}
	
	@Test
	public void credentialsAreNotMatching() {
		rxWorldConfig.clientId = "1234";
		rxWorldConfig.clientSecret = "1234";
		
		assertFalse(rxWorldConfig.areCredentialsMatching("3837da76f818af376286bc25f21a2040"));
	}
	
	@Test
	public void credentialsAreNotMacthingForNewConfig() {
		rxWorldConfig.clientId = null;
		rxWorldConfig.clientSecret = null;
		
		assertFalse(rxWorldConfig.areCredentialsMatching("3837da76f818af376286bc25f21a2040"));
	}
}