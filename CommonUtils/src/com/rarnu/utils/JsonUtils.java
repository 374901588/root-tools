package com.rarnu.utils;

import android.util.Log;
import com.rarnu.utils.common.JsonNode;
import com.rarnu.utils.common.JsonNode.FieldType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils<T> {

    private JsonNode root;
    private Class<T> classType;
    private Class<T> innerClassType;

    public JsonUtils(Class<T> classType, JsonNode root) {
        this.classType = classType;
        this.root = root;
    }

    public JsonUtils(Class<T> classType, Class<T> innerClassType, JsonNode root) {
        this(classType, root);
        this.innerClassType = innerClassType;
    }

    public String toJson(T obj) throws Exception {
        String retStr = "";

        switch (root.fieldType) {
            case ftObject:
                JSONObject json = new JSONObject();
                json = objectToJson(obj, json, root);
                retStr = json.toString();
                break;
            case ftList:
                JSONArray jarr = new JSONArray();
                jarr = listToJsonArray(obj, jarr, root);
                retStr = jarr.toString();
                break;
            case ftMap:
                JSONObject jmap = new JSONObject();
                jmap = mapToJsonObject(obj, jmap, root);
                retStr = jmap.toString();
                break;
            case ftValue:
                JSONObject jvalue = new JSONObject();
                jvalue = objectToJson(obj, jvalue, root);
                retStr = jvalue.toString();
                break;
        }

        return retStr;
    }

    @SuppressWarnings("unchecked")
    public T toObject(String jsonString) throws Exception {
        T obj = classType.newInstance();

        switch (root.fieldType) {
            case ftList:
                JSONArray jarr = new JSONArray(jsonString);
                obj = (T) jsonToList(jarr, classType, innerClassType, obj, root);
                break;
            case ftMap:
                JSONObject jmap = new JSONObject(jsonString);
                obj = (T) jsonToMap(jmap, classType, innerClassType, obj, root);
                break;
            case ftObject:
                JSONObject json = new JSONObject(jsonString);
                obj = (T) jsonToObject(json, classType, obj, root);
                break;
            case ftValue:
                JSONObject jvalue = new JSONObject(jsonString);
                obj = (T) jsonToObject(jvalue, classType, obj, root);
                break;
        }
        return obj;
    }

    private Object jsonToObject(JSONObject jobj, Class<?> cType, Object obj, JsonNode node) throws Exception {
        for (int i = 0; i < node.childs.size(); i++) {
            Field f = getField(obj, node.childs.get(i).fieldName);
            FieldType type = node.childs.get(i).fieldType;
            switchTypeDoJ2O(type, obj, f, jobj, null, node.childs.get(i).fieldName, null, i, -1, node.childs.get(i), false, false);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private Object jsonToList(JSONArray jarr, Class<?> cType, Class<?> genericType, Object obj, JsonNode node) throws Exception {
        for (int i = 0; i < jarr.length(); i++) {
            if (node.subItemNode.fieldType == FieldType.ftValue) {
                ((List<Object>) obj).add(jarr.get(i));
            } else {
                Object o = genericType.newInstance();
                for (int j = 0; j < node.subItemNode.childs.size(); j++) {
                    Field f = getField(o, node.subItemNode.childs.get(j).fieldName);
                    FieldType type = node.subItemNode.childs.get(j).fieldType;

                    switchTypeDoJ2O(type, o, f, null, jarr, node.subItemNode.childs.get(j).fieldName, null, i, j, node.subItemNode.childs.get(j), true, false);

                }
                ((List<Object>) obj).add(o);
            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private Object jsonToMap(JSONObject jobj, Class<?> cType, Class<?> genericType, Object obj, JsonNode node) throws Exception {
        Iterator<?> iter = jobj.keys();
        String key = "";
        while (iter.hasNext()) {
            key = (String) iter.next();
            if (node.subItemNode.fieldType == FieldType.ftValue) {
                ((Map<String, Object>) obj).put(key, jobj.get(key));
            } else {
                Object o = genericType.newInstance();
                for (int i = 0; i < node.subItemNode.childs.size(); i++) {
                    Field f = getField(o, node.subItemNode.childs.get(i).fieldName);
                    FieldType type = node.subItemNode.childs.get(i).fieldType;

                    switchTypeDoJ2O(type, o, f, jobj, null, node.subItemNode.childs.get(i).fieldName, key, i, -1, node.subItemNode.childs.get(i), false, true);
                }
                ((Map<String, Object>) obj).put(key, o);
            }
        }
        return obj;
    }

    private JSONObject objectToJson(Object obj, JSONObject jobj, JsonNode node) throws Exception {

        for (int i = 0; i < node.childs.size(); i++) {
            Field f = getField(obj, node.childs.get(i).fieldName);
            FieldType type = node.childs.get(i).fieldType;
            switchTypeDoO2J(type, jobj, node.childs.get(i).fieldName, f, obj, i, node.childs.get(i));
        }
        return jobj;
    }

    private JSONArray listToJsonArray(Object list, JSONArray jsonarray, JsonNode node) throws Exception {
        List<?> objList = (List<?>) list;
        for (int i = 0; i < objList.size(); i++) {
            Object o = objList.get(i);

            if (node.subItemNode.fieldType == FieldType.ftValue) {
                jsonarray.put(o);
            } else {

                JSONObject jo = new JSONObject();
                for (int j = 0; j < node.subItemNode.childs.size(); j++) {
                    Field f = getField(o, node.subItemNode.childs.get(j).fieldName);
                    FieldType type = node.subItemNode.childs.get(j).fieldType;
                    switchTypeDoO2J(type, jo, node.subItemNode.childs.get(j).fieldName, f, o, j, node.subItemNode.childs.get(j));
                }
                jsonarray.put(jo);
            }
        }
        return jsonarray;
    }

    private JSONObject mapToJsonObject(Object map, JSONObject jsonobject, JsonNode node) throws Exception {
        Map<?, ?> mapObj = (Map<?, ?>) map;
        Iterator<?> iter = mapObj.keySet().iterator();
        String key = "";
        Object o = null;
        while (iter.hasNext()) {
            key = (String) iter.next();
            if (node.subItemNode.fieldType == FieldType.ftValue) {
                jsonobject.put(key, mapObj.get(key));
            } else {
                o = mapObj.get(key);
                JSONObject jo = new JSONObject();
                for (int i = 0; i < node.subItemNode.childs.size(); i++) {
                    Field f = getField(o, node.subItemNode.childs.get(i).fieldName);
                    FieldType type = node.subItemNode.childs.get(i).fieldType;
                    switchTypeDoO2J(type, jo, node.subItemNode.childs.get(i).fieldName, f, o, i, node.subItemNode.childs.get(i));

                }
                jsonobject.put(key, jo);
            }
        }
        return jsonobject;
    }

    private Field getField(Object o, String name) throws Exception {
        Field f = o.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    private void switchTypeDoO2J(FieldType type, JSONObject jobj, String name, Field f, Object o, int index, JsonNode node) throws Exception {
        switch (type) {
            case ftValue:
                jobj.put(name, f.get(o));
                break;
            case ftObject:
                jobj.put(name, objectToJson(f.get(o), new JSONObject(), node));
                break;
            case ftList:
                jobj.put(name, listToJsonArray(f.get(o), new JSONArray(), node));
                break;
            case ftMap:
                jobj.put(name, mapToJsonObject(f.get(o), new JSONObject(), node));
                break;
        }
    }

    public void switchTypeDoJ2O(FieldType type, Object o, Field f, JSONObject jobj, JSONArray jarr, String name, String key, int index, int indexj, JsonNode node, boolean isArray, boolean isMap) throws Exception {

        switch (type) {
            case ftList:
                Field fList = o.getClass().getField(name);
                Class<?> cList = fList.getType();
                String sType = fList.getGenericType().toString();
                String genericClassName = sType.substring(sType.indexOf("<") + 1, sType.indexOf(">"));
                Class<?> cListInner = Class.forName(genericClassName);
                Object oList = cList.newInstance();
                if (isMap) {
                    f.set(o, jsonToList(jobj.getJSONArray(key), cList, cListInner, oList, node.subItemNode.childs.get(index)));
                } else {
                    if (isArray) {
                        f.set(o, jsonToList(jarr.getJSONObject(index).getJSONArray(name), cList, cListInner, oList, node.subItemNode.childs.get(indexj)));
                    } else {
                        f.set(o, jsonToList(jobj.getJSONArray(name), cList, cListInner, oList, node.childs.get(index)));
                    }
                }

                break;
            case ftMap:
                Field fMap = o.getClass().getField(name);
                Class<?> cMap = fMap.getType();
                String sMapType = fMap.getGenericType().toString();
                String genericMapName = sMapType.substring(sMapType.indexOf(",") + 1, sMapType.indexOf(">")).trim();
                Class<?> cMapInner = Class.forName(genericMapName);
                Object oMap = cMap.newInstance();
                if (isMap) {
                    f.set(o, jsonToMap(jobj.getJSONObject(key), cMap, cMapInner, oMap, node.subItemNode.childs.get(index)));
                } else {
                    if (isArray) {
                        f.set(o, jsonToMap(jarr.getJSONObject(index).getJSONObject(name), cMap, cMapInner, oMap, node.subItemNode.childs.get(indexj)));
                    } else {
                        f.set(o, jsonToMap(jobj.getJSONObject(name), cMap, cMapInner, oMap, node));
                    }
                }
                break;
            case ftObject:
                Field fSub = o.getClass().getField(name);
                Class<?> cSub = fSub.getType();
                Object oSub = cSub.newInstance();
                if (isMap) {
                    f.set(o, jsonToObject(jobj.getJSONObject(key), cSub, oSub, node.subItemNode.childs.get(index)));
                } else {
                    if (isArray) {
                        f.set(o, jsonToObject(jarr.getJSONObject(index).getJSONObject(name), cSub, oSub, node.subItemNode.childs.get(indexj)));
                    } else {
                        f.set(o, jsonToObject(jobj.getJSONObject(name), cSub, oSub, node.childs.get(index)));
                    }
                }
                break;
            case ftValue:
                if (isMap) {
                    f.set(o, jobj.getJSONObject(key).get(name));
                } else {
                    if (isArray) {
                        f.set(o, jarr.getJSONObject(index).get(name));
                    } else {
                        f.set(o, jobj.get(name));
                    }
                }
                break;
        }
    }

    public static void fillJsonToObject(String jsonString, Object obj) {
        try {
            JSONObject json = new JSONObject(jsonString);
            fillJsonToObject(json, obj);
        } catch(Exception e) {

        }
    }

    public static void fillJsonToObject(JSONObject json, Object obj) {
        try {
            Class<?> clz = obj.getClass();
            Field[] fs = clz.getFields();
            String typeStr = "";
            for (Field f: fs) {
                typeStr = f.getType().getSimpleName();
                if (typeStr.equals("String")) {
                    try { f.set(obj, json.getString(f.getName())); } catch(Exception e) { }
                } else if (typeStr.equals("int")) {
                    try { f.setInt(obj, json.getInt(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("double")) {
                    try { f.setDouble(obj, json.getDouble(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("boolean")) {
                    try { f.setBoolean(obj, json.getBoolean(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("float")) {
                    try { f.setFloat(obj, (float) json.getDouble(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("long")) {
                    try { f.setLong(obj, json.getLong(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("byte")) {
                    try { f.setByte(obj, (byte) json.getInt(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("short")) {
                    try { f.setShort(obj, (short) json.getInt(f.getName())); } catch(Exception e) {}
                } else if (typeStr.equals("char")) {
                    try { f.setChar(obj, json.getString(f.getName()).charAt(0)); } catch(Exception e) {}
                }
            }
        } catch(Exception e) {

        }
    }

    public static String objectToJsonString(Object obj) {
        String str = "{";
        try {
            Class<?> clz = obj.getClass();
            Field[] fs = clz.getFields();
            String typeStr = "";
            for (Field f: fs) {
                typeStr = f.getType().getSimpleName();
                if (typeStr.equals("String")) {
                    str += String.format("\"%s\":\"%s\",", f.getName(), (String)f.get(obj));
                } else if (typeStr.equals("int")) {
                    str += String.format("\"%s\":%d,", f.getName(), f.getInt(obj));
                } else if (typeStr.equals("double")) {
                    str += String.format("\"%s\":%f,", f.getName(), f.getDouble(obj));
                } else if (typeStr.equals("boolean")) {
                    str += String.format("\"%s\":%b,", f.getName(), f.getBoolean(obj));
                } else if (typeStr.equals("float")) {
                    str += String.format("\"%s\":%f,", f.getName(), f.getFloat(obj));
                } else if (typeStr.equals("long")) {
                    str += String.format("\"%s\":%d,", f.getName(), f.getLong(obj));
                } else if (typeStr.equals("byte")) {
                    str += String.format("\"%s\":%d,", f.getName(), f.getByte(obj));
                } else if (typeStr.equals("short")) {
                    str += String.format("\"%s\":%d,", f.getName(), f.getShort(obj));
                } else if (typeStr.equals("char")) {
                    str += String.format("\"%s\":\"%s\",", f.getName(), f.getChar(obj));
                }
            }
        } catch(Exception e) {

        }
        str = str.substring(0, str.length() - 1);
        str += "}";
        return str;
    }
}
