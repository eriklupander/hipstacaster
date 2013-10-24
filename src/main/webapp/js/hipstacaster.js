// External namespace for cast specific javascript library
var cast = window.cast || {};

// Anonymous namespace
(function () {
    'use strict';

    HipstaCaster.PROTOCOL = 'com.squeed.chromecast.hipstacaster';

    /**
     * Will use the broadcast method to send the message to all connected senders.
     * @param msg
     */
    HipstaCaster.sendMessage = function (msg) {
        hipstacaster.broadcast(msg);
    }

    /**
     * Creates a HipstaCast object and attaches a
     * cast.receiver.ChannelHandler, which receives messages from the
     * channel between the sender and receiver.

     * @constructor
     */
    function HipstaCaster() {

        this.mChannelHandler =
            new cast.receiver.ChannelHandler('HipstaCasterDebug');

        /** Listeners */
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


    // Adds event listening functions to HipstaCaster.prototype.
    HipstaCaster.prototype = {

        /**
         * Channel opened event; checks number of open channels.
         * @param {event} event the channel open event.
         */
        onChannelOpened: function (event) {
            console.log('onChannelOpened. Total number of channels: ' +
                this.mChannelHandler.getChannels().length);
        },

        /**
         * Channel closed event; if all devices are disconnected,
         * closes the application.
         * @param {event} event the channel close event.
         */
        onChannelClosed: function (event) {
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
        onMessage: function (event) {

            var message = event.message;
            if (message.command == 'viewphoto') {
                viewer.viewPhoto(message);
            } else if (message.command == 'slideshow') {
                viewer.startSlideshow(message);
            }
        },

        sendError: function (channel, errorMessage) {
            channel.send({ event: 'error',
                message: errorMessage });
        },

        /**
         * Broadcasts a message to all of this object's known channels.
         * @param {Object|string} message the message to broadcast.
         */
        broadcast: function (message) {
            this.mChannelHandler.getChannels().forEach(
                function (channel) {
                    channel.send(message);
                });
        }
    };

    cast.HipstaCaster = HipstaCaster;


    /** Methods */
    cast.HipstaCaster.notifySlideShowEnded = function () {
        var payload = {
            "event": "slideshow_ended"
        };
        HipstaCaster.sendMessage(payload);
    };

    cast.HipstaCaster.notifyCurrentPhoto = function (msg) {
        var payload = {
            "event": "slideshow_current_image",
            "text": msg
        };
        HipstaCaster.sendMessage(payload);
    };

})();
