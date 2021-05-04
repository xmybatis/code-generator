package com.github.xmybatis.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.xmybatis.core.annotation.Column;

public class ExampleGenerator {

	public boolean generateByClass(Class cls, String dirName) {
		Package pkg = cls.getPackage();
		String target = pkg.getName().replaceAll("\\.", "\\" + File.separator);
		System.out.println(dirName + File.separator + target);
		File dir = new File(dirName);
		if (!dir.exists()) {

			if (!(new File(dirName + File.separator + target).mkdirs())) {
				System.out.println("make dir error");
				return false;
			}
		}
		File f = new File(dirName + File.separator + target + File.separator + cls.getSimpleName() + "ExampleWraper.java");
		try {
			FileOutputStream output = new FileOutputStream(f);
			PrintWriter pw = new PrintWriter(output);

			pw.println("package " + pkg.getName() + ";");
			pw.println();
			pw.println(""
					//+"import net.iotgw.mybatis.Criteria;\r\n"
//					+"import net.iotgw.mybatis.Example;\r\n"
					+"import net.iotgw.mybatis.ExampleWraper;"
					);
			pw.println("public class " + cls.getSimpleName() + "ExampleWraper extends ExampleWraper  {\r\n");
			
			
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {

				Annotation methodAnnon = field.getAnnotation(Column.class);
				if (methodAnnon == null) {
					continue;
				}
				pw.println(genEqual(field,(Column)methodAnnon));
				pw.println(genNotEqual(field,(Column)methodAnnon));
				pw.println(genGreater(field,(Column)methodAnnon));
				pw.println(genGreaterAndEqual(field,(Column)methodAnnon));
				pw.println(genLesser(field,(Column)methodAnnon));
				pw.println(genLesserAndEqual(field,(Column)methodAnnon));
				pw.println(genBetween(field,(Column)methodAnnon));
				pw.println(genNotBetween(field,(Column)methodAnnon));
				pw.println(genLike(field,(Column)methodAnnon));
				pw.println(genNotLike(field,(Column)methodAnnon));
				pw.println(genLikeL(field,(Column)methodAnnon));
				pw.println(genNotLikeL(field,(Column)methodAnnon));
				pw.println(genLikeR(field,(Column)methodAnnon));
				pw.println(genNotLikeR(field,(Column)methodAnnon));
				pw.println(genIn(field,(Column)methodAnnon));
				pw.println(genNotIn(field,(Column)methodAnnon));
			}
			pw.println("}");
			pw.flush();
			pw.close();
		}catch(Exception e)
		{
			
		}
		return true;
	}
	

	private String upperFirstChar(String s)
	{
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}
	
	private String genEqual(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"Equals(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+"=\", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotEqual(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"NotEquals(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" <> \", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genGreater(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"Greater(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" > \", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genGreaterAndEqual(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"GreaterAndEqual(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" >= \", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genLesser(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"Lesser(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" > \", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genLesserAndEqual(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"LesserAndEqual(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" >= \", value, \""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genBetween(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"Between(Object value1,Object value2)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" between \", value1, value2,\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotBetween(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"NotBetween(Object value1,Object value2)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" not between \", value1, value2,\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genLike(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"Like(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" like \", value,\"%\",\"%\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotLike(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"NotLike(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" not like \", value,\"%\",\"%\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genLikeL(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"LikeL(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" like \", value,\"%\",\"\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotLikeL(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+f.getName()+"NotLikeL(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" not like \", value,\"%\",\"\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genLikeR(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"LikeR(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+"  like \", value,\"\",\"%\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotLikeR(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+f.getName()+"NotLikeR(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" not like \", value,\"\",\"%\",\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genIn(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"In(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" in  \", value,\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
	private String genNotIn(Field f,Column c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+upperFirstChar(f.getName())+"NotIn(Object value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\tc.addCriterion(\""+c.value()+" not in  \", value,\""+f.getName()+"\");\r\n");
		sb.append("\t}");
		return sb.toString();
	}
}
