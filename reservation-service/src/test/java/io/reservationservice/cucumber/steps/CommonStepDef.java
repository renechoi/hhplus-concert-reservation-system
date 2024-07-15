package io.reservationservice.cucumber.steps;

import io.cucumber.java8.En;

/**
 * @author : Rene Choi
 * @since : 2024/06/26
 */
public class CommonStepDef implements En {

	public CommonStepDef() {

		And("스텝 구분을 위한 딜레이 {double}초를 기다린다", this::waitDelayForStepSeparationForDouble);

	}

	private void waitDelayForStepSeparationForDouble(double delay) {
		waitForDelay((long) (delay * 1000L)); // double 값을 초 단위에서 밀리초로 변환하여 처리
	}

	private void waitForDelay(long delayInMillis) {
		try {
			Thread.sleep(delayInMillis); // 밀리초 단위의 딜레이 적용
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}