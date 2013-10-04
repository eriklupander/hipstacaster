var flickr = new function() {

    return new function() {
        this.demo = function() {
            var albums = client.getPopularAlbums();
            for(var a = 0; a < albums.length; a++) {
                var album = albums[a];
                var photo = client.getPhoto(album.primaryPhotoId);
                $('body').append('<img src="' + photo.thumbnailUrl + '">');
            }
        }
    }

}($);