package io.apiorchestrationservice.testhelpers.apiexecutor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class DynamicPortHolder {
	private static int port;

	public static int getPort(){
		return port;
	}

	public static void setPort(int port){
		DynamicPortHolder.port = port;
	}
}
