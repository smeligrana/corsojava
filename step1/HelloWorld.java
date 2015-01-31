public class HelloWorld {
	public static void main(String[] args){
		HelloWorld hw = new HelloWorld();
		hw.echo();

		int sum = hw.sum(2, 3);
		System.out.println(sum);

		HelloWorld hw2 = new HelloWorld();

		System.out.println("hw "+hw.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw2 "+hw2.increment());
		System.out.println("hw "+hw.increment());
		System.out.println("hw "+hw.increment());
	}

	void echo(){
		System.out.println("Hello World");
	}

	int sum(int a, int b){
		return a +b;
	}

	private int contatore = 0;
	int increment(){
		return contatore++;
	}
}
