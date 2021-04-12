package com.merlin.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Pooja Chopra
 *
 */
public class Json_Parsor {

	public  static String Get_Value_From_Json(String json, String key ) {
		return JsonPath.parse(json).read("$."+key).toString();
	}
	
	public  static String Get_Id_From_Json_Based_On_Value(String json, String aray_Name, String key, String value ) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray array = jsonObject.getJSONArray(aray_Name);
		Iterator<Object> iterator = array.iterator();
		String value_Of_Response ="", id="";
		while(iterator.hasNext()){
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			value_Of_Response = JsonPath.parse(jsonObject1.toString()).read("$."+key).toString();
			if(value_Of_Response.equals(value))
			{
				id = JsonPath.parse(jsonObject1.toString()).read("$.ID").toString();
				break;
			}
			System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		}
		System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		return id;
	}

	public  static String Get_Value_Of_Key_From_Json_Based_On_Value(String response, String aray_Name, String Check_key, String Check_value,String key ) {
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray(aray_Name);
		Iterator<Object> iterator = array.iterator();
		String value_Of_Response ="", id="";
		while(iterator.hasNext()){
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			value_Of_Response = JsonPath.parse(jsonObject1.toString()).read("$."+Check_key).toString();
			if(value_Of_Response.equals(Check_value))
			{
				id = JsonPath.parse(jsonObject1.toString()).read("$."+key).toString();
				break;
			}
			System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		}
		System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		return id;
	}

	public  static String Get_Id_From_Asset_folder(String json,String object, String aray_Name, String key, String value ) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray array=jsonObject.getJSONObject(object).getJSONArray(aray_Name);
		Iterator<Object> iterator = array.iterator();
		String value_Of_Response ="", id="";
		while(iterator.hasNext()){
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			value_Of_Response = JsonPath.parse(jsonObject1.toString()).read("$."+key).toString();
			if(value_Of_Response.equals(value))
			{
				id = JsonPath.parse(jsonObject1.toString()).read("$.id").toString();
				break;
			}
			System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		}
		System.out.println("test--name - "+ value_Of_Response + ",  ID - "+ id);
		return id;
	}

	public  static String Get_Id_From_Response(String json, String aray_Name, String key ) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray array = jsonObject.getJSONArray(aray_Name);
		Iterator<Object> iterator = array.iterator();
		String id="";
		while(iterator.hasNext()){
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			if(jsonObject1.toString().contains(key))
			{
				id = JsonPath.parse(jsonObject1.toString()).read("$."+key).toString();
				break;
			}
			System.out.println("From Response : "+ key + ",  ID - "+ id);
		}

		return id;
	}
	
	public static ArrayList Fetch_All_Json_Array_For_Given_Field_From_Response_in_a_List(String json_Response,String array_Name,String field) {
		JSONObject jsonObject = new JSONObject(json_Response);
		JSONArray array = jsonObject.getJSONArray(array_Name);
		Iterator<Object> iterator = array.iterator();
		ArrayList<String> list= new ArrayList<String>();
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			list.add(jsonObject1.get(field).toString());
		}
		return list;
	}
	
	
	//This method will return the Field value of a field with check for a particular field-value
	public  static String Get_Value_From_Response_For_Given_Field(String json, String aray_Name, String key,String field_Value ,String field ) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray array = jsonObject.getJSONArray(aray_Name);
		Iterator<Object> iterator = array.iterator();
		String value_Fetched="";
		while(iterator.hasNext()){
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			if(key.contains(jsonObject1.get(field).toString()))
			{
				value_Fetched=jsonObject1.get(field_Value).toString();
				break;
			}
			System.out.println("From Response : "+ key + ",  ID - "+ value_Fetched);
		}
		return value_Fetched;
	}
}
