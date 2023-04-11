package bifast.inbound.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransRef {

    private static int seq = 0;

    private synchronized static int nextSeq() {
        seq++;
        if (seq > 99999) seq = 1;
        return seq;
    }

    public static class Ref {
        private String dateTime;
        private String noRef;

        public String getDateTime() {
            return dateTime;
        }

        public String getNoRef() {
            return noRef;
        }
    }
    
    public static Ref newRef() {
        DateTimeFormatter fdt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateTimeFormatter fno = DateTimeFormatter.ofPattern("'KOM'yyMMddHHmmss");
        DecimalFormat fseq = new DecimalFormat("00000");
        
        LocalDateTime dt = LocalDateTime.now();

        Ref r = new Ref();
        r.dateTime = dt.format(fdt);
        r.noRef = dt.format(fno) + fseq.format(nextSeq());
        return r;
    }

//    public static Ref setRef(String noRef) {
//        Ref r = new Ref();
//        r.noRef = noRef;
//        r.dateTime = dateTimeFromNoRef(noRef);
//        return r;
//    }

//    public static Ref copyRef(String noRef, String dateTime) {
//        Ref r = new Ref();
//        r.noRef = noRef;
//        r.dateTime = dateTime;
//        return r;
//    }

//    public static String dateTimeFromNoRef(String noRef) {
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
//    }

}
