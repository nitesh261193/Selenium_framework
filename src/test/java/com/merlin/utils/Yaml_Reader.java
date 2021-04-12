/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Pooja Chopra
 *
 */
public class Yaml_Reader {

	public static String yaml_File_Path;

	@SuppressWarnings("resource")
	public static String Set_Yaml_File_Path(String type) {
		
		

		yaml_File_Path = Yml_Path(type);
		try {
			new FileReader(yaml_File_Path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return yaml_File_Path;
	}
	
	public static String Yml_Path(String type) {
		String path = System.getProperty("user.dir") + "/src/test/resources/";
		switch (type) {
		case "config":
			path = path + "Properties/config.yml";
			break;
		case "api":
			path = path + "Merlin_App_Api/" + Rest_Api.config_Name + ".yml";
			break;

		default:
			break;
			
			
		}
		
		return path;
	}

	public static String Get_Yaml_Value(String yml_Path , String token) {
			Set_Yaml_File_Path(yml_Path);
		return Get_Value(token);
	}


	public static Map<String, Object> Get_Yaml_Values(String yml_Path ,String token) {
		if (yaml_File_Path == null) {
			Set_Yaml_File_Path(yml_Path);
		}
		Reader doc;
		try {
			doc = new FileReader(yaml_File_Path);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
		Yaml yaml = new Yaml();
		// TODO: check the type casting of object into the Map and create
		// instance in one place
		Map<String, Object> object = (Map<String, Object>) yaml.load(doc);
		return Parse_Map(object, token + ".");
	}

	private static String Get_Value(String token) {
		Reader doc = null;
		try {
			doc = new FileReader(yaml_File_Path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Yaml yaml = new Yaml();
		Map<String, Object> object = (Map<String, Object>) yaml.load(doc);
		try {
			return Get_Map_Value(object, token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String Get_Value(String yaml_Path, String token) {
		Reader doc = null;
		try {
			doc = new FileReader(yaml_Path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Yaml yaml = new Yaml();
		Map<String, Object> object = (Map<String, Object>) yaml.load(doc);
		try {
			return Get_Map_Value(object, token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String Get_Map_Value(Map<String, Object> object, String token) {
		// TODO: check for proper yaml token string based on presence of '.'
		String[] st = token.split("\\.");
		return Parse_Map(object, token).get(st[st.length - 1]).toString();
	}

	private static Map<String, Object> Parse_Map(Map<String, Object> object, String token) {
		if (token.contains(".")) {
			String[] st = token.split("\\.");
			object = Parse_Map((Map<String, Object>) object.get(st[0]), token.replace(st[0] + ".", ""));
		}
		return object;
	}

	/*
	 * public static List<String> getYamlValuesAsList(String token) { return
	 * Arrays.asList(getYamlValue(token).split("\\|")); }
	 */
}
