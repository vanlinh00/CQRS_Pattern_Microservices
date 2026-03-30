package go.seni.java.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import go.seni.java.exception.CustomOAuth2Exception;

import java.io.IOException;
import java.util.Map;

public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    public CustomOAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeBooleanField("success", value.isSuccess());
        Map<String, String> additionalInfo = value.getAdditionalInformation();
        if (additionalInfo != null) {
            for (Map.Entry<String, String> entry : additionalInfo.entrySet()) {
                gen.writeStringField(entry.getKey(), entry.getValue());
            }
        }
        gen.writeEndObject();
    }
}