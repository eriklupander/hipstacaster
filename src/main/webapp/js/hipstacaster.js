// External namespace for cast specific javascript library
var cast = window.cast || {};

// Anonymous namespace
(function() {
  'use strict';

  HipstaCaster.PROTOCOL = 'com.squeed.chromecast.hipstacaster';

  /**
   * Creates a HipstaCast object and attaches a
   * cast.receiver.ChannelHandler, which receives messages from the
   * channel between the sender and receiver.
   
   * @constructor
   */
  function HipstaCaster() {
    
    this.mChannelHandler =
        new cast.receiver.ChannelHandler('HipstaCasterDebug');
    this.mChannelHandler.addEventListener(
        cast.receiver.Channel.EventType.MESSAGE,
        this.onMessage.bind(this));
    this.mChannelHandler.addEventListener(
        cast.receiver.Channel.EventType.OPEN,
        this.onChannelOpened.bind(this));
    this.mChannelHandler.addEventListener(
        cast.receiver.Channel.EventType.CLOSED,
        this.onChannelClosed.bind(this));
  }

  // Adds event listening functions to HipstaCast.prototype.
  HipstaCaster.prototype = {

    /**
     * Channel opened event; checks number of open channels.
     * @param {event} event the channel open event.
     */
    onChannelOpened: function(event) {
      console.log('onChannelOpened. Total number of channels: ' +
          this.mChannelHandler.getChannels().length);
    },

    /**
     * Channel closed event; if all devices are disconnected,
     * closes the application.
     * @param {event} event the channel close event.
     */
    onChannelClosed: function(event) {
      console.log('onChannelClosed. Total number of channels: ' +
          this.mChannelHandler.getChannels().length);

      if (this.mChannelHandler.getChannels().length == 0) {
        window.close();
      }
    },

    /**
     * Message received event; determines event message and command, and
     * choose function to call based on them.
     * @param {event} event the event to be processed.
     */
    onMessage: function(event) {
      var message = event.message;
      var channel = event.target;
      console.log('********onMessage********' + JSON.stringify(message));
    
      if (message.command == 'next') {
        this.onNext(channel, message);
      } else if (message.command == 'prev') {
        this.onPrev(channel);
      } else if (message.command == 'close') {
        this.onClose(channel, message);
      } else {
        cast.log.error('Invalid message command: ' + message.command);
      }
    },

   
    onNext: function(channel, message) {
      console.log('****onNext: ' + JSON.stringify(message));      
    },
    onPrev: function(channel, message) {
      console.log('****onPrev: ' + JSON.stringify(message));      
    },
	onClose: function(channel, message) {
      console.log('****onClose: ' + JSON.stringify(message));      
    },
    

    sendError: function(channel, errorMessage) {
      channel.send({ event: 'error',
                     message: errorMessage });
    },

    /**
     * @private
     */
    startGame_: function() {
      console.log('****startGame');
      
     // this.mPlayer1.channel.send({ event: 'joined',
      //                             player: this.mPlayer1.player,
      //                             opponent: this.mPlayer2.name });
    },

    /**
     * Broadcasts a message to all of this object's known channels.
     * @param {Object|string} message the message to broadcast.
     */
    broadcast: function(message) {
      this.mChannelHandler.getChannels().forEach(
        function(channel) {
          channel.send(message);
        });
    }

  };

  // Exposes public functions and APIs
  cast.HipstaCaster = HipstaCaster;
})();
