package com.study.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestCopyOnWriteArrayList {

	public static void main(String[] args) {
		
		List<String> list=new ArrayList<>();
		list.add("2323");
		
		int index=2;
		
		Object[] objs=new Object[] {"s",2,"dsfs","2"};
		//删除index为2的 也就是dsfs
		Object oldValue=objs[index];
		
		Object[] newObjs=new Object[objs.length-1];
		
		System.arraycopy(objs, 0, newObjs, 0, index);
		System.arraycopy(objs, index+1, newObjs, index, objs.length-index-1);
	
		CopyOnWriteArrayList<String> cwList=new CopyOnWriteArrayList<>(new String[]{"11","ss","dfsd"});
		
		cwList.remove("11");
	}
}
