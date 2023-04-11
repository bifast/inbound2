package bifast.inbound.credittransfer.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bifast.inbound.model.CreditTransfer;
import bifast.inbound.pojo.ProcessDataPojo;
import bifast.inbound.pojo.flat.FlatPacs008Pojo;
import bifast.inbound.repository.CreditTransferRepository;

// ct_saf = NO, ct_cbsts="" 		--> bukan berupa SAF dari BI, tidak ada history komi
// ct_saf = NEW, ct_cbsts=""		--> SAF dari BI, tapi tidak ada datanya di history komi
// ct_saf = OLD, ct_cbsts="RJCT"	--> berupakan SAF, dan trns sebelumnya ditolak oleh KOMI
// ct_saf = OLD, ct_cbsts="ACTC"	--> berupakan SAF, dan trns sebelumnya diterima

@Component
public class CheckSAFStatusProcessor implements Processor{
	@Autowired private CreditTransferRepository ctRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		ProcessDataPojo processData = exchange.getProperty("prop_process_data", ProcessDataPojo.class);
		FlatPacs008Pojo flat = (FlatPacs008Pojo) processData.getBiRequestFlat();
		
		String saf = "NO";
		String cbSts = "";
		
		if ((null != flat.getCpyDplct()) && (flat.getCpyDplct().equals("DUPL"))) {
			saf = "NEW";
			List<CreditTransfer> lCT = ctRepo.findAllByCrdtTrnRequestBizMsgIdr(flat.getBizMsgIdr());
			for (CreditTransfer ct : lCT) {      // mungkin pernah terima ct beberapa kali dg bizmsgidr yg sama, cari yg bukan tolakan
				if (ct.getReasonCode().equals("U149"))
					continue;
				if (ct.getResponseCode().equals("ACTC")) {
					cbSts = "ACTC";
					saf = "OLD";
					break;
				}
				else {
					cbSts = "RJCT";
					saf = "OLD";
				}
			}
		}	
		
		exchange.getMessage().setHeader("ct_saf", saf);
		exchange.getMessage().setHeader("ct_cbsts", cbSts);
		exchange.setProperty("ct_saf", saf);
		exchange.getMessage().setBody(flat);
				
	}

	
}
