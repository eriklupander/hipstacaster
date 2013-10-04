var flickr = new function() {

    return new function() {
        this.demo = function() {
            $('body').empty();
            var albums = client.getPopularAlbums();
            for(var a = 0; a < albums.length; a++) {
                var album = albums[a];
                var photo = client.getPhoto(album.primaryPhotoId);

                $('body').append('<div class="album"><div><img id="' + album.id + '" src="' + photo.squareUrl + '"></div><div>' + album.title + '</div></div>');

                (function (_albumId) {
                    $('#' + album.id).unbind().click(function(){
                        flickr.loadAlbum(_albumId);
                    });
                })(album.id);
            }
        };
		
		this.loadAlbum = function(albumId) {
			var photos = client.getPhotosOfAlbum(albumId);
			$('body').empty();
			for(var a = 0; a < photos.length; a++) {
				var photo = photos[a];
				$('body').append('<img id="' + photo.id + '" src="' + photo.squareUrl + '">');

                (function (_photoId, _prevId, _nextId) {
                    $('#' + photo.id).unbind().click(function(){
                        flickr.loadPhoto(_photoId, _prevId, _nextId);
                    });
                })(photo.id, photo.prevPhotoId, photo.nextPhotoId);
			}
		};
		
		this.loadPhoto = function(photoId, prevPhotoId, nextPhotoId) {
			var photo = client.getPhoto(photoId);
			$('body').empty();
			$('body').append('<img id="' + photo.id + '" src="' + photo.fullsizeUrl + '">');
			$('body').append('<div><button id="prev">Prev</button>');
			$('body').append('<button id="next">Next</button></div>');
			
			$('#next').unbind().click(function() {
				if(typeof nextPhotoId != 'undefined' && nextPhotoId != null) {
                    flickr.loadPhoto(nextPhotoId);
				}
			});
			
			$('#prev').unbind().click(function() {
				if(typeof prevPhotoId != 'undefined' && prevPhotoId != null) {
                    flickr.loadPhoto(prevPhotoId);
				}
			});
		};
		
    }

}($);