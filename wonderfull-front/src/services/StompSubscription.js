const SERVICE = Symbol();
const TOPIC = Symbol();
const CALLBACK = Symbol();
const INDEX = Symbol();

export default class StompSubscription {
  constructor(index, service, topic, callback) {
    this[INDEX] = index;
    this[SERVICE] = service;
    this[TOPIC] = topic;
    this[CALLBACK] = callback;
  }

  unsubscribe() {
    this[SERVICE].unsubscribe(this);
  }

  get index() {
    return this[INDEX];
  }

  get topic() {
    return this[TOPIC];
  }

  get callback() {
    return this[CALLBACK];
  }
}
