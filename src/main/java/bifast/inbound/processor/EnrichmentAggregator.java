package bifast.inbound.processor;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentAggregator implements AggregationStrategy {
//	private static Logger logger = LoggerFactory.getLogger(EnrichmentAggregator.class);
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		oldExchange.getIn().setBody(newExchange.getIn().getBody());
		return oldExchange;
	}

}
