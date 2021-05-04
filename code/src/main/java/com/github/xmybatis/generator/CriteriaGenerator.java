package com.github.xmybatis.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import com.github.xmybatis.core.annotation.Column;

public class CriteriaGenerator {

	// private Hashtable<Class,String> classMap = new Hashtable

	public boolean generateByClass(Class cls, String dirName) {
		Package pkg = cls.getPackage();
		String target = pkg.getName().replaceAll("\\.", "\\" + File.separator);
		
		File dir = new File(dirName);
		if (!dir.exists()) {

			if (!(new File(dirName + File.separator + target).mkdirs())) {
				
				return false;
			}
		}
		File file = new File(dirName + File.separator + target + File.separator + cls.getSimpleName() + "Criteria.java");
		System.out.println("target file:"+file.getAbsolutePath());
		try {
			FileOutputStream output = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(output);

			pw.println("package " + pkg.getName() + ";");
			pw.println();
			pw.println("import java.sql.Timestamp;");
			pw.println("import java.util.List;");
			pw.println("import com.github.xmybatis.core.Criteria;");
			pw.println();
			pw.println("public class " + cls.getSimpleName() + "Criteria extends Criteria {\r\n");
			pw.println("\tpublic " + cls.getSimpleName() + "Criteria()");
			pw.println("\t{\r\n" + "\t\tsuper();\r\n" + "\t}");

			Field ms[] = cls.getDeclaredFields();

			for (Field f : ms) {
				
				Annotation methodAnnon = f.getAnnotation(Column.class);
				if (methodAnnon == null) {
					continue;
				}

				Column c = (Column) methodAnnon;
				pw.print(this.genIsNull(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genIsNotNull(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genEqualTo(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(
						this.genNotEqualTo(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genGreaterThan(c.value(), upperFirstChar(f.getName()),
						f.getType().getSimpleName()));
				pw.print(this.genGreaterThanOrEqualTo(c.value(), upperFirstChar(f.getName()),
						f.getType().getSimpleName()));
				pw.print(this.genLessThan(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genLessThanOrEqualTo(c.value(), upperFirstChar(f.getName()),
						f.getType().getSimpleName()));
				pw.print(this.genLike(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genNotLike(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));

				pw.print(this.genLikeL(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genNotLikeL(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				
				pw.print(this.genLikeR(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(this.genNotLikeR(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				
				pw.print(this.genBetween(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(
						this.genNotBetween(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(
						this.genIn(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));
				pw.print(
						this.genNotIn(c.value(), upperFirstChar(f.getName()), f.getType().getSimpleName()));

			}

			pw.println("}");
			pw.flush();
			pw.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return true;
	}



	private String genIsNull(String colName, String propertyName, String paramTypeName) {
		return    "        public Criteria and" + propertyName + "IsNull() {\r\n" + "            addCriterion(\"" + colName
				+ " is null\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "IsNull_U_D() {\r\n" + "            addCriterion(\"" + getAfterDot(colName)
				+ " is null\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genIsNotNull(String colName, String propertyName, String paramTypeName) {
		return   "        public Criteria and" + propertyName + "IsNotNull() {\r\n" + "            addCriterion(\""
				+ colName + " is not null\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "IsNotNull_U_D() {\r\n" + "            addCriterion(\""
				+ getAfterDot(colName) + " is not null\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genEqualTo(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "EqualTo(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " = \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+ "        public Criteria and" + propertyName + "EqualTo_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " = \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genNotEqualTo(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "NotEqualTo(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " <> \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "NotEqualTo_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " <> \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genGreaterThan(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "GreaterThan(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " > \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "GreaterThan_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " > \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genGreaterThanOrEqualTo(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "GreaterThanOrEqualTo(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " >= \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "GreaterThanOrEqualTo_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " >= \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genLessThan(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "LessThan(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " < \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "LessThan_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " < \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genLessThanOrEqualTo(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "LessThanOrEqualTo(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + colName + " <= \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "LessThanOrEqualTo_U_D(" + paramTypeName + " value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " <= \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genLike(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"Like("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+colName+" like \", value,\"%\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		sb.append("\tpublic void and"+propertyName+"Like_U_D("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+" like \", value,\"%\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	private String genNotLike(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"NotLike("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+colName+" not like \", value,\"%\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		sb.append("\tpublic void and"+propertyName+"NotLike_U_D("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+" not like \", value,\"%\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	private String genLikeL(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"LikeL("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+colName+" like \", value,\"%\",\"\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		sb.append("\tpublic void and"+propertyName+"LikeL_U_D("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+" like \", value,\"%\",\"\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	private String genNotLikeL(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"NotLikeL("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+colName+" not like \", value,\"%\",\"\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		sb.append("\tpublic void and"+propertyName+"NotLikeL_U_D("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+" not like \", value,\"%\",\"\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	private String genLikeR(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"LikeR("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+"  like \", value,\"\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	private String genNotLikeR(String colName, String propertyName, String paramTypeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic void and"+propertyName+"NotLikeR("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+colName+" not like \", value,\"\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		
		sb.append("\tpublic void and"+propertyName+"NotLikeR_U_D("+paramTypeName+" value)\r\n");
		sb.append("\t{\r\n");
		sb.append("\t\taddCriterion(\""+getAfterDot(colName)+" not like \", value,\"\",\"%\",\""+propertyName+"\");\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}

	private String genBetween(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "Between(" + paramTypeName + " value1," + paramTypeName
				+ "  value2) {\r\n" + "            addCriterion(\"" + colName + " between \", value1,value2,\""
				+ propertyName + "\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "Between_U_D(" + paramTypeName + " value1," + paramTypeName
				+ "  value2) {\r\n" + "            addCriterion(\"" + getAfterDot(colName) + " between \", value1,value2,\""
				+ propertyName + "\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genNotBetween(String colName, String propertyName, String paramTypeName) {
		return "        public Criteria and" + propertyName + "NotBetween(" + paramTypeName + " value1," + paramTypeName
				+ " value2) {\r\n" + "            addCriterion(\"" + colName + " not between \", value1,value2,\""
				+ propertyName + "\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "NotBetween_U_D(" + paramTypeName + " value1," + paramTypeName
				+ " value2) {\r\n" + "            addCriterion(\"" + getAfterDot(colName )+ " not between \", value1,value2,\""
				+ propertyName + "\");\r\n" + "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genIn(String colName, String propertyName, String paramTypeName) {
		if (paramTypeName.equals("int"))
		{
			paramTypeName="Integer";
		}else if (paramTypeName.equals("short"))
		{
			paramTypeName="Short";
		}else if (paramTypeName.equals("Long"))
		{
			paramTypeName="Long";
		}else if (paramTypeName.equals("float"))
		{
			paramTypeName="Float";
		}else if (paramTypeName.equals("double"))
		{
			paramTypeName="Double";
		}else
			if (paramTypeName.equals("byte"))
			{
				paramTypeName="Byte";
			}
		return "        public Criteria and" + propertyName + "In(List<" + paramTypeName + "> value) {\r\n"
				+ "            addCriterion(\"" + colName + " in \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "In_U_D(List<" + paramTypeName + "> value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " in \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}

	private String genNotIn(String colName, String propertyName, String paramTypeName) {
		
		if (paramTypeName.equals("int"))
		{
			paramTypeName="Integer";
		}else if (paramTypeName.equals("short"))
		{
			paramTypeName="Short";
		}else if (paramTypeName.equals("Long"))
		{
			paramTypeName="Long";
		}else if (paramTypeName.equals("float"))
		{
			paramTypeName="Float";
		}else if (paramTypeName.equals("double"))
		{
			paramTypeName="Double";
		}else
			if (paramTypeName.equals("byte"))
			{
				paramTypeName="Byte";
			}
		
		
		return "        public Criteria and" + propertyName + "NotIn(List<" + paramTypeName + "> value) {\r\n"
				+ "            addCriterion(\"" + colName + " not in \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n"
				+"        public Criteria and" + propertyName + "NotIn_U_D(List<" + paramTypeName + "> value) {\r\n"
				+ "            addCriterion(\"" + getAfterDot(colName) + " not in \", value,\"" + propertyName + "\");\r\n"
				+ "            return (Criteria) this;\r\n" + "        }\r\n";
	}
	


	private String upperFirstChar(String s)
	{
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}
	
	private static String getAfterDot(String s)
	{
		return s.substring(s.indexOf('.')+1);
	}
	
	public static void main(String[] args)
	{
		System.out.println(getAfterDot("aa.bb"));
	}
}
