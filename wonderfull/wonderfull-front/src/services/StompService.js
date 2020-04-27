import {
  Client
} from '@stomp/stompjs';

import StompSubscription from './StompSubscription'

const SUBSCRIPTIONS = Symbol();
const CLIENT = Symbol();
const STATUS = Symbol();
const LISTENERS = Symbol();
const BROKER_URL = Symbol();
const XCSRF = Symbol();

const SET_STATUS = Symbol();

function wrap(callback) {
  return frame => {
    const parsed = JSON.parse(frame.body);
    callback(parsed);
  };
}

class StompService {

  constructor() {
    this[STATUS] = false;
    this[SUBSCRIPTIONS] = [];
    this[LISTENERS] = [];
    this[XCSRF] = null;

    var brokerURL = process.env.VUE_APP_BACKEND_WS;
    if (!brokerURL) {
      const hostport = location.hostname + ":" + location.port;
      const protocol = location.protocol.replace(/^http/, 'ws')
      brokerURL = protocol + "//" + hostport + "/ws";
    }

    this[BROKER_URL] = brokerURL;

    const client = new Client({
      brokerURL,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000
    });
    client.beforeConnect = () => {
      this[CLIENT].connectHeaders["X-CSRF-TOKEN"] = this[XCSRF];
    }
    client.onConnect = () => {
      this[SUBSCRIPTIONS] = this[SUBSCRIPTIONS].map(s => {
        var sub = s.sub;
        s.handle = this[CLIENT].subscribe(sub.topic, sub.callback);
        return s;
      });
      this[SET_STATUS](true);
    };
    client.onWebSocketClose = () => {
      this[SET_STATUS](false);
    };
    this[CLIENT] = client;
  }

  [SET_STATUS](connected) {
    this[STATUS] = connected;

    this[LISTENERS].forEach(l => l(this));
  }

  get brokerURL() {
    return this[BROKER_URL];
  }

  addListener(callback) {
    this[LISTENERS].push(callback);

    callback(this);
  }

  publish(destination, action) {
    const msg = {
      destination,
      headers: {
        'content-type': 'application/json'
      }
    }
    if (action)
      msg.body = JSON.stringify(action);

    this[CLIENT].publish(msg);
  }

  subscribe(topic, callback) {
    const index = this[SUBSCRIPTIONS].length;
    const sub = new StompSubscription(index, this, topic, wrap(callback));

    const handled = {
      handle: null,
      sub
    }
    this[SUBSCRIPTIONS].push(handled);

    if (this.status) {
      handled.handle = this[CLIENT].subscribe(topic, sub.callback);
    }

    return sub;
  }

  unsubscribe(subscription) {
    var subs = this[SUBSCRIPTIONS];
    var removed = subs[subscription.index];

    if (subscription === removed) {
      subs.splice(subscription.index, 1);

      if (this.status) {
        removed.handle.unsubscribe();
      }
    }
  }

  get status() {
    return this[STATUS];
  }

  deactivate() {
    try {
      this[CLIENT].deactivate();
    } catch (e) {
      //noop
    }
  }

  connect(xcsrf) {

    this.deactivate();
    this[XCSRF] = xcsrf;
    this[CLIENT].activate();
  }
}

export default new StompService();
