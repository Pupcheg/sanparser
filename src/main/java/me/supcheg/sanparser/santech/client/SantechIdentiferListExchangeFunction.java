package me.supcheg.sanparser.santech.client;

import me.supcheg.sanparser.santech.SantechIdentifier;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;

import java.util.List;

public interface SantechIdentiferListExchangeFunction extends ExchangeFunction<List<SantechIdentifier>> {
}
