package smpp.utils;

import com.clct.models.ShortMessage;
import com.clct.models.ShortMessageBuilder;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.gsm.GsmUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.SmppInvalidArgumentException;
import org.apache.commons.lang3.RandomUtils;
import smpp.exceptions.SendSmsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luyenchu on 6/28/16.
 */
public final class ShortMessageConverter {
    public static final byte SOURCE_TON_NORMAL = (byte) 0x03;//0x01: normal, 0x03: normal, 0x05:brand name
    public static final byte SOURCE_TON_BRANDNAME = (byte) 0x05;

    private ShortMessageConverter() {
    }

    public static ShortMessage fromDeliverSm(DeliverSm deliverSm) {
        ShortMessageBuilder moBuilder = ShortMessageBuilder.aMO()
                .withSequenceNumber(deliverSm.getSequenceNumber())
                .withCreatedTime(new Date())
                .withSourceAddress(deliverSm.getSourceAddress().getAddress())
                .withDestAddress(deliverSm.getDestAddress().getAddress())
                .withServiceType(deliverSm.getServiceType())
                .withShortMessage(CharsetUtil.decode(deliverSm.getShortMessage(), CharsetUtil.NAME_UTF_8));
        return moBuilder.build();
    }

    /**
     * @param shortMessage
     * @return
     * @throws SendSmsException
     */
    public static SubmitSm toDefaultSubmitSm(ShortMessage shortMessage) throws SendSmsException {
        return toSubmitSm(shortMessage, SOURCE_TON_NORMAL);
    }

    /**
     * @param shortMessage
     * @param sourceTon
     * @return
     * @throws SendSmsException
     */
    public static SubmitSm toSubmitSm(ShortMessage shortMessage, byte sourceTon) throws SendSmsException {
        String text160 = shortMessage.getShortMessage();
        byte[] textBytes = CharsetUtil.encode(text160, CharsetUtil.CHARSET_GSM);

        SubmitSm submit = new SubmitSm();
        submit.setSourceAddress(new Address(sourceTon, (byte) 0x00, shortMessage.getSourceAddress()));
        submit.setDestAddress(new Address((byte) 0x01, (byte) 0x01, shortMessage.getDestAddress()));
        submit.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
        try {
            submit.setShortMessage(textBytes);
        } catch (SmppInvalidArgumentException e) {
            throw new SendSmsException("Error on creation of SubmitSm", e);
        }
        return submit;
    }

    /**
     * To send long message using UDH
     *
     * @param shortMessage
     * @param sourceTon    = SOURCE_TON_NORMAL = 0x03( or 0x01), or SOURCE_TON_BRANDNAME = 0x05
     * @return
     * @throws SendSmsException
     */
    public static List<SubmitSm> divMultipleSubmitSm(ShortMessage shortMessage, byte sourceTon) throws SendSmsException {
        byte[] textBytes = CharsetUtil.encode(shortMessage.getShortMessage(), CharsetUtil.CHARSET_GSM);
        byte[] refNum = RandomUtils.nextBytes(1);
        byte[][] msgParts = GsmUtil.createConcatenatedBinaryShortMessages(textBytes, refNum[0]);
        List<SubmitSm> submitSms = new ArrayList<>();
        for (int i = 0; i < msgParts.length; i++) {
            SubmitSm submit = new SubmitSm();
            submit.setSourceAddress(new Address(sourceTon, (byte) 0x00, shortMessage.getSourceAddress()));
            submit.setDestAddress(new Address((byte) 0x01, (byte) 0x01, shortMessage.getDestAddress()));
            submit.setEsmClass(SmppConstants.ESM_CLASS_UDHI_MASK);
            try {
                submit.setShortMessage(msgParts[i]);
            } catch (SmppInvalidArgumentException e) {
                throw new SendSmsException("Error on creation(divide) of SubmitSm", e);
            }
            submitSms.add(submit);
        }
        return submitSms;
    }

}
