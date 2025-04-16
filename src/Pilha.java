public class Pilha {
    
    int counter = 0;
    int size;
    Node head;
    Node tail;
    
    public Pilha(int size) {
        this.size = size;
        this.head = null;
        this.tail = null;
    }

    public void push(int data){
        Node newNode = new Node(data);

        if(head == null){
            head = newNode;
            tail = newNode;
            counter++;
        }else{
            Node temp = head;
            head = newNode;
            head.next = temp;
            counter++;
        }
    }

    public void pop(){
        if(head == null){
            System.out.println("Pilha vazia");
        }else{
            head = head.next;
            counter--;
        }
    }

    public void peek(){
        if(head == null){
            System.out.println("Pilha vazia");
        }else{
            System.out.println(head.data);
        }
    }

    public void isEmpty(){
        if(head == null){
            System.out.println("Pilha vazia");
        }
    }

    public void isFull(){
        if(counter >= size){
            System.out.println("Pilha cheia");
        }
    }

}
