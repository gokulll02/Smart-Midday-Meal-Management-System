package digitalmidday;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SMSUtil {

    public static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
    public static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";

    public static void sendSMS(String item, double qty){

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
            new com.twilio.type.PhoneNumber("your_mobile_number"),
            new com.twilio.type.PhoneNumber("Twilio_account_number"), 
            "⚠ LOW STOCK ALERT!\nItem: " + item +
            "\nAvailable: " + qty
        ).create();

        System.out.println("SMS Sent: " + message.getSid());
    }
}