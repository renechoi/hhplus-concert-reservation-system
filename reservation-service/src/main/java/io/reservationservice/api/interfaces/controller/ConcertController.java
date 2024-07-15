package io.reservationservice.api.interfaces.controller;

import static io.reservationservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reservationservice.api.application.dto.request.ConcertCreateRequest;
import io.reservationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.reservationservice.api.application.dto.response.ConcertCreateResponse;
import io.reservationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.reservationservice.api.application.facade.ConcertCrudFacade;
import io.reservationservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
@Tag(name = "콘서트 및 콘서트 옵션 API")
public class ConcertController {

	private final ConcertCrudFacade facade;

	@PostMapping
	@Operation(summary = "콘서트 생성")
	public CommonApiResponse<ConcertCreateResponse> createConcert(@RequestBody ConcertCreateRequest request) {
		return created(facade.createConcert(request));
	}

	@PostMapping("/{concertId}/options")
	@Operation(summary = "콘서트 옵션 생성")
	public CommonApiResponse<ConcertOptionCreateResponse> createConcertOption(@PathVariable Long concertId, @RequestBody @Validated ConcertOptionCreateRequest request) {
		return created(facade.createConcertOption(concertId, request));
	}

}
