package bifast.inbound.credittransfer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.accountenquiry.IsoAERequestPrc;
import bifast.inbound.credittransfer.processor.CTProcessor;
import bifast.inbound.credittransfer.processor.CheckSAFStatusProcessor;
import bifast.inbound.credittransfer.processor.SaveCreditTransferProcessor;
import bifast.inbound.processor.DuplicateTransactionValidation;
import bifast.inbound.service.JacksonDataFormatService;
import bifast.library.iso20022.custom.BusinessMessage;

@Component
public class CreditTransferRoute extends RouteBuilder {
	@Autowired private CheckSAFStatusProcessor checkSafStatus;
	@Autowired private CTProcessor creditTransferProcessor;
	@Autowired private DuplicateTransactionValidation duplicationTrnsValidation;
	@Autowired private JacksonDataFormatService jdfService;
	@Autowired private SaveCreditTransferProcessor saveCreditTransferProcessor;
	@Autowired private IsoAERequestPrc isoAERequestPrc;

	@Override
	public void configure() throws Exception {
		JacksonDataFormat businessMessageJDF = jdfService.wrapRoot(BusinessMessage.class);
		
		onException(Exception.class).routeId("komi.ct.excp")
			.log(LoggingLevel.ERROR, "${exception.stacktrace}")
			.marshal(businessMessageJDF)
			.log(LoggingLevel.DEBUG,"komi.ct","${body}")
			.handled(true)
			.wireTap("direct:save_ct")
			.removeHeaders("*")
		;
		
		from("direct:crdttransfer").routeId("komi.ct")
			.process(duplicationTrnsValidation)
			
			// cek apakah SAF atau bukan
			// check saf
			.process(checkSafStatus)
			
			.log(LoggingLevel.DEBUG,"komi.ct", 
					"[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Status SAF: ${header.ct_saf}")
			
			// if SAF = NO/NEW --> call CB, set CBSTS = ACTC/RJCT
			.filter().simple("${header.ct_saf} in 'NO,NEW'")

				// proses sebagai AE request
				.process(isoAERequestPrc)
//				.to("direct:isoadpt")
				.to("direct:cb_ae")
				
				.setHeader("ct_cbsts", simple("${body.status}"))

			.end()
					
			.process(creditTransferProcessor)
			
			.log(LoggingLevel.DEBUG, "komi.ct", 
					"[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] saf ${header.ct_saf}, cb_sts ${header.ct_cbsts}")

			//if SAF=old/new and CBSTS=RJCT --> reversal
			//jika saf dan corebank error, ct proses harus diulang: reversal = UNDEFINED
			.filter().simple("${header.ct_saf} != 'NO'")
				.choice()
					.when().simple("${header.ct_cbsts} == 'RJCT'")
						.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Harus CT Reversal.")
						.setHeader("hdr_reversal", constant("YES"))
					.endChoice()
					.when().simple("${header.ct_cbsts} == 'ERROR'")
						.log("[${exchangeProperty.prop_process_data.inbMsgName}:${exchangeProperty.prop_process_data.endToEndId}] Apakah CT Reversal?")
						.setHeader("hdr_reversal", constant("UNDEFINED"))
					.endChoice()
				.end()
			.end()
	
			.wireTap("direct:save_ct")
		
			.removeHeaders("ct_*")

		;
	
		from("direct:save_ct").routeId("savect").process(saveCreditTransferProcessor);

	}
}
