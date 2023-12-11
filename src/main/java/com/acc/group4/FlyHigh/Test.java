package com.acc.group4.FlyHigh;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.base.Predicate;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> str = Arrays.asList(8,2,10,4,5);
		Stream<Integer> stream = str.stream();
		System.out.println("List :: "+str.toString() );
		//stream.forEach(n->System.out.println("Data:: "+n));
		//System.out.println(stream.count());
		Predicate<Integer> pr = n->n%2==1;
		Stream<Integer> sortedOne = stream.sorted();
		sortedOne.filter(n->n%2==1).map(n -> n*2).forEach(n->System.out.println("N"+n));
       
	}

}
