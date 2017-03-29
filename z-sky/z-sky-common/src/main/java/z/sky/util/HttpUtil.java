package z.sky.util;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpUtil {

	/**
	 * 支持https
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static HttpClient getHttpClient() throws NoSuchAlgorithmException {
		final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(),
				NoopHostnameVerifier.INSTANCE);
		
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslsf)
				.build();
		
		final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		cm.setMaxTotal(100);
		HttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.setConnectionManager(cm)
				.build();
		return httpClient;
	}
}
