package org.xhome.xauth.core.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhome.xauth.User;
import org.xhome.xauth.core.crypto.DefaultUserCrypto;
import org.xhome.xauth.core.crypto.UserCrypto;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20133:29:44 PM
 * @description 
 */
@Service
public class UserCryptoServiceImpl implements UserCryptoService {
	
	@Autowired(required = false)
	private Map<String, UserCrypto> userCryptoMaps;
	
	public final static String DEFAULT_USER_CRYPTO = "DEFAULT";

	@Override
	public void crypto(User user) {
		if (userCryptoMaps == null) {
			synchronized (this) {
				if (userCryptoMaps == null) {
					userCryptoMaps = new HashMap<String, UserCrypto>();
					userCryptoMaps.put(DEFAULT_USER_CRYPTO, new DefaultUserCrypto());
				}
			}
		}
		UserCrypto userCrypto = userCryptoMaps.get(user.getMethod());
		if (userCrypto == null) {
			userCrypto = userCryptoMaps.get(DEFAULT_USER_CRYPTO);
		}
		userCrypto.crypto(user);
	}
	
	public void setUserCryptoMaps(Map<String, UserCrypto> userCryptoMaps) {
		this.userCryptoMaps = userCryptoMaps;
	}
	
	public Map<String, UserCrypto> getUserCryptoMaps() {
		return this.userCryptoMaps;
	}
	
	public void registerUserCrypto(String method, UserCrypto userCrypto) {
		userCryptoMaps.put(method, userCrypto);
	}

}
