package com.github.xmybatis.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;



public class SQLProviderGenerator 
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
		File f = new File(dirName + File.separator + target + File.separator + cls.getSimpleName() + "SQL.java");
		System.out.println("target file:"+f.getAbsolutePath());
		try {
			FileOutputStream output = new FileOutputStream(f);
			PrintWriter pw = new PrintWriter(output);

			pw.println("package " + pkg.getName() + ";");
			pw.println();
			pw.println("import com.github.xmybatis.core.SQLProvider;\r\n" );
			pw.println();
			pw.print("public class "+cls.getSimpleName()+"SQL extends SQLProvider\r\n" + 
					"{	\r\n" + 
					"	public "+cls.getSimpleName()+"SQL() {\r\n" + 
					"		super("+cls.getSimpleName()+".class);\r\n" + 
					"	}\r\n" + 
					"}");
			pw.flush();
			pw.close();
		}catch(Exception e)
		{
			
		}
		return true;
	}
}
