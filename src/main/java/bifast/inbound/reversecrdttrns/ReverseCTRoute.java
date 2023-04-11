package bifast.inbound.reversecrdttrns;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.reversecrdttrns.processor.CheckDebitHistoryProc;
import bifast.inbound.reversecrdttrns.processor.RevCTAcceptedProcessor;
import bifast.inbound.reversecrdttrns.processor.RevCTRejectResponseProcessor;
import bifast.inbound.reversecrdttrns.processor.SaveRevCTProc;

@Component
public class ReverseCTRoute extends RouteBuilder {
	@Autowired private CheckDebitHistoryProc checkDebitHistoryProcessor;
	@Autowired private RevCTAcceptedProcessor rctAccpProcessor;
	@Autowired private RevCTRejectResponseProcessor rejectResponsePrc;
	@Autowired private SaveRevCTProc saveRevCTProc;

	@Override
	public void configure() throws Exception {

		from("direct:reverct").routeId("komi.reversect")
		
			// cari original transaksi 
			.process(checkDebitHistoryProcessor)

			.filter().simple("${exchangeProperty.pr_revCTCheckRsl} != 'DataMatch' ")
				.log("[${exchangeProperty.msgName}:${exchangeProperty.prop_process_data.endToEndId}] CT asal tidak ketemu atau tidak sesuai.")
				.process(rejectResponsePrc)
				.wireTap("direct:save_rct")
			.end()
			.end()  // entah kenapa harus end() 2x baru bisa lolos ke baris selanjutnya
			
			.filter(exchangeProperty("pr_revCTCheckRsl").isEqualTo("DataMatch"))

			.process(rctAccpProcessor)
			.wireTap("direct:save_rct")
		;

		
		from("direct:save_rct").routeId("komi.saverevct").log("saveRevCTTable").process(saveRevCTProc);
	
	}
}
