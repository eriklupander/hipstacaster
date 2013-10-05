var flickr = new function() {

    var currentPhotoSet = new Array();

    return new function() {
        this.demo = function() {
            $('body').empty();
            var photos = client.search('sarek');
            currentPhotoSet = photos;

            flickr.renderCurrentPhotoSet();
        };

        this.renderCurrentPhotoSet = function() {
            for(var a = 0; a < currentPhotoSet.length; a++) {

                var photo = currentPhotoSet[a];
                $('body').append('<div class="album"><div><img id="' + photo.id + '" src="' + photo.squareUrl + '"></div><div class="author-small">' + photo.ownerName + '</div></div>');

                (function (_a) {
                    $('#' + photo.id).unbind().click(function(){
                        flickr.loadPhoto(_a);
                    });
                })(a);
            }
        }

		
		this.loadPhoto = function(index) {
			var photo = currentPhotoSet[index];
			$('body').empty();
			$('body').append('<img id="' + photo.id + '" src="' + photo.fullsizeUrl + '">');



            if(index < currentPhotoSet.length - 1) {
                //$('#nextphoto').attr('src', currentPhotoSet[index+1].fullsizeUrl);
                $('body').append('<button id="next">Next</button></div>');
                $('#next').unbind().click(function() {
                    flickr.loadPhoto(++index);
                });
            }


            if(index > 0) {
                $('body').append('<div><button id="prev">Prev</button>');
                $('#prev').unbind().click(function() {
                    flickr.loadPhoto(--index);
                });
            }

            $('body').append('<button id="back">Refresh</button></div>');
            $('#back').unbind().click(function() {
                flickr.demo();
            });

            flickr.renderCurrentPhotoSet();
		};
		
    }

}($);