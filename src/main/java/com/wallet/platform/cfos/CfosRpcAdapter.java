package com.wallet.platform.cfos;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wallet.platform.cfos.exception.CfosInnerException;
import com.wallet.platform.cfos.exception.ConnectException;

class CfosRpcAdapter implements ICfosRpcInterface {

	public CfosRpcAdapter(String server, String user, String password) throws ConnectException, MalformedURLException {
		url = new URL(server);
		authorization = "Basic " + Base64.encode(new String(user + ":" + password).getBytes());
	}

	@Override
	public CfosRpcResult getBlockCount() throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_BLOCK_COUNT));
	}

	@Override
	public CfosRpcResult getBlockHash(Long blockIndex) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_BLOCK_HASH, blockIndex));
	}

	@Override
	public CfosRpcResult getBlock(String blockHash) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_BLOCK, blockHash));
	}

	@Override
	public CfosRpcResult getTransaction(String txid) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_TRANSACTION, txid));
	}

	@Override
	public CfosRpcResult sendAsset2Address(String fromAssetAddress, String toAssetAddress, BigDecimal amount, String cashFeeAddress, String assetChangeAddress, String cashFeeChangeAddress)
			throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.SEND_ASSET_TO_ADDRESS, fromAssetAddress, toAssetAddress, amount, cashFeeAddress, cashFeeChangeAddress, assetChangeAddress));
	}
	
	@Override
	public CfosRpcResult getNewAddress(String symbol, String account) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_NEW_ADDRESS, symbol, account));
	}
	
	@Override
	public CfosRpcResult getNewAddress(String account) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_NEW_ADDRESS, account));
	}

	@Override
	public CfosRpcResult getAddressBalance(String adddress) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_ADDRESS_BALANCE, adddress));
	}
	
	@Override
	public CfosRpcResult getBitcoinBalance(String account) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.GET_BITCOIN_BALANCE, account));
	}
	
	@Override
	public CfosRpcResult sendBitcoin(String account, String address, BigDecimal amount) throws ConnectException, CfosInnerException, Exception {
		return doPost(buildParam(CfosMethod.SEND_BITCOIN, account, address, amount));
	}
	
	private String buildParam(String method, Object ... params) {
		if (null == params || params.length == 0) {
			return new StringBuffer("{\"jsonrpc\":\"1.0\",\"id\":\"1\",\"method\":\"").append(method).append("\"}").toString();
		}
		StringBuffer buf = new StringBuffer("{\"jsonrpc\":\"1.0\",\"id\":\"1\",\"method\":\"").append(method);
		buf.append("\",\"params\":[");
		for (int i = 0; i < params.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			if (null != params[i]) {
				if (params[i] instanceof String) {
					buf.append("\"").append(params[i]).append("\"");
				} else if (params[i] instanceof BigDecimal || params[i] instanceof Double || params[i] instanceof Float) {
					buf.append(String.format("%.8f", params[i]));
				} else {
					buf.append(String.valueOf(params[i]));
				}
			} else {
				buf.append("\"\"");
			}
		}
		buf.append("]}");
		return buf.toString();
	}
	
	private CfosRpcResult doPost(String param) throws ConnectException, CfosInnerException {
		OutputStream os = null;
		OutputStreamWriter osw = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Accept-Charset", charset);
			httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
			httpURLConnection.setRequestProperty("Authorization", authorization);
			httpURLConnection.setRequestMethod(POST);
			httpURLConnection.setConnectTimeout(30 * 60 * 000); // 超时时间30分钟
			httpURLConnection.setReadTimeout(31 * 60 * 000);
			
			if (null != param && param.length() > 0) {
				httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			}
			
			httpURLConnection.setDoOutput(true);
			osw = new OutputStreamWriter(os = httpURLConnection.getOutputStream(), charset);
			osw.write(param);
			osw.flush();
			
			int code = httpURLConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == code) {
				is = httpURLConnection.getInputStream();
			} else {
				is = httpURLConnection.getErrorStream();
			}
			if (null == is) {
				return new CfosRpcResult(code, null);
			}
			return new CfosRpcResult(code, read(br = new BufferedReader(isr = new InputStreamReader(is, charset))));
		} catch (java.net.ConnectException e) {
			throw new ConnectException("无法连接到服务器。[host] -> " + url.getHost() + ", [port] -> " + url.getPort(), e);
		} catch (IOException e) {
			throw new ConnectException(e.getMessage(), e);
		} finally {
			close(osw, os, br, isr, is);
		}
	}
	
	private static String read(BufferedReader br) throws IOException {
		StringBuffer buf = new StringBuffer();
		for (String line = br.readLine(); null != line; line = br.readLine()) {
			buf.append(line);
		}
		return buf.toString();
	}
	
	private void close(Closeable ... closeables) {
		if (null != closeables && closeables.length > 0) {
			for (int i = 0; i < closeables.length; ++i) {
				if (null != closeables[i]) {
					try {
						closeables[i].close();
					} catch (Exception e) { }
				}
			}
		}
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	
	private static final String POST = "POST";
	
	private static final String CONTENT_TYPE = "application/json-rpc";

	private String charset = "UTF-8";
	
	private URL url;
	
	private String authorization;

}

