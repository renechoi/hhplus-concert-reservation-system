package io.messageservice.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 설정 파일 로더. 설정 파일을 로드하고 관련 데이터를 관리합니다.
 * 싱글톤 패턴을 사용하여 인스턴스를 관리합니다.
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class YmlLoader {

	private static YmlLoader instance;
	private Map<String, String> configMap;
	private Map<String, String> backupConfigMap;
	private final String contextPath;




	/**
	 * 싱글톤 인스턴스를 반환합니다.
	 *
	 * @return YmlLoader의 인스턴스
	 */
	public static YmlLoader ymlLoader() {
		if (instance == null) {
			synchronized (YmlLoader.class) {
				if (instance == null) {
					instance = new YmlLoader();
				}
			}
		}
		return instance;
	}

	/**
	 * 생성자. application.yml 파일에서 설정을 로드합니다.
	 */
	private YmlLoader() {
		this.configMap = new HashMap<>();
		try {
			this.configMap = loadYml("src/main/resources/application.yml");
		} catch (IOException e) {
			try {
				this.configMap = loadYml("message-service/src/main/resources/application.yml");
			} catch (IOException ex) {
				throw new RuntimeException("Failed to load configuration from both default and fallback paths", ex);
			}
		}
		this.contextPath = this.configMap.getOrDefault("server.servlet.context-path", "");
	}


	public static String getConfigMap(String key) {
		YmlLoader loader = YmlLoader.ymlLoader();
		return loader.configMap.getOrDefault(key, null);
	}

	public static Map<String, String>  getConfigMap() {
		YmlLoader loader = YmlLoader.ymlLoader();
		return loader.configMap;
	}


	/**
	 * application.yml 파일에서 설정을 로드합니다.
	 *
	 * @param filePath 설정 파일의 경로
	 * @return 설정 맵
	 * @throws IOException 파일 읽기 실패 시 발생
	 */
	private static Map<String, String> loadYml(String filePath) throws IOException {
		Map<String, String> configMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String currentPath = "";
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
					int indentLevel = getIndentLevel(line);
					String[] parts = line.trim().split(":", 2);
					if (parts.length == 2) {
						currentPath = updateCurrentPath(currentPath, indentLevel, parts[0].trim());
						String key = currentPath;
						String value = parts[1].trim();
						configMap.put(key, value);
					}
				}
			}
		}
		return configMap;
	}

	/**
	 * 주어진 라인의 들여쓰기 레벨을 계산합니다.
	 *
	 * @param line 대상 문자열
	 * @return 들여쓰기 레벨
	 */
	private static int getIndentLevel(String line) {
		int indent = 0;
		while (line.charAt(indent) == ' ') {
			indent++;
		}
		return indent / 2;
	}

	/**
	 * 현재 경로를 업데이트합니다.
	 *
	 * @param currentPath 현재 경로
	 * @param indentLevel 들여쓰기 레벨
	 * @param newSegment  새 세그먼트
	 * @return 업데이트된 경로
	 */
	private static String updateCurrentPath(String currentPath, int indentLevel, String newSegment) {
		String[] pathSegments = currentPath.split("\\.");
		StringBuilder newPath = new StringBuilder();
		for (int i = 0; i < indentLevel && i < pathSegments.length; i++) {
			newPath.append(pathSegments[i]).append(".");
		}
		newPath.append(newSegment);
		return newPath.toString();
	}

	/**
	 * contextPath를 반환합니다.
	 *
	 * @return contextPath
	 */
	public String getContextPath() {
		return this.contextPath;
	}



	public static long getSchedulerFixedRate(String key) {
		YmlLoader loader = YmlLoader.ymlLoader();
		return Long.parseLong(loader.configMap.getOrDefault(key, "1000"));
	}

	public boolean isLoggingFilterEnabled() {
		return Boolean.parseBoolean(this.configMap.getOrDefault("custom-logging-filter.enabled", "false"));
	}

	public void setLoggingFilterEnabled(boolean enabled) {
		this.configMap.put("custom-logging-filter.enabled", String.valueOf(enabled));
	}

	public void backup() {
		this.backupConfigMap = new HashMap<>(this.configMap);
	}

	public void restore() {
		if (this.backupConfigMap!=null){
			this.configMap = new HashMap<>(this.backupConfigMap);
			this.backupConfigMap = null;
		}
	}


}
