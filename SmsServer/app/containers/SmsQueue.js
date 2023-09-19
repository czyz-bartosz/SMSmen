import Queue from "./Queue.js"

export default class SmsQueue {
    queue = new Queue();

    isEmpty() {
        return this.queue.isEmpty();
    }

    add(value) {
        this.queue.push(value);
    }

    remove() {
        this.queue.pop();
    }

    get() {
        return this.queue.top();
    }
}