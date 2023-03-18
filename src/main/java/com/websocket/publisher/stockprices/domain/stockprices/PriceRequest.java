package com.websocket.publisher.stockprices.domain.stockprices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceRequest {
    private String stockCode;
}
