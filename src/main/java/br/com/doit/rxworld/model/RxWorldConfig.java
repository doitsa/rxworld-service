package br.com.doit.rxworld.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.codec.digest.DigestUtils;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity(name = "rxworld_config")
public class RxWorldConfig extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(name = "client_id")
	public String clientId;
	@Column(name = "client_secret")
	public String clientSecret;
	@Column(name = "current_bearer_token")
	public String currentBearerToken;
	@Column(name = "token_expires_at")
	public LocalDateTime tokenExpiresAt;

	public boolean isTokenValid() {
		if (tokenExpiresAt == null) {
			return false;
		}

		return tokenExpiresAt.isAfter(LocalDateTime.now());
	}

	public boolean areCredentialsMatching(String credentialsMD5) {
		return DigestUtils.md5Hex(clientId + "-" + clientSecret).equals(credentialsMD5);
	}
}
