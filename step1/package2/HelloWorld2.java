package package2;

public class HelloWorld2 {

	public void print(){
		HelloWorld2 hw = new HelloWorld2();
		hw.echo();

		int sum = hw.sum(2, 3);
		System.out.println(sum);

		HelloWorld2 hw2 = new HelloWorld2();

		System.out.println("hw "+hw.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw "+hw.increment());
		System.out.println("hw "+hw.increment());
		System.out.println("hw "+hw.increment(100));

		hw.increment();
		System.out.println("hw "+hw.increment());
	}

	void echo(){
		System.out.println("Hello World");
	}

	int sum(int a, int b){
		short d = 5;
		int c=  a+b;
	
		c= c+d;
		return c;
	}

	int contatore = 0;
	int increment(){
		contatore= contatore+1;
		return contatore;
	}

	
	int increment(int a){
		contatore= contatore+a;
		return contatore;
	}

}
