package test.wallet.platfrom.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wallet.platform.po.*;

public class MybatisXmlFile {
	private static final Map<Class<?>, String> map = new HashMap<Class<?>, String>();
	private Class<?> clazz;
	private String namespace;
	private String id;
	private Class<?> idClass;
	private String tableName;
	private Field[] fileds;
	private FileOutputStream fos;
	private OutputStreamWriter osw;

	static {
		map.put(Long.class, "BIGINT");
		map.put(Date.class, "TIMESTAMP");
		map.put(Integer.class, "INTEGER");
		map.put(Boolean.class, "TINYINT");
		map.put(String.class, "VARCHAR");
		map.put(Double.class, "DOUBLE");
		map.put(BigDecimal.class, "DECIMAL");
	}

	public static void main(String[] args) throws Exception {
		MybatisXmlFile file = new MybatisXmlFile(
				Asset.class, 
				"id",
				String.class, 
				"wallet_asset", 
				"com.wallet.platform.dao",
				"D:\\temp\\mybatis");
		file.create();
	}

	public MybatisXmlFile(Class<?> clazz, String id, Class<?> idClass,
			String tableName, String namespace, String floder) {
		this.clazz = clazz;
		this.id = id;
		this.idClass = idClass;
		this.tableName = tableName;
		this.namespace = namespace;

		String file = floder + File.separator + "I"
				+ this.clazz.getSimpleName() + "Dao.xml";
		try {
			this.fileds = this.clazz.getDeclaredFields();
			this.fos = new FileOutputStream(file);
			this.osw = new OutputStreamWriter(this.fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		createSql();

		createHeader();

		createResultMap();
		createInsert();
		createUpdateById();
		createDeleteById();
		createGetById();
		createGetByParam();
		createQueryByParam();
		createGetCountByParam();

		createFooter();
		close();
	}

	private void createSql() {
		System.out.println("drop table if exists " + this.tableName + ";");
		System.out.println("create table " + this.tableName + "(");
		for (Field f : this.fileds)
			if (!"serialVersionUID".equals(f.getName()) && null != map.get(f.getType())) {
				String temp = "";
				if (this.id.equals(f.getName())) {
					temp = "primary key";
				}
				System.out.println("\t" + getColumn(f.getName()) + " "
						+ ((String) map.get(f.getType())).toLowerCase() + " "
						+ temp + " comment '',");
			}
		System.out.println(") engine=InnoDB default charset=utf8;");
	}

	private void createHeader() throws Exception {
		println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		println("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
		println("<mapper namespace=\"" + this.namespace + ".I"
				+ this.clazz.getSimpleName() + "Dao\">");
	}

	private void createFooter() throws Exception {
		println();
		println("</mapper>");
	}

	private void createResultMap() throws Exception {
		println();
		println("\t<resultMap type=\"" + this.clazz.getName() + "\" id=\""
				+ firstLower(this.clazz.getSimpleName()) + "\">");
		for (Field f : this.fileds)
			if (!"serialVersionUID".equals(f.getName()) && null != map.get(f.getType())) {
				String temp = "result";
				if (this.id.equals(f.getName())) {
					temp = "id";
				}
				if (null != map.get(f.getType())) {
					println("\t\t<" + temp + " column=\"" + getColumn(f.getName())
							+ "\" jdbcType=\"" + (String) map.get(f.getType())
							+ "\" property=\"" + f.getName() + "\" javaType=\""
							+ f.getType().getName() + "\" />");
				}
			}
		println("\t</resultMap>");
	}

	private void createInsert() throws Exception {
		println();
		println("\t<insert id=\"insert\" parameterType=\""
				+ this.clazz.getName() + "\">");
		println("\t\tinsert into " + this.tableName);
		println("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		for (int i = 0; i < this.fileds.length; i++) {
			if (!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType())) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ "\">");
				println("\t\t\t\t" + getColumn(this.fileds[i].getName()) + ",");
				println("\t\t\t</if>");
			}
		}
		println("\t\t</trim>");
		println("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
		for (int i = 0; i < this.fileds.length; i++)
			if (!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType())) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ "\">");
				println("\t\t\t\t#{" + this.fileds[i].getName() + ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "},");
				println("\t\t\t</if>");
			}
		println("\t\t</trim>");
		println("\t</insert>");
	}

	private void createUpdateById() throws Exception {
		println();
		println("\t<update id=\"updateById\" parameterType=\""
				+ this.clazz.getName() + "\">");
		println("\t\tupdate");
		println("\t\t\t" + this.tableName);
		println("\t\t<set>");
		for (int i = 0; i < this.fileds.length; i++)
			if ((!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType()))
					&& (!this.id.equals(this.fileds[i].getName()))) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ "\">");
				println("\t\t\t\t" + getColumn(this.fileds[i].getName())
						+ " = " + "#{" + this.fileds[i].getName()
						+ ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "},");
				println("\t\t\t</if>");
			}
		println("\t\t</set>");
		println("\t\twhere");
		for (int i = 0; i < this.fileds.length; i++)
			if ((!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType()))
					&& (this.id.equals(this.fileds[i].getName()))) {
				println("\t\t\t" + getColumn(this.fileds[i].getName()) + " = "
						+ "#{" + this.fileds[i].getName() + ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
			}
		println("\t</update>");
	}

	private void createDeleteById() throws Exception {
		println();
		println("\t<delete id=\"deleteById\" parameterType=\""
				+ this.idClass.getName() + "\">");
		println("\t\tdelete from");
		println("\t\t\t" + this.tableName);
		println("\t\twhere");
		for (int i = 0; i < this.fileds.length; i++)
			if ((!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType()))
					&& (this.id.equals(this.fileds[i].getName()))) {
				println("\t\t\t" + getColumn(this.fileds[i].getName()) + " = "
						+ "#{" + this.fileds[i].getName() + ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
			}
		println("\t</delete>");
	}

	private void createGetById() throws Exception {
		println();
		println("\t<select id=\"getById\" parameterType=\""
				+ this.idClass.getName() + "\" resultMap=\""
				+ firstLower(this.clazz.getSimpleName()) + "\">");
		println("\t\tselect");
		println("\t\t\t*");
		println("\t\tfrom");
		println("\t\t\t" + this.tableName);
		println("\t\twhere");
		for (int i = 0; i < this.fileds.length; i++)
			if ((!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType()))
					&& (this.id.equals(this.fileds[i].getName()))) {
				println("\t\t\t" + getColumn(this.fileds[i].getName()) + " = "
						+ "#{" + this.fileds[i].getName() + ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
			}
		println("\t</select>");
	}

	private void createGetByParam() throws Exception {
		println();
		println("\t<select id=\"getByParam\" parameterType=\""
				+ Map.class.getName() + "\" resultMap=\""
				+ firstLower(this.clazz.getSimpleName()) + "\">");
		println("\t\tselect");
		println("\t\t\t*");
		println("\t\tfrom");
		println("\t\t\t" + this.tableName);
		println("\t\t<trim prefix=\" where \" prefixOverrides=\"and\" suffixOverrides=\"and\">");
		for (int i = 0; i < this.fileds.length; i++)
			if (!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType())) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ " or null != " + this.fileds[i].getName()
						+ "Required" + "\">");
				println("\t\t\t\tand " + getColumn(this.fileds[i].getName())
						+ " = " + "#{" + this.fileds[i].getName()
						+ ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
				println("\t\t\t</if>");
			}
		println("\t\t</trim>");
		println("\t\tlimit 1");
		println("\t</select>");
	}

	private void createQueryByParam() throws Exception {
		println();
		println("\t<select id=\"queryByParam\" parameterType=\""
				+ Map.class.getName() + "\" resultMap=\""
				+ firstLower(this.clazz.getSimpleName()) + "\">");
		println("\t\tselect");
		println("\t\t\t*");
		println("\t\tfrom");
		println("\t\t\t" + this.tableName);
		println("\t\t<trim prefix=\" where \" prefixOverrides=\"and\" suffixOverrides=\"and\">");
		for (int i = 0; i < this.fileds.length; i++)
			if (!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType())) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ " or null != " + this.fileds[i].getName()
						+ "Required" + "\">");
				println("\t\t\t\tand " + getColumn(this.fileds[i].getName())
						+ " = " + "#{" + this.fileds[i].getName()
						+ ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
				println("\t\t\t</if>");
			}
		println("\t\t</trim>");
		println("\t\t<if test=\"null != start and null != size\">");
		println("\t\tlimit");
		println("\t\t\t#{start, jdbcType=INTEGER}, #{size, jdbcType=INTEGER}");
		println("\t\t</if>");
		println("\t</select>");
	}

	private void createGetCountByParam() throws Exception {
		println();
		println("\t<select id=\"getCountByParam\" parameterType=\""
				+ Map.class.getName() + "\" resultType=\"java.lang.Integer\">");
		println("\t\tselect");
		println("\t\t\tcount(*)");
		println("\t\tfrom");
		println("\t\t\t" + this.tableName);
		println("\t\t<trim prefix=\" where \" prefixOverrides=\"and\" suffixOverrides=\"and\">");
		for (int i = 0; i < this.fileds.length; i++)
			if (!"serialVersionUID".equals(this.fileds[i].getName()) && null != map.get(fileds[i].getType())) {
				println("\t\t\t<if test=\"null != " + this.fileds[i].getName()
						+ " or null != " + this.fileds[i].getName()
						+ "Required" + "\">");
				println("\t\t\t\tand " + getColumn(this.fileds[i].getName())
						+ " = " + "#{" + this.fileds[i].getName()
						+ ", jdbcType="
						+ (String) map.get(this.fileds[i].getType()) + "}");
				println("\t\t\t</if>");
			}
		println("\t\t</trim>");
		println("\t</select>");
	}

	private static String firstLower(String src) {
		return new String(src.substring(0, 1).toLowerCase() + src.substring(1));
	}

	private static String getColumn(String name) {
		String res = "";
		for (char c : name.toCharArray()) {
			if (('A' <= c) && (c <= 'Z'))
				res = res + "_" + (char) (c + ' ');
			else {
				res = res + c;
			}
		}
		return res;
	}

	private void println(String content) throws Exception {
		this.osw.write(content);
		println();
	}

	private void println() throws Exception {
		this.osw.write("\n");
	}

	private void close() {
		if (this.osw != null) {
			try {
				this.osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.fos != null)
			try {
				this.fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}