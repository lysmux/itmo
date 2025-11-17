//package dev.lysmux.lab4;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.webauthn4j.converter.jackson.WebAuthnJSONModule;
//import com.webauthn4j.converter.util.CborConverter;
//import com.webauthn4j.converter.util.JsonConverter;
//import com.webauthn4j.converter.util.ObjectConverter;
//import com.webauthn4j.util.J;
//import jakarta.ws.rs.ext.ContextResolver;
//import jakarta.ws.rs.ext.Provider;
//
//@Provider
//public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
//
//    private final ObjectMapper mapper;
//
//    public ObjectMapperResolver() {
//        // Используем JacksonUtil из webauthn4j-util — он уже регистрирует WebAuthn4jModule
//        this.mapper = new ObjectMapper();
//
//        ObjectConverter objectConverter = new ObjectConverter();
//        JsonConverter jsonConverter = objectConverter.getJsonConverter();
//        String registrationRequest  = jsonConverter.writeValueAsString(publicKeyCredentialCreationOptions);
//    }
//
//    @Override
//    public ObjectMapper getContext(Class<?> type) {
//        return mapper;
//    }
//}
