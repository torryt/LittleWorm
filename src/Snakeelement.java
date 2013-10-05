
public class Snakeelement extends Grafikkelement {

	public Snakeelement tail = null;
	
	
	public Snakeelement() {
	
	}
	
	/**
	 * Creates a new Snakeelement which is a copy of the Snakeelement
	 * that is passed to the method.
	 * @param head
	 */
	public Snakeelement(Snakeelement head) {
		x = head.x;
		y = head.y;
	}
	
	public Snakeelement(int startX,int startY) {
		x = startX;
		y = startY;
	}
	
	
	/*
	 * Methods for snakeelement
	 */
	public Snakeelement getLastElement(Snakeelement snake) {
		if(snake.tail.tail == null) {
			return snake.tail;
		}
		else {
			return getLastElement(snake.tail);
		}
	}
	
	
	public Snakeelement getNext() {
		return this.tail;
	}
	
	
	//Recursive method that deletes the last element of the snake
	/** Removes last element of the snake.
	 * @param snake Takes the head-element of the snake as input.
	 */
	public void removeLastElement(Snakeelement snake) {
		
		if(snake.tail.tail == null) {
			snake.tail = null;
		}
		else {
			removeLastElement(snake.tail);
		}
	}

	public void addTail(int numElements) {
		getLastElement(this).tail = new Snakeelement(this.tail);
		numElements--;
		
		if( numElements > 0) {
			addTail(numElements);
		}
	}
}