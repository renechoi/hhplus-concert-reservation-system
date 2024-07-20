package io.paymentservice.api.balance.interfaces.controller;

import static io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand.*;
import static io.paymentservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.paymentservice.api.balance.business.service.impl.UserBalanceService;
import io.paymentservice.api.balance.interfaces.dto.request.UserBalanceChargeRequest;
import io.paymentservice.api.balance.interfaces.dto.request.UserBalanceUseRequest;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceChargeResponse;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceSearchResponse;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceUseResponse;
import io.paymentservice.common.model.CommonApiResponse;
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
public class UserBalanceController {


	private final UserBalanceService service;


	/**
	 * 특정 유저의 잔액을 조회하는 기능
	 */
	@GetMapping("/{userId}")
	public CommonApiResponse<UserBalanceSearchResponse> balanceSearch(@PathVariable long userId) {
		return OK(UserBalanceSearchResponse.from(service.search(searchCommandById(userId))));
	}

	/**
	 * 특정 유저의 잔액 충전/이용 내역을 조회하는 기능
	 */
	@GetMapping("/histories/{userId}")
	public CommonApiResponse<BalanceTransactionResponses> history(@PathVariable long userId) {
		return OK(BalanceTransactionResponses.from(service.getHistories(userId)));
	}

	/**
	 * 특정 유저의 잔액을 충전하는 기능
	 */
	@PutMapping("/charge/{userId}")
	public CommonApiResponse<UserBalanceChargeResponse> charge(@PathVariable long userId, @RequestBody @Validated UserBalanceChargeRequest userPointChargeRequest) {
		return OK(UserBalanceChargeResponse.from(service.charge(userPointChargeRequest.withUserId(userId).toCommand())));
	}

	/**
	 * 특정 유저의 잔액을 사용하는 기능
	 */
	@PutMapping("/use/{userId}")
	public CommonApiResponse<UserBalanceUseResponse> use(@PathVariable long userId, @RequestBody @Validated UserBalanceUseRequest userPointUseRequest) {
		return OK(UserBalanceUseResponse.from(service.use(userPointUseRequest.withUserId(userId).toCommand())));
	}
}