class Base64 {

	static private final int BASELENGTH = 255;
	static private final int LOOKUPLENGTH = 64;
	static private final int TWENTYFOURBITGROUP = 24;
	static private final int EIGHTBIT = 8;
	static private final int SIXTEENBIT = 16;
	static private final int FOURBYTE = 4;
	static private final int SIGN = -128;
	static private final char PAD = '=';
	static final private byte[] base64Alphabet = new byte[BASELENGTH];
	static final private char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];

	static {
		for (int i = 0; i < BASELENGTH; i++) {
			base64Alphabet[i] = -1;
		}
		for (int i = 'Z'; i >= 'A'; i--) {
			base64Alphabet[i] = (byte) (i - 'A');
		}
		for (int i = 'z'; i >= 'a'; i--) {
			base64Alphabet[i] = (byte) (i - 'a' + 26);
		}
		for (int i = '9'; i >= '0'; i--) {
			base64Alphabet[i] = (byte) (i - '0' + 52);
		}
		base64Alphabet['+'] = 62;
		base64Alphabet['/'] = 63;

		for (int i = 0; i <= 25; i++) {
			lookUpBase64Alphabet[i] = (char) ('A' + i);
		}

		for (int i = 26, j = 0; i <= 51; i++, j++) {
			lookUpBase64Alphabet[i] = (char) ('a' + j);
		}

		for (int i = 52, j = 0; i <= 61; i++, j++) {
			lookUpBase64Alphabet[i] = (char) ('0' + j);
		}
		lookUpBase64Alphabet[62] = (char) '+';
		lookUpBase64Alphabet[63] = (char) '/';
	}

	protected static boolean isWhiteSpace(char octect) {
		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	}

	protected static boolean isPad(char octect) {
		return (octect == PAD);
	}

	protected static boolean isData(char octect) {
		return (base64Alphabet[octect] != -1);
	}

	protected static boolean isBase64(char octect) {
		return (isWhiteSpace(octect) || isPad(octect) || isData(octect));
	}

	/**
	 * Encodes hex octects into Base64
	 * 
	 * @param binaryData
	 *            Array containing binaryData
	 * @return Encoded Base64 array
	 */
	public static String encode(byte[] binaryData) {

		if (binaryData == null) {
			return null;
		}

		int lengthDataBits = binaryData.length * EIGHTBIT;
		if (lengthDataBits == 0) {
			return "";
		}

		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		int numberQuartet = (fewerThan24bits != 0) ? numberTriplets + 1 : numberTriplets;
		int numberLines = (numberQuartet - 1) / 19 + 1;
		char encodedData[] = null;

		encodedData = new char[numberQuartet * 4 + numberLines];

		byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;
		int i = 0;

		for (int line = 0; line < numberLines - 1; line++) {
			for (int quartet = 0; quartet < 19; quartet++) {
				b1 = binaryData[dataIndex++];
				b2 = binaryData[dataIndex++];
				b3 = binaryData[dataIndex++];

				l = (byte) (b2 & 0x0f);
				k = (byte) (b1 & 0x03);

				byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
				byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
				byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

				encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];

				i++;
			}
			encodedData[encodedIndex++] = 0xa;
		}

		for (; i < numberTriplets; i++) {
			b1 = binaryData[dataIndex++];
			b2 = binaryData[dataIndex++];
			b3 = binaryData[dataIndex++];

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
			encodedData[encodedIndex++] = PAD;
			encodedData[encodedIndex++] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
			encodedData[encodedIndex++] = PAD;
		}

		encodedData[encodedIndex] = 0xa;

		return new String(encodedData).replace("\n", "");
	}

	/**
	 * Decodes Base64 data into octects
	 * 
	 * @param binaryData
	 *            Byte array containing Base64 data
	 * @return
	 */
	public static byte[] decode(String encoded) {
		if (encoded == null) {
			return null;
		}
		char[] base64Data = encoded.toCharArray();
		// remove white spaces
		int len = removeWhiteSpace(base64Data);

		if (len % FOURBYTE != 0) {
			return null; // should be divisible by four
		}

		int numberQuadruple = (len / FOURBYTE);

		if (numberQuadruple == 0) {
			return new byte[0];
		}

		byte decodedData[] = null;
		byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
		char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

		int i = 0;
		int encodedIndex = 0;
		int dataIndex = 0;
		decodedData = new byte[(numberQuadruple) * 3];

		for (; i < numberQuadruple - 1; i++) {

			if (!isData((d1 = base64Data[dataIndex++]))
					|| !isData((d2 = base64Data[dataIndex++]))
					|| !isData((d3 = base64Data[dataIndex++]))
					|| !isData((d4 = base64Data[dataIndex++]))) {
				return null; // if found "no data" just return null
			}
			
			b1 = base64Alphabet[d1];
			b2 = base64Alphabet[d2];
			b3 = base64Alphabet[d3];
			b4 = base64Alphabet[d4];

			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
		}

		if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
			return null; // if found "no data" just return null
		}

		b1 = base64Alphabet[d1];
		b2 = base64Alphabet[d2];

		d3 = base64Data[dataIndex++];
		d4 = base64Data[dataIndex++];
		if (!isData((d3)) || !isData((d4))) { // Check if they are PAD characters
			if (isPad(d3) && isPad(d4)) { // Two PAD e.g. 3c[Pad][Pad]
				if ((b2 & 0xf) != 0) { // last 4 bits should be zero
					return null;
				}
				byte[] tmp = new byte[i * 3 + 1];
				System.arraycopy(decodedData, 0, tmp, 0, i * 3);
				tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				return tmp;
			} else if (!isPad(d3) && isPad(d4)) { // One PAD e.g. 3cQ[Pad]
				b3 = base64Alphabet[d3];
				if ((b3 & 0x3) != 0) {// last 2 bits should be zero
					return null;
				}
				byte[] tmp = new byte[i * 3 + 2];
				System.arraycopy(decodedData, 0, tmp, 0, i * 3);
				tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
				tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				return tmp;
			} else {
				return null; // an error like "3c[Pad]r", "3cdX", "3cXd", "3cXX"
				// where X is non data
			}
		} else { // No PAD e.g 3cQl
			b3 = base64Alphabet[d3];
			b4 = base64Alphabet[d4];
			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
		}
		return decodedData;
	}

	/**
	 * remove WhiteSpace from MIME containing encoded Base64 data.
	 * 
	 * @param data
	 *            the byte array of base64 data (with WS)
	 * @return the new length
	 */
	protected static int removeWhiteSpace(char[] data) {
		if (data == null) {
			return 0;
		}

		// count characters that's not whitespace
		int newSize = 0;
		int len = data.length;
		for (int i = 0; i < len; i++) {
			if (!isWhiteSpace(data[i])) {
				data[newSize++] = data[i];
			}
		}
		return newSize;
	}
}
