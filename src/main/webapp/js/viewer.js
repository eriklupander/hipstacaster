var viewer = new function() {

    var slideShowIntervalHandle;
    var currentPhotoSet = new Array();

    var windowY = $(window).height();

    var index = 0;

    var cancelSlideShowInterval = function() {
        if(typeof slideShowIntervalHandle != 'undefined' && slideShowIntervalHandle != null) {
            window.clearInterval(slideShowIntervalHandle);
        }
    }

    var showPhoto = function(photoEvent) {
        $('#content').empty();
        var imgLoad = $('<img id="currentphoto" class="photo">');
        imgLoad.attr("src", photoEvent.fullsizeUrl);
        imgLoad.unbind("load");
        imgLoad.bind("load", function () {
            if(typeof this.height != 'undefined' && this.height != null && typeof this.width != 'undefined' && this.width != null) {
                var ratio = this.width / this.height;
                if(this.height > 700) {
                    $('#currentphoto').attr('height', 700);
                    $('#currentphoto').attr('width', 700*ratio);
                } else {
                    $('#currentphoto').attr('height', this.height);
                    $('#currentphoto').attr('width', this.width);
                }
            }
        });

        $('#content').append(imgLoad);
        $('#photo_title').text(photoEvent.title);
        $('#photo_author').text(photoEvent.ownerName);
        if(typeof photoEvent.description != 'undefined' && photoEvent.description != null && photoEvent.description != "null") {
            $('#photo_description').text(photoEvent.description);
        } else {
            $('#photo_description').empty();
        }
        $('#content').fadeIn("fast");
        $('#photo_overlay').fadeIn( "slow" );
    }

    return new function() {

        /**
         * Runs when the receiver app starts, will show the default "welcome image".
         */
        this.init = function() {
            $('#content').empty();
            var imgLoad = $('<img class="photo"/>');
            imgLoad.attr("src", "images/candles720.jpg");
            imgLoad.attr("width", "1280");
            imgLoad.attr("height", "700");
            imgLoad.unbind("load");
            imgLoad.bind("load", function () {
                var marginTop = (windowY - this.height) / 2;
                $('#content').css('margin-top', marginTop);
            });

            $('#content').append(imgLoad);
            $('#content').fadeIn("fast");
        }

        /**
         * Just for clarity, slideshows uses this method instead of directly
         * calling showPhoto.
         *
         * @param photoEvent
         */
        this.viewPhotoInSlideshow = function(photoEvent) {
            showPhoto(photoEvent);
        }

        /**
         * Views a single photo, will cancel any ongoing slideshow when invoked.
         *
         * @param photoEvent
         */
        this.viewPhoto = function(photoEvent) {
            cancelSlideShowInterval();
            showPhoto(photoEvent);
        };

        /**
         * Starts a slideshow of the supplied photoset, starting at the offset parameter encapsulated inside the message body.
         * @param message
         *          contains:
         *          {
         *           'offset': N,
         *           'photoSet':[
         *               {'fullsizeUrl':'...','title':'...','description':'...','ownerName':'...'},
         *               {'fullsizeUrl':'...','title':'...','description':'...','ownerName':'...'},
         *                ...
         *             ]}
         */
        this.startSlideshow = function(message) {
            viewer.showGlobalOnScreenMessage('Slideshow starting');
            cancelSlideShowInterval();
            index = 0;
            if(typeof message.offset != 'null' && message.offset != null) {
                index = message.offset+1;
            }

            var startingOffset = index-1;

            currentPhotoSet = message.photoSet;

            slideShowIntervalHandle = window.setInterval(function() {
                if(typeof currentPhotoSet != 'undefined' && currentPhotoSet != null && currentPhotoSet.length > 0) {
                    if(index < currentPhotoSet.length) {
                        $('#content').fadeOut("fast", function() {
                            // Preload next if possible
                            if(index+2 < currentPhotoSet.length) {
                                $('#offscreen_img').attr('src', currentPhotoSet[index+2].fullsizeUrl);
                            }
                            viewer.viewPhotoInSlideshow(currentPhotoSet[index++]);

                            // For demoing purposes, the receiver app will send a message to the sender telling it the current
                            // index / total photo being viewed.
                            cast.HipstaCaster.notifyCurrentPhoto("" + (index - startingOffset) + " of " + (currentPhotoSet.length - startingOffset));
                        });

                    } else {
                        cancelSlideShowInterval();

                        // Run the init method so the welcome screen is shown again.
                        viewer.init();
                        // We have reached the end of the slideshow. Notify the sender app about this.
                        cast.HipstaCaster.notifySlideShowEnded();
                        viewer.showGlobalOnScreenMessage('Slideshow ended');
                    }
                }
            }, 5000);
        };

        this.showGlobalOnScreenMessage = function(msg) {
            $('#content').append('<div class="global_message">' + msg + '</div>');
            $('.global_message').fadeOut(10000, function() {
                $('.global_message').remove();
            });
        };
    }

}($);