package z.sky.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 脚本执行工具
 * @author jianming.zhou
 *
 */
public class ScriptUtil {

	private static Logger LOG = LoggerFactory.getLogger(ScriptUtil.class);

	/**
	 * 执行脚本(通用)
	 * @param cmdarray
	 * @param separator
	 * @throws InterruptedException
	 */
	public static void exec(String[] cmdarray, String separator) throws InterruptedException {
		if (cmdarray == null || cmdarray.length == 0) {
			LOG.warn("执行参数不合法！");
			return;
		}
		try {
			Process ps = Runtime.getRuntime().exec(cmdarray);
			ps.waitFor();

			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuffer shellLog = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				shellLog.append(line).append(separator);
			}
			LOG.debug(shellLog.toString());
		} catch (InterruptedException e) {
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 执行Shell
	 * @param file
	 * @param args
	 * @param separator
	 * @throws InterruptedException
	 */
	public static void exec4Shell(String file, String[] args, String separator) throws InterruptedException {
		if (file == null) {
			LOG.warn("执行参数不合法！");
			return;
		}
		String[] cmdarray = args != null ? new String[args.length + 2] : new String[2];
		cmdarray[0] = "bash";
		cmdarray[1] = file;
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				cmdarray[i + 2] = args[i];
			}
		}
		exec(cmdarray, separator);
	}
	
	/**
	 * 执行Python
	 * @param file
	 * @param args
	 * @param separator
	 * @throws InterruptedException
	 */
	public static void exec4Python(String file, String[] args, String separator) throws InterruptedException {
		if (file == null) {
			LOG.warn("执行参数不合法！");
			return;
		}
		String[] cmdarray = args != null ? new String[args.length + 2] : new String[2];
		cmdarray[0] = "python";
		cmdarray[1] = file;
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				cmdarray[i + 2] = args[i];
			}
		}
		exec(cmdarray, separator);
	}
	
	/**
	 * 远程Shell——Expect
	 * @param file
	 * @param args
	 * @param separator
	 * @throws InterruptedException
	 */
	public static void exec4Expect(String file, String[] args, String separator) throws InterruptedException {
		if (file == null || args == null || args.length < 3) {
			LOG.warn("执行参数不合法！");
			return;
		}
		String[] cmdarray = new String[args.length + 2];
		cmdarray[0] = "expect";
		cmdarray[1] = file;
		for (int i = 0; i < args.length; i++) {
			cmdarray[i + 2] = args[i];
		}
		exec(cmdarray, separator);
	}
	
	/**
	 * 远程Shell——Fabric
	 * @param file
	 * @param args
	 * @param separator
	 * @throws InterruptedException
	 */
	public static void exec4Fabric(String file, String[] args, String separator) throws InterruptedException {
		if (file == null || args == null || args.length < 1) {
			LOG.warn("执行参数不合法！");
			return;
		}
		String[] cmdarray = new String[args.length + 3];
		cmdarray[0] = "/usr/local/bin/fab";
		cmdarray[1] = "-f";
		cmdarray[2] = file;
		for (int i = 0; i < args.length; i++) {
			cmdarray[i + 3] = args[i];
		}
		exec(cmdarray, separator);
	}
}
