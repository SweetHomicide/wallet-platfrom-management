package test.wallet.platfrom.project;

import org.junit.Test;

import com.wallet.platform.cfos.CfosRpc;
import com.wallet.platform.cfos.exception.ConnectException;

public class TestBitcoin {
	
	@Test
	public void test() throws ConnectException, Exception {
		CfosRpc rpc = CfosRpc.getRpc("http://127.0.0.1:20000/", "otc", "otc123");
		System.out.println(rpc.getBlockCount());
	}
	
}
