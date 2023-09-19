class QueueNode {
    constructor(value, next = null) {
        this.value = value;
        this.next = next;
    }
}

export default class Queue {
    head = null;
    tail = null;
    size = 0;

    isEmpty() {
        return !this.size;
    }

    push(value) { 
        if(this.head === null) {
            this.head = this.tail = new QueueNode(value);
        }else {
            this.tail.next = new QueueNode(value);
            this.tail = this.tail.next;
        }

        ++this.size;
    }

    pop() {
        if(this.head !== null) {
            this.head = this.head.next;

            --this.size;

            if(this.size === 0) {
                this.tail = null;
            }
        }
    }

    top() {
        return this.head?.value || null;
    }
}