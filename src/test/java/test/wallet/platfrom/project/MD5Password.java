package test.wallet.platfrom.project;

import com.wallet.platform.util.MD5;

public class MD5Password {

	public static void main(String[] args) {
		System.out.println(MD5.getMD5("manager123", true));
	}
}
