package com.websocket.publisher.stockprices.domain.eapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockPriceResponse {
    private String stockCode;
    private String stockPrice;
}
