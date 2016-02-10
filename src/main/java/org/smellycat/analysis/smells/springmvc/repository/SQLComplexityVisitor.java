package org.smellycat.analysis.smells.springmvc.repository;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.smellycat.domain.SmellyClass;

public class SQLComplexityVisitor extends ASTVisitor {

	private SmellyClass clazz;
	private int complexity;
	
	private static Set<String> complexityKeywords;
	static {
		complexityKeywords = new HashSet<String>();
		complexityKeywords.add("where");
		complexityKeywords.add("and");
		complexityKeywords.add("or");
		complexityKeywords.add("join");
		complexityKeywords.add("exists");
		complexityKeywords.add("not");
		complexityKeywords.add("from");
	}

	public SQLComplexityVisitor(SmellyClass clazz) {
		this.clazz = clazz;
		this.complexity = 0;
	}
	
	public boolean visit(StringLiteral node) {
		String sql = node.getLiteralValue();
		if(isSql(sql)) {
			calculateComplexity(sql);
			clazz.setAttribute("sql-complexity", complexity);
		}
		
		return super.visit(node);
	}

	private void calculateComplexity(String sql) {
		sql = sql.toLowerCase();
		for(String keyword : complexityKeywords) {
			complexity += StringUtils.countMatches(sql, keyword);
		}
	}

	private boolean isSql(String sql) {
		// TODO can we actually know this is a sql?
		return true;
	}
}