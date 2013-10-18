var flickr = new function() {

    var slideShowIntervalHandle;
    var currentPhotoSet = new Array();

    var windowX = $(window).width();
    var windowY = $(window).height();

    var index = 0;
    var page = 0;

    var cancelSlideShow = function() {
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

        this.viewPhotoInSlideshow = function(photoEvent) {
            showPhoto(photoEvent);
        }

        this.viewPhoto = function(photoEvent) {
            cancelSlideShow();
            showPhoto(photoEvent);
        };

        this.startSlideshow = function(message) {
            flickr.showGlobalOnScreenMessage('Slideshow starting');
            cancelSlideShow();
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
                            flickr.viewPhotoInSlideshow(currentPhotoSet[index++]);
                            cast.HipstaCaster.notifyCurrentPhoto("" + (index - startingOffset) + " of " + (currentPhotoSet.length - startingOffset));
                        });

                    } else {
                        cancelSlideShow();

                        flickr.init();
                        // We have reached the end of the slideshow. Notify the sender app about this.
                        cast.HipstaCaster.notifySlideShowEnded();
                        flickr.showGlobalOnScreenMessage('Slideshow ended');
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