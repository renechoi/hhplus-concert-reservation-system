package io.paymentservice.api.balance.interfaces.controller;

import static io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand.*;
import static io.paymentservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.paymentservice.api.balance.business.service.impl.BalanceService;
import io.paymentservice.api.balance.interfaces.dto.request.BalanceChargeRequest;
import io.paymentservice.api.balance.interfaces.dto.request.BalanceUseRequest;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceChargeResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceSearchResponse;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceUseResponse;
import io.paymentservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@RestController
@RequestMapping("/api/user-balance")
@RequiredArgsConstructor
@Tag(name = "잔액 충전 API")
public class BalanceController {


	private final BalanceService service;


	/**
	 * 특정 유저의 잔액을 조회하는 기능
	 */
	@GetMapping("/{userId}")
	@Operation(summary = "잔액 조회")
	public CommonApiResponse<BalanceSearchResponse> balanceSearch(@PathVariable long userId) {
		return OK(BalanceSearchResponse.from(service.search(searchCommandById(userId))));
	}

	/**
	 * 특정 유저의 잔액 충전/이용 내역을 조회하는 기능
	 */
	@GetMapping("/histories/{userId}")
	@Operation(summary = "잔액 충전/사용 이용 내역 조회")
	public CommonApiResponse<BalanceTransactionResponses> history(@PathVariable long userId) {
		return OK(BalanceTransactionResponses.from(service.getHistories(userId)));
	}

	/**
	 * 특정 유저의 잔액을 충전하는 기능
	 */
	@PutMapping("/charge/{userId}")
	@Operation(summary = "잔액 충전")
	public CommonApiResponse<BalanceChargeResponse> charge(@PathVariable long userId, @RequestBody @Validated BalanceChargeRequest userPointChargeRequest) {
		return OK(BalanceChargeResponse.from(service.charge(userPointChargeRequest.withUserId(userId).toCommand())));
	}

	/**
	 * 특정 유저의 잔액을 사용하는 기능
	 */
	@PutMapping("/use/{userId}")
	@Operation(summary = "잔액 사용")
	public CommonApiResponse<BalanceUseResponse> use(@PathVariable long userId, @RequestBody @Validated BalanceUseRequest userPointUseRequest) {
		return OK(BalanceUseResponse.from(service.use(userPointUseRequest.withUserId(userId).toCommand())));
	}
}
