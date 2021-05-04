package com.github.xmybatis.generator;



public class CodeGenerator 
{
	
	
	public static void genCode(Class c,String dir)
	{
		CriteriaGenerator gen = new CriteriaGenerator();
		gen.generateByClass(c, dir);
		
		DaoGenerator daoGen = new DaoGenerator();
		daoGen.generateByClass(c,dir);
		
		SQLProviderGenerator sqlGen = new SQLProviderGenerator();
		sqlGen.generateByClass(c,dir);
		
		
	}
}
