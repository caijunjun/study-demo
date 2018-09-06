package com.example.demo;

import reactor.core.publisher.Flux;

public class Test {

	public static void main(String[] args) {
//		Flux.just("Hello", "World").subscribe(System.out::println);
//		Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
//		Flux.empty().subscribe(System.out::println);
//		Flux.range(1, 10).subscribe(System.out::println);
//		Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).subscribe(System.out::println);
		
//		Flux.generate(sink -> {
//		    sink.next(1);;
//		    sink.complete();
//		}).subscribe(System.out::println);
//
//		 
//		final Random random = new Random();
//		Flux.generate(ArrayList::new, (list, sink) -> {
//		    int value = random.nextInt(100);
//		    list.add(value);
//		    sink.next(value);
//		    if (list.size() == 10) {
//		        sink.complete();
//		    }
//		    return list;
//		}).subscribe(System.out::println);
		
//		List<String> list=new ArrayList<>();
//		list.add("1");
//		list.add("0");
//		list.add("xxd");
//		list.add("ds2");
//		list.forEach(System.out::println);
//		
//		Flux.fromStream(list.stream()).subscribe(System.out::println);
//		
//		while(true) {
//			
//		}
		
		
//		Flux.range(1, 100).window(20).subscribe(System.out::println);
		
		Flux.just("a", "b")
        .zipWith(Flux.just("c", "d"))
        .doOnNext(System.out::println)
        .doOnNext(System.out::println)
        .subscribe(System.out::println);
		
//		Flux.just("a", "b")
//        .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
//        .subscribe();
	}
}
