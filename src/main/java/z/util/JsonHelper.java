package z.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json转换工具
 * 
 * @see @JsonIgnoreProperties(ignoreUnknown = true)
 * @author jianming.zhou
 *
 */
public class JsonHelper {

	private static ObjectMapper mapper = new ObjectMapper();
	
	public static ObjectMapper getMapper() {
		return mapper;
	}
}
