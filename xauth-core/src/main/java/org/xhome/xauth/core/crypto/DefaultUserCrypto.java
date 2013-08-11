package org.xhome.xauth.core.crypto;

import org.xhome.crypto.AES;
import org.xhome.crypto.Base64;
import org.xhome.crypto.MD5;
import org.xhome.crypto.SHA1;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20133:14:34 PM
 * @description 
 */
public class DefaultUserCrypto implements UserCrypto {

	@Override
	public void crypto(User user) {
		String key = new SHA1().encrypt(user.getName()).substring(5, 21);
		AES aes = new AES(key);
		String e = aes.encrypt(new MD5().encrypt(user.getPassword()));
		byte[] r = new byte[32];
		for (int i = 0, j = 0; i < 32; i++) {
			r[i] = (byte) (((int) e.charAt(j++)) + ((int) e.charAt(j++)));
		}
		user.setPassword(new String(Base64.encodeBytes(r)).substring(0, 32));
	}

}
