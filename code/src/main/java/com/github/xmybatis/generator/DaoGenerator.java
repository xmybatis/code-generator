package com.github.xmybatis.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;



import com.github.xmybatis.core.annotation.Column;


public class DaoGenerator 
{
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
		File f = new File(dirName + File.separator + target + File.separator + cls.getSimpleName() + "Dao.java");
		System.out.println("target file:"+f.getAbsolutePath());
		try {
			FileOutputStream output = new FileOutputStream(f);
			PrintWriter pw = new PrintWriter(output);

			pw.println("package " + pkg.getName() + ";");
			pw.println();
			pw.println("import java.util.List;\r\n" + 
					"import org.apache.ibatis.annotations.DeleteProvider;\r\n" + 
					"import org.apache.ibatis.annotations.InsertProvider;\r\n" + 
					"import org.apache.ibatis.annotations.Mapper;\r\n" + 
					"import org.apache.ibatis.annotations.SelectKey;\r\n" + 
					"import org.apache.ibatis.annotations.SelectProvider;\r\n" + 
					"import org.apache.ibatis.annotations.UpdateProvider;\r\n" + 
					"import org.apache.ibatis.annotations.Param;\r\n"+
					"import org.apache.ibatis.annotations.Results;\r\n" + 
					"import org.apache.ibatis.annotations.Result;\r\n" + 
					"import org.apache.ibatis.annotations.ResultMap;\r\n" + 
					"\r\n" + 
					"import com.github.xmybatis.core.Example;\r\n");
			pw.println("@Mapper\r\npublic interface " + cls.getSimpleName() + "Dao  {\r\n");
			
			pw.println(genSelectyById(cls));
			pw.println();
			pw.println(genSelectByExample(cls));
			pw.println();
			
			pw.println(genInsertSelective(cls));
			pw.println();
			
			pw.println(genUpdateSelectiveById(cls));
			pw.println();
			pw.println(genUpdateSelectiveByExample(cls));
			pw.println();

			pw.println(genDeleteId(cls));
			pw.println();
			pw.println(genDeleteByExample(cls));
			pw.println();
			
			pw.println(genCountByExample(cls));
			pw.println();
			pw.println("}");
			pw.flush();
			pw.close();
		}catch(Exception e)
		{
			
		}
		return true;
	}
	
	private String genInsertSelective(Class clz)
	{
		String keyProperty=getIdProperty(clz);
		
		return "	@InsertProvider(type="+clz.getSimpleName()+"SQL.class,method=\"insertSelective\")\r\n" + 
				"	@SelectKey(statement = \"SELECT LAST_INSERT_ID()\", keyProperty = \""+keyProperty+"\", before = false, resultType = int.class)\r\n" + 
				"	public int insertSelective("+clz.getSimpleName()+" value);";
	}
	private String genUpdateSelectiveById(Class cls)
	{
		return "	@UpdateProvider(type="+cls.getSimpleName()+"SQL.class,method=\"updateSelectiveById\")\r\n" + 
				"	public int updateSelectiveById("+cls.getSimpleName()+" info);";
	}
	private String genUpdateSelectiveByExample(Class cls)
	{
		return "	@UpdateProvider(type="+cls.getSimpleName()+"SQL.class,method=\"updateSelectiveByExample\")\r\n" + 
				"	public int updateSelectiveByExample(@Param(\"record\")"+cls.getSimpleName()+" info,@Param(\"example\")Example ex);";
	}
	private String genSelectyById(Class cls)
	{
		return //genResults(cls)+
				"	@SelectProvider(type="+cls.getSimpleName()+"SQL.class,method=\"selectById\")\r\n" + 
				"	public "+cls.getSimpleName()+" selectById(@Param(\"schema\")String schema,@Param(\"id\")Integer id);";
	}
	private String genSelectByExample(Class cls)
	{
		return 
				//"	@ResultMap(\""+Character.toLowerCase(cls.getSimpleName().charAt(0))
				//		+cls.getSimpleName().substring(1)+"Mapper\")\r\n" + 
				
				"	@SelectProvider(type="+cls.getSimpleName()+"SQL.class,method=\"selectByExample\")\r\n" + 
				"	public List<"+cls.getSimpleName()+"> selectByExample(Example ex);";
	}
	private String genDeleteId(Class cls)
	{
		return "	@DeleteProvider(type="+cls.getSimpleName()+"SQL.class,method=\"deleteById\")\r\n" + 
				"	public int deleteById(@Param(\"schema\")String schema,@Param(\"id\")int id);";
	}
	private String genDeleteByExample(Class cls)
	{
		return "	@DeleteProvider(type="+cls.getSimpleName()+"SQL.class,method=\"deleteByExample\")\r\n" + 
				"	public int deleteByExample(Example ex);";
	}
	private String genCountByExample(Class cls)
	{
		return "	@SelectProvider(type="+cls.getSimpleName()+"SQL.class,method=\"countByExample\")\r\n" + 
				"	public int countByExample(Example ex);";
	}
	private String getIdProperty(Class clz)
	{
		Field fs[] = clz.getDeclaredFields();
		for (Field f : fs) {
			
			
			Annotation idAnnon = f.getAnnotation(Column.class);
			if (idAnnon!=null && ((Column)idAnnon).id()) {
				return f.getName();
			}
		}
		return null;
	}
	
//	private String genResults(Class clz)
//	{
//		StringBuffer sb = new StringBuffer("	@Results(id = \""+Character.toLowerCase(clz.getSimpleName().charAt(0))
//				+clz.getSimpleName().substring(1)+"Mapper\", value = {");
//		//Method ms[] = clz.getMethods();
//		Field fs[] =clz.getDeclaredFields();
//		for (Field f : fs) {
//			
//			Annotation methodAnnon = f.getAnnotation(Column.class);
//			if (methodAnnon == null) {
//				continue;
//			}
//
//			Column c = (Column) methodAnnon;
//			
//			//Annotation idAnnon = f.getAnnotation(Id.class);
//			if (!c.id()) {
//				sb.append("\r\n			@Result(property = \""+f.getName()+"\", column = \""+c.name()+"\"),");
//			}else {
//				sb.append("\r\n			@Result(property = \""+f.getName()+"\", column = \""+c.name()+"\", id = true),");
//			}
//		}
//		// remove the , in the end
//		sb.deleteCharAt(sb.length()-1);
//		sb.append("\r\n			})\r\n");
//		return sb.toString();
//	}

	
	public static void main(String[] args)
	{
		
	}
}
