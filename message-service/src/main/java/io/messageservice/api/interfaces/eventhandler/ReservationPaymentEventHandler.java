package io.messageservice.api.interfaces.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.messageservice.api.interfaces.stream.payload.PaymentMessagePayload;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
@Component
@RequiredArgsConstructor
public class ReservationPaymentEventHandler {

	/**
	 * 결제 완료 이벤트를 수신합니다.
	 * 향후 니즈에 따라, 해당 유저에게 알림 메시지를 전송할 수 있습니다.
	 * @param event
	 */
	@EventListener
	public void handlePaymentCompleteEvent(PaymentMessagePayload event) {
	}

	/**
	 * 결제 취소 이벤트를 수신합니다.
	 * 향후 니즈에 따라, 해당 유저에게 알림 메시지를 전송할 수 있습니다.
	 * @param event
	 */
	@EventListener
	public void handlePaymentCancelEvent(PaymentMessagePayload event) {
	}
}
