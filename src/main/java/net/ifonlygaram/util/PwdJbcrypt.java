package net.ifonlygaram.util;

import org.mindrot.jbcrypt.BCrypt;

public class PwdJbcrypt {
	public String pwdJbcrypt(String password) { return BCrypt.hashpw(password, BCrypt.gensalt()); }
	public boolean pwdCheck(String password, String hashed) { return BCrypt.checkpw(password, hashed); }
}
