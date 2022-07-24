package netPackets;

import com.google.gson.*;
import models.interfaces.EnumInterface;

import java.lang.reflect.Type;

public class JsonDeserializerWithInheritance<T> implements JsonDeserializer<T>, JsonSerializer<T> {
    @Override
    public T deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        //json.isJsonObject()
//        String enumString = json.getAsString();
//        String[] parts = enumString.split(" ");
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String enumString = json.getAsString();
            String[] parts = enumString.split(" ");
            if (parts[0].equals("Enum")) {
                Class<?> clazz;
                try {
                    clazz = Class.forName(parts[1]);
//                    System.out.println("found class: " + clazz.getName());
                    for (Object enumConstant : clazz.getEnumConstants()) {
                        String constantName = ((EnumInterface) enumConstant).getName();
//                        System.out.println(constantName);
                        if (parts[2].equals(constantName)) {
                            return (T) enumConstant;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new JsonParseException(e.getMessage());
                }
            } else {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");
                if (classNamePrimitive == null) {
                    Gson gson = new Gson();
                    return gson.fromJson(json, typeOfT);
                }
                String className = classNamePrimitive.getAsString();
                Class<?> clazz;
                try {
                    clazz = Class.forName(className);
//                    System.out.println("found class: " + clazz.getName());
                } catch (ClassNotFoundException e) {
                    throw new JsonParseException(e.getMessage());
                }
                return context.deserialize(jsonObject, clazz);
            }
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");
            if (classNamePrimitive == null) {
                Gson gson = new Gson();
                return gson.fromJson(json, typeOfT);
            }
            String className = classNamePrimitive.getAsString();
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
//                System.out.println("found class: " + clazz.getName());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
            return context.deserialize(jsonObject, clazz);
        }
        return null;
    }

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
//        System.out.println("serializing type: " + type.getTypeName());
//        System.out.flush();
        return jsonSerializationContext.serialize(t);
    }
//            JsonElement json, Type typeOfT, JsonDeserializationContext context)
//            throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");
//
//        String className = classNamePrimitive.getAsString();
//
//        Class<?> clazz;
//        try {
//            clazz = Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            throw new JsonParseException(e.getMessage());
//        }
//        return context.deserialize(jsonObject, clazz);
    //}
}
